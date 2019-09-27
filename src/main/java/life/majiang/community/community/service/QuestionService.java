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
        PaginationDto paginationDto=new PaginationDto();

        Integer totalPage;
        Integer totalCount=questionMapper.count();

        if(totalCount % size==0){
            totalPage=totalCount/size;
        }
        else{
            totalPage=totalCount/size+1;
        }

        if(page<1){
            page=1;
        }
        if(page>totalPage){
            page=totalPage;
        }
        if(page<1){
            page=1;
        }
        if(page>totalPage){
            page=totalPage;
        }

        paginationDto.setPagination(totalPage,page);

        Integer offset=size*(page-1);
        if(offset<0){
            offset=0;
        }
        List<Question>questions=questionMapper.list(offset,size);
        List<QuestionDto>questionDtoList=new ArrayList<>();

        for(Question question:questions){
            User user=userMapper.findById(question.getCreator());
            QuestionDto questionDto=new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);//快速将question中的属性拷贝到questionDto
            questionDto.setUser(user);//此时question中包含了User对象
            questionDtoList.add(questionDto);
        }
        paginationDto.setQuestions(questionDtoList);
        return paginationDto;
    }

    public PaginationDto list(Integer userId, Integer page, Integer size) {
        PaginationDto paginationDto=new PaginationDto();
        Integer totalPage;
        Integer totalCount=questionMapper.countByUserId(userId);

        if(totalCount % size==0){
            totalPage=totalCount/size;
        }
        else{
            totalPage=totalCount/size+1;
        }

        if(page<1){
            page=1;
        }
        if(page>totalPage){
            page=totalPage;
        }

        paginationDto.setPagination(totalPage,page);

        Integer offset=size*(page-1);
        List<Question>questions=questionMapper.listByUserId(userId,offset,size);
        List<QuestionDto>questionDtoList=new ArrayList<>();

        for(Question question:questions){
            User user=userMapper.findById(question.getCreator());
            QuestionDto questionDto=new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);//快速将question中的属性拷贝到questionDto
            questionDto.setUser(user);//此时question中包含了User对象
            questionDtoList.add(questionDto);
        }

        paginationDto.setQuestions(questionDtoList);
        return paginationDto;
    }

    public QuestionDto getById(Integer id) {
        //根据问题的id查找
        Question question=questionMapper.getById(id);
        QuestionDto questionDto=new QuestionDto();
        BeanUtils.copyProperties(question,questionDto);
        //将作者信息放入
        User user=userMapper.findById(question.getCreator());
        questionDto.setUser(user);
        return questionDto;
    }

    public void createOrUpdate(Question question) {
        if(question.getId()==null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        }
        else{
            //更新
            question.setGmtModified(question.getGmtCreate());
            questionMapper.update(question);
        }
    }
}
