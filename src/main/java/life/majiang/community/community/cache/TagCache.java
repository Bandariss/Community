package life.majiang.community.community.cache;

import life.majiang.community.community.dto.TagDto;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagCache {

    public static List<TagDto> get(){
        List<TagDto>tagDtos=new ArrayList<>();
        TagDto program=new TagDto();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("js","php","css","html","java","node","python","c++","c","golang"));
        tagDtos.add(program);

        TagDto framework=new TagDto();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("laravel","spring","express","django","flask","yii","ruby-on-rails","tornado","koa","struts"));
        tagDtos.add(framework);

        TagDto server=new TagDto();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("nginx","linux","docker","apache","ubuntu","centos","unix"));
        tagDtos.add(server);

        TagDto db=new TagDto();
        db.setCategoryName("数据库");
        db.setTags(Arrays.asList("sqlserver","postgresql","sqlite","mysql","h2"));
        tagDtos.add(db);

        TagDto tool=new TagDto();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("github","IDEA","Eclipse","vscode","pycharm","visualC++","jupyter","visual-studio","maven"));
        tagDtos.add(tool);
        return tagDtos;
    }
    public static String filterInvalid(String tags){
        String[] split=StringUtils.split(tags,",");
        List<TagDto>tagDtos=get();
        List<String>tagList=tagDtos.stream().flatMap(tag ->tag.getTags().stream()).collect(Collectors.toList());//二维转换成一维
        String invalid=Arrays.stream(split).filter(t->!tagList.contains(t)).collect(Collectors.joining());
        return invalid;
    }
}
