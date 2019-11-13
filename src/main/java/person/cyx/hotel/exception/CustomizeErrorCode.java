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
    ORDER_NOT_FOUND(2006,"你操作的订单不存在了，要不换个试试？"),
    USER_IS_EMPTY(2007,"输入的内容不能为空"),
    USER_NOT_PERMISSION(2008,"你没有权限访问"),
    USER_PERMISSION_FAIL(2009,"权限更改失败"),
    FILE_UPLOAD_FAIL(2010,"图片上传失败"),
    UPDATE_FAIL(2011,"修改失败"),
    PASSWORD_WRONG(2012,"当前密码不正确"),
    PHONE_FOUND(2013,"手机号已存在"),
    USERNAME_FOUND(2014,"用户名已存在"),
    REGISTER_FAIL(2015,"注册失败"),
    DELETE_FAIL(2016,"删除失败"),
    OFFLINE_FAIL(2017,"下线失败"),
    ADD_FAIL(2018,"添加失败"),
    ROOM_NUMBER_FOUND(2019,"房间号已存在"),
    CUSTOMER_NOT_FOUND(2020,"没有该顾客"),
    CHECKIN_FAIL(2021,"入住失败"),
    UNSUBSCRIBE_FAIL(2022,"退订失败"),
    CHECKOUT_FAIL(2023,"退房失败"),
    CHOOSE_FILE_UPLOAD(2024,"请选择图片上传"),
    PHONE_PASSWORD_WRONG(2025,"手机号或密码不正确"),
    ROOM_ALREADY_BOOKED(2026,"该房间已被预订"),
    BOOKED_FAIL(2027,"预订失败"),
    USER_ROLE_FAIL(2028,"角色更改失败"),
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
