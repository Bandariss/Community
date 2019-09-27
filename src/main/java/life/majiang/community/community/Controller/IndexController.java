package life.majiang.community.community.Controller;

import life.majiang.community.community.dto.PaginationDto;
import life.majiang.community.community.dto.QuestionDto;
import life.majiang.community.community.mapper.QuestionMapper;
import life.majiang.community.community.mapper.UserMapper;
import life.majiang.community.community.module.Question;
import life.majiang.community.community.module.User;
import life.majiang.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class IndexController {



    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(
                        Model model,
                        @RequestParam(name="page",defaultValue = "1") Integer page,
                        @RequestParam(name="size",defaultValue = "5") Integer size){

        PaginationDto pagination=questionService.list(page,size);
        model.addAttribute("pagination", pagination);
        return "index";
    }
}
