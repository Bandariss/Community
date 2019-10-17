package life.majiang.community.community.dto;

import life.majiang.community.community.exception.CustomizeErrorCode;
import life.majiang.community.community.exception.CustomizeException;

public class ResultDto <T>{
    //泛型类
    private Integer code;
    private String message;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ResultDto errorOf(CustomizeException e) {
        return errorOf(e.getCode(),e.getMessage());
    }

    public static ResultDto errorOf(Integer code,String message){
        ResultDto resultDto=new ResultDto();
        resultDto.setCode(code);
        resultDto.setMessage(message);
        return resultDto;
    }
    public static ResultDto errorOf(CustomizeErrorCode errorCode) {
        return errorOf(errorCode.getCode(),errorCode.getMessage());
    }
    public static ResultDto okOf(){
        ResultDto resultDto=new ResultDto();
        resultDto.setCode(200);
        resultDto.setMessage("请求成功");
        return resultDto;
    }
    public static <T> ResultDto okOf(T t){
        //静态方法无法访问类上定义的泛型；如果静态方法操作的引用数据类型不确定的时候，必须要将泛型定义在方法上。
        //
        //即：如果静态方法要使用泛型的话，必须将静态方法也定义成泛型方法
        ResultDto resultDto=new ResultDto();
        resultDto.setCode(200);
        resultDto.setMessage("请求成功");
        resultDto.setData(t);
        return resultDto;
    }
}
