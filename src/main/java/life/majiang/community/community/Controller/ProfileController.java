package life.majiang.community.community.Controller;

import life.majiang.community.community.dto.PaginationDto;
import life.majiang.community.community.mapper.UserMapper;
import life.majiang.community.community.module.User;
import life.majiang.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;


    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name="action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name="page",defaultValue = "1") Integer page,
                          @RequestParam(name="size",defaultValue = "5") Integer size){
        User user=null;
        Cookie[] cookies=request.getCookies();
        if(cookies !=null&&cookies.length!=0){
            for (Cookie cookie : cookies) {
                //页面再刷新时，会在cookies中查找是否有该user，并放到session中交给前端判断
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        if(user==null){
            return "redirect/";
        }
        if("questions".equals(action)){//点击我的问题选项
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的问题");
        }
        else if("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }

        questionService.list(user.getId(),page,size);
        PaginationDto paginationDto=questionService.list(user.getId(),page,size);
        model.addAttribute("pagination",paginationDto);
        return "profile";
    }
}
