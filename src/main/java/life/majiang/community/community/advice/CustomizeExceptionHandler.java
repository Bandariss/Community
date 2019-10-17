package life.majiang.community.community.advice;


import com.alibaba.fastjson.JSON;
import life.majiang.community.community.dto.ResultDto;
import life.majiang.community.community.exception.CustomizeErrorCode;
import life.majiang.community.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, Throwable e, Model model, HttpServletResponse response) {
        String contentType=request.getContentType();
        if("application/json".equals(contentType)){
            ResultDto resultDto;
            //返回json
            if (e instanceof CustomizeException) {
                resultDto=ResultDto.errorOf((CustomizeException) e);
            } else {
                resultDto=ResultDto.errorOf(CustomizeErrorCode.SYSTEM_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("UTF-8");
                PrintWriter writer=response.getWriter();
                writer.write(JSON.toJSONString(resultDto));
                writer.close();

            } catch (IOException ioe) {
            }
            return  null;
        }
        else {
            //页面跳转
            if (e instanceof CustomizeException) {
                model.addAttribute("message", e.getMessage());
            } else {
                model.addAttribute("message", CustomizeErrorCode.SYSTEM_ERROR.getMessage());
            }
            return new ModelAndView("error");//错误后返回自定义的页面
        }
    }
}
