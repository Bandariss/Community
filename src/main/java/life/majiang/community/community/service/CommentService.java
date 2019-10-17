package life.majiang.community.community.service;

import life.majiang.community.community.dto.CommentDto;
import life.majiang.community.community.enums.CommentTypeEnum;
import life.majiang.community.community.enums.NotificationTypeEnum;
import life.majiang.community.community.enums.NotificationStatusEnum;
import life.majiang.community.community.exception.CustomizeErrorCode;
import life.majiang.community.community.exception.CustomizeException;
import life.majiang.community.community.mapper.*;
import life.majiang.community.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment, User commentor) {
        //验证
        if(comment.getParentId()==null||comment.getParentId()==0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if(comment.getType()==null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if(comment.getType()==CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbcomment=commentMapper.selectByPrimaryKey(comment.getParentId());//找到评论
            if(dbcomment==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            Question question=questionMapper.selectByPrimaryKey(dbcomment.getParentId());//根据找到的评论找到问题
            if(question==null){
                throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);

            //增加评论数
            Comment parentComment=new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
            //创建通知
            Long receiver=dbcomment.getCommentor();
            createNotify(comment, receiver, commentor.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT,question.getId());//创建通知
        }
        else{
            //回复问题
            Question question=questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question==null){
                throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            comment.setCommentCount(0);
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
            //创建通知
            createNotify(comment,question.getCreator(),commentor.getName(),question.getTitle(), NotificationTypeEnum.REPLY_QUESTION,question.getId());
        }
    }

    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType,Long outerId) {
        if(receiver==comment.getCommentor()){
            return;//自己发出的评论不需要通知
        }
        Notification notification=new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(outerId);
        notification.setNotifier(comment.getCommentor());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    public List<CommentDto> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample  commentExample=new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments=commentMapper.selectByExample(commentExample);
        if(comments.size()==0){
            return new ArrayList<>();
        }
        //获取去重的评论人,并且把Id放到userIds里面
        Set<Long> commentors=comments.stream().map(comment->comment.getCommentor()).collect(Collectors.toSet());
        List<Long>userIds=new ArrayList<>();
        userIds.addAll(commentors);

        //获取评论人并且转换成Id和user的Map
        UserExample userExample=new UserExample();
        userExample.createCriteria().
                andIdIn(userIds);
        List<User>users=userMapper.selectByExample(userExample);
        Map<Long,User> userMap=users.stream().collect(Collectors.toMap(user->user.getId(), user->user));

        //转换comment为commentDto
        List<CommentDto>commentDtos=comments.stream().map(comment -> {CommentDto commentDto=new CommentDto();
            BeanUtils.copyProperties(comment,commentDto);
            commentDto.setUser(userMap.get(comment.getCommentor()));
        return commentDto;}).collect(Collectors.toList());

        return commentDtos;
    }
}
