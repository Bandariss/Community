package life.majiang.community.community.provider;

import com.alibaba.fastjson.JSON;
import life.majiang.community.community.dto.AccessTokenDto;
import life.majiang.community.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component   //之后调用该类时，不需要初始化了(已经注入到IOC)
public class GithubProvider {
   public String getAccessToken(AccessTokenDto accessTokenDto){
       MediaType mediaType
               = MediaType.get("application/json; charset=utf-8");

       OkHttpClient client = new OkHttpClient();

           RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDto));
           Request request = new Request.Builder()
                   .url("https://github.com/login/oauth/access_token")
                   .post(body)
                   .build();
           try (Response response = client.newCall(request).execute()) {
               String string=response.body().string();
               String token=string.split("&")[0].split("=")[1];//获取token
               System.out.println(string);
               return token;
           } catch (Exception e) {
               e.printStackTrace();
           }
   return null;
   }
   public GithubUser getUser(String accessToken){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().
                url("http://api.github.com/user?access_token="+accessToken).build();
        try {
            Response response=client.newCall(request).execute();
            String string=response.body().string();
            GithubUser githubUser=JSON.parseObject(string,GithubUser.class);//将json转换成GithubUser类对象
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
