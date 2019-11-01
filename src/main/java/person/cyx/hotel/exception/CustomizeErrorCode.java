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
    USER_LOCK(2005,"账号已被锁定,请联系管理员！"),
    COMMENT_NOT_FOUND(2006,"你操作的评论不存在了，要不换个试试？"),
    USER_IS_EMPTY(2007,"输入的内容不能为空"),
    USER_NOT_PERMISSION(2008,"你没有权限访问"),
    USER_PERMISSION_REFRESH(2009,"权限刷新，请重新登录"),
    FILE_UPLOAD_FAIL(2010,"图片上传失败"),
    UPDATE_FAIL(2011,"修改失败"),
    PASSWORD_WRONG(2012,"当前密码不正确"),
    PHONE_FOUND(2013,"手机号已存在"),
    USERNAME_FOUND(2014,"用户名已存在"),
    REGISTER_FAIL(2015,"注册失败"),
    DELETE_FAIL(2016,"删除失败"),
    OFFLINE_FAIL(2017,"下线失败"),
    ADD_FAIL(2018,"添加失败"),
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
