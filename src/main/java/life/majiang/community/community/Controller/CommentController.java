package life.majiang.community.community.Controller;

import life.majiang.community.community.dto.CommentCreateDto;
import life.majiang.community.community.dto.CommentDto;
import life.majiang.community.community.dto.ResultDto;
import life.majiang.community.community.enums.CommentTypeEnum;
import life.majiang.community.community.exception.CustomizeErrorCode;
import life.majiang.community.community.model.Comment;
import life.majiang.community.community.model.User;
import life.majiang.community.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value="/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDto commentCreateDto, HttpServletRequest request){
        User user=(User)request.getSession().getAttribute("user");
        if(user==null){
            return ResultDto.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if(commentCreateDto==null|| StringUtils.isBlank(commentCreateDto.getContent())){
            return ResultDto.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment=new Comment();
        comment.setParentId(commentCreateDto.getParentId());
        comment.setContent(commentCreateDto.getContent());
        comment.setType(commentCreateDto.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentor(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment,user);
        return ResultDto.okOf();
    }


    @ResponseBody
    @RequestMapping(value="/comment/{id}",method = RequestMethod.GET)
    public ResultDto<List> comments(@PathVariable(name="id") Long id){
       List<CommentDto> commentDtos= commentService.listByTargetId(id,CommentTypeEnum.COMMENT);
        return ResultDto.okOf(commentDtos);
    }
}
