package life.majiang.community.community.Controller;

import life.majiang.community.community.dto.QuestionDto;
import life.majiang.community.community.mapper.QuestionExtMapper;
import life.majiang.community.community.mapper.QuestionMapper;
import life.majiang.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {
//页面详情页
    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name="id") Integer id, Model model){
        QuestionDto questionDto=questionService.getById(id);
        questionService.incView(id);
        model.addAttribute("question",questionDto);
        return "question";
    }
}
