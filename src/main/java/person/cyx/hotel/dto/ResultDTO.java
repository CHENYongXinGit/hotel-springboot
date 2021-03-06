package person.cyx.hotel.dto;

import lombok.Data;
import person.cyx.hotel.exception.CustomizeErrorCode;
import person.cyx.hotel.exception.CustomizeException;

/**
 * @program: community
 * @description
 * @author: chenyongxin
 * @create: 2019-10-18 13:54
 **/
@Data
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T data;

    public static ResultDTO errorOf(Integer code, String message){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {
        return errorOf(errorCode.getCode(),errorCode.getMessage());
    }

    public static ResultDTO errorOf(CustomizeException ex) {
        return errorOf(ex.getCode(),ex.getMessage());
    }

    public static <T> ResultDTO errorOf(T t){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(2000);
        resultDTO.setMessage("登录失败");
        resultDTO.setData(t);
        return resultDTO;
    }

    public static ResultDTO okOf(){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }

    public static <T>  ResultDTO okOf(T t){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(t);
        return resultDTO;
    }
}
