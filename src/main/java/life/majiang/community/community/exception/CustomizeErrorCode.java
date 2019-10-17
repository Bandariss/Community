package life.majiang.community.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{
    QUESTION_NOT_FOUND(2001,"你的问题不在了"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或评论进行回复"),
    NO_LOGIN(2003,"当前操作需要登陆，请登陆后重试"),
    SYSTEM_ERROR(2004,"服务冒烟了，请稍后"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"你找的评论不在了"),
    CONTENT_IS_EMPTY(2007,"输入内容不能为空"),
    READ_NOTIFICATION_FAIL(2008,"兄弟，你点击的是别人的信息"),
    NOTIFICATION_NOT_FOUND(2008,"消息不见了");





    @Override
    public String getMessage(){ return message;}
    @Override
    public Integer getCode(){
        return code;
    }

    private String message;
    private Integer code;

    CustomizeErrorCode(Integer code,String message) {
        this.message = message;
        this.code = code;
    }

}
