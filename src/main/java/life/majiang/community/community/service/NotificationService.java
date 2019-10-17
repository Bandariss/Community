package life.majiang.community.community.service;

import life.majiang.community.community.dto.NotificationDto;
import life.majiang.community.community.dto.PaginationDto;
import life.majiang.community.community.enums.NotificationStatusEnum;
import life.majiang.community.community.enums.NotificationTypeEnum;
import life.majiang.community.community.exception.CustomizeErrorCode;
import life.majiang.community.community.exception.CustomizeException;
import life.majiang.community.community.mapper.NotificationMapper;
import life.majiang.community.community.mapper.QuestionMapper;
import life.majiang.community.community.mapper.UserMapper;
import life.majiang.community.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;

    public PaginationDto list(Long userId, Integer page, Integer size) {
        PaginationDto<NotificationDto> paginationDto=new PaginationDto<>();
        Integer totalPage;
        NotificationExample notificationExample=new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        Integer totalCount=(int)notificationMapper.countByExample(notificationExample);

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
        //size*(page-1)
        Integer offset=size*(page-1);
        NotificationExample example=new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        List<Notification> notifications=notificationMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));

        if(notifications.size()==0){
            return paginationDto;
        }

        List<NotificationDto>notificationDtos=new ArrayList<>();

        for(Notification notification:notifications){
            NotificationDto notificationDto=new NotificationDto();
            BeanUtils.copyProperties(notification,notificationDto);
            notificationDto.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDtos.add(notificationDto);
        }
        paginationDto.setData(notificationDtos);
        return paginationDto;
    }

    public Long unreadCount(Long userId) {
        NotificationExample notificationExample=new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId)
        .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    public NotificationDto read(Long id, User user) {
        Notification notification=notificationMapper.selectByPrimaryKey(id);
        //验证
        if(notification==null){
           throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(!Objects.equals(notification.getReceiver(),user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

        //标记已读
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDto notificationDto=new NotificationDto();
        BeanUtils.copyProperties(notification,notificationDto);
        notificationDto.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDto;
    }
}
