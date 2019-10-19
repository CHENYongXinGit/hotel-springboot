package person.cyx.hotel.dto;

import lombok.Data;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-10-18 20:24
 **/
@Data
public class LoginRedisDTO {

    /**
     * 用户名称
     */
    private String name;
    /**
     * 是否被锁
     */
    private Boolean flag;
    /**
     * 锁定时间
     */
    private String loginLockTime;
    /**
     * 登录错误失效时间
     */
    private String loginInvalidTime;
    /**
     * 还允许登录输入错误次数
     */
    private Long allowNum;
}
