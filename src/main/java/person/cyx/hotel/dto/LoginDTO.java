package person.cyx.hotel.dto;

import lombok.Data;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-10-18 15:59
 **/
@Data
public class LoginDTO {
    private String username;
    private String password;
    private String rememberMe;
}
