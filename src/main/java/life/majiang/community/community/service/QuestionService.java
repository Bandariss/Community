package life.majiang.community.community.service;

import life.majiang.community.community.dto.PaginationDto;
import life.majiang.community.community.dto.QuestionDto;
import life.majiang.community.community.dto.QuestionQueryDto;
import life.majiang.community.community.exception.CustomizeErrorCode;
import life.majiang.community.community.exception.CustomizeException;
import life.majiang.community.community.mapper.QuestionExtMapper;
import life.majiang.community.community.mapper.QuestionMapper;
import life.majiang.community.community.mapper.UserMapper;
import life.majiang.community.community.model.Question;
import life.majiang.community.community.model.QuestionExample;
import life.majiang.community.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;


    public PaginationDto list(String search,Integer page, Integer size) {
        if(StringUtils.isNotBlank(search)){
            String[]tags=StringUtils.split(search," ");
            search=Arrays.stream(tags).collect(Collectors.joining("|"));
        }

        PaginationDto paginationDto=new PaginationDto();
        Integer totalPage;
        QuestionQueryDto questionQueryDto=new QuestionQueryDto();
        questionQueryDto.setSearch(search);
        Integer totalCount=questionExtMapper.countBySearch(questionQueryDto);

        if(totalCount % size==0){
            totalPage=totalCount/size;
        }
        else{
            totalPage=totalCount/size+1;
        }


        if(page>totalPage){
            page=totalPage;
        }
        if(page<1){
            page=1;
        }


        paginationDto.setPagination(totalPage,page);
        Integer offset=size*(page-1);
        QuestionExample questionExample=new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");
        questionQueryDto.setSize(size);
        questionQueryDto.setPage(offset);
        List<Question>questions=questionExtMapper.selectBySearch(questionQueryDto);
        List<QuestionDto>questionDtoList=new ArrayList<>();

        for(Question question:questions){
            User user=userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto questionDto=new QuestionDto();
            questionDto.setId(question.getId());
            questionDto.setDescription(question.getDescription());
            questionDto.setCommentCount(question.getCommentCount());
            questionDto.setCreator(question.getCreator());
            questionDto.setGmtCreate(question.getGmtCreate());
            questionDto.setViewCount(question.getViewAccount());
            questionDto.setGmtModified(question.getGmtModified());
            questionDto.setTag(question.getTag());
            questionDto.setLikeCount(question.getLikeCount());
            questionDto.setTitle(question.getTitle());//快速将question中的属性拷贝到questionDto
            questionDto.setUser(user);//此时question中包含了User对象
            questionDtoList.add(questionDto);
        }
        paginationDto.setData(questionDtoList);
        return paginationDto;
    }

    public PaginationDto list(Long userId, Integer page, Integer size) {
        PaginationDto paginationDto=new PaginationDto();
        Integer totalPage;
        QuestionExample questionExample=new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount=(int)questionMapper.countByExample(questionExample);

        if(totalCount % size==0){
            totalPage=totalCount/size;
        }
        else{
            totalPage=totalCount/size+1;
        }


        if(page>totalPage){
            page=totalPage;
        }
        if(page<1){
            page=1;
        }

        paginationDto.setPagination(totalPage,page);

        Integer offset=size*(page-1);

        QuestionExample example=new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        List<Question>questions=questionMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));
        List<QuestionDto>questionDtoList=new ArrayList<>();

        for(Question question:questions){
            User user=userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto questionDto=new QuestionDto();
            questionDto.setId(question.getId());
            questionDto.setDescription(question.getDescription());
            questionDto.setCommentCount(question.getCommentCount());
            questionDto.setCreator(question.getCreator());
            questionDto.setGmtCreate(question.getGmtCreate());
            questionDto.setViewCount(question.getViewAccount());
            questionDto.setGmtModified(question.getGmtModified());
            questionDto.setTag(question.getTag());
            questionDto.setLikeCount(question.getLikeCount());
            questionDto.setTitle(question.getTitle());//快速将question中的属性拷贝到questionDto
            questionDto.setUser(user);//此时question中包含了User对象
            questionDtoList.add(questionDto);
        }

        paginationDto.setData(questionDtoList);
        return paginationDto;
    }

    public QuestionDto getById(Long id) {
        //根据问题的id查找
        Question question=questionMapper.selectByPrimaryKey(id);
        if(question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDto questionDto=new QuestionDto();
        questionDto.setDescription(question.getDescription());
        questionDto.setCommentCount(question.getCommentCount());
        questionDto.setCreator(question.getCreator());
        questionDto.setGmtCreate(question.getGmtCreate());
        questionDto.setViewCount(question.getViewAccount());
        questionDto.setGmtModified(question.getGmtModified());
        questionDto.setTag(question.getTag());
        questionDto.setLikeCount(question.getLikeCount());
        questionDto.setTitle(question.getTitle());
        questionDto.setId(question.getId());
        //将作者信息放入
        User user=userMapper.selectByPrimaryKey(question.getCreator());
        questionDto.setUser(user);
        return questionDto;
    }

    public void createOrUpdate(Question question) {
        if(question.getId()==null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewAccount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        }
        else{
            //更新
            question.setGmtModified(question.getGmtCreate());
            Question updateQuestion=new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example=new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int updated=questionMapper.updateByExampleSelective(updateQuestion,example);
            if(updated !=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question question=new Question();
        question.setId(id);
        question.setViewAccount(1);
        questionExtMapper.incView(question);
    }

    public List<QuestionDto> selectRelated(QuestionDto queryDto) {
        if(StringUtils.isBlank(queryDto.getTag())){
            return new ArrayList<>();
        }
        String[]tags=StringUtils.split(queryDto.getTag(),",");
        String regexpTag=Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question=new Question();
        question.setId(queryDto.getId());
        question.setTag(regexpTag);

        List<Question>questions=questionExtMapper.selectRelated(question);
        List<QuestionDto>questionDtos=questions.stream().map(q->{QuestionDto questionDto=new QuestionDto();
            BeanUtils.copyProperties(q,questionDto);
        return questionDto;})
                .collect(Collectors.toList());//把先转换成流List<Question>questions，再用lambda表达式将流中的每个元素转换成questionDto再转换成List
        return  questionDtos;
    }
}
