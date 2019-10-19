package person.cyx.hotel.exception;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-10-18 13:42
 **/
public enum CustomizeErrorCode implements ICustomizeErrorCode {

    USER_NOT_FOUND(2001,"账户不存在"),
    USER_PARAM_WRONG(2002,"用户未登录"),
    SYS_PARAM_WRONG(2003,"我勒个去，页面被外星人挟持了!"),
    SYS_ERROR(2004,"服务冒烟了，要不然换个试试！！！"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"你操作的评论不存在了，要不换个试试？"),
    USER_IS_EMPTY(2007,"输入的内容不能为空"),
    READ_NOTIFICATION_FAIL(2008,"兄弟你这是读别人的信息呢？"),
    NOTIFICATION_NOT_FOUND(2009,"信息莫非是不翼而飞？"),
    FILE_UPLOAD_FAIL(2010,"图片上传失败"),
    ;

    private Integer code;
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
