package life.majiang.community.community.service;

import life.majiang.community.community.dto.PaginationDto;
import life.majiang.community.community.dto.QuestionDto;
import life.majiang.community.community.mapper.QuestionMapper;
import life.majiang.community.community.mapper.UserMapper;
import life.majiang.community.community.module.Question;
import life.majiang.community.community.module.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public PaginationDto list(Integer page, Integer size) {
        Integer offset=size*(page-1);
        List<Question>questions=questionMapper.list(offset,size);
        List<QuestionDto>questionDtoList=new ArrayList<>();
        PaginationDto paginationDto=new PaginationDto();
        for(Question question:questions){
            User user=userMapper.findById(question.getCreator());
            QuestionDto questionDto=new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);//快速将question中的属性拷贝到questionDto
            questionDto.setUser(user);//此时question中包含了User对象
            questionDtoList.add(questionDto);
        }
        paginationDto.setQuestions(questionDtoList);
        Integer totalCount=questionMapper.count();

        paginationDto.setPagination(totalCount,page,size);
        System.out.println(totalCount);
        return paginationDto;
    }
}
