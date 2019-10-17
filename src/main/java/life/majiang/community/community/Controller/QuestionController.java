package life.majiang.community.community.Controller;

import life.majiang.community.community.dto.CommentDto;
import life.majiang.community.community.dto.QuestionDto;
import life.majiang.community.community.enums.CommentTypeEnum;
import life.majiang.community.community.service.CommentService;
import life.majiang.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
//页面详情页
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name="id") Long id, Model model){
        QuestionDto questionDto=questionService.getById(id);
        List<QuestionDto> relatedQuestions=questionService.selectRelated(questionDto);
        List<CommentDto> comments=commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        questionService.incView(id);
        model.addAttribute("question",questionDto);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }
}
