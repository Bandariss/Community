package life.majiang.community.community.interceptor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private SessionInterceptor sessionInterceptor;//登陆拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**").excludePathPatterns("/admin/**");//排除静态资源拦截
    }
}
