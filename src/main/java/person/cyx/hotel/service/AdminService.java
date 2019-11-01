package person.cyx.hotel.service;

import person.cyx.hotel.dto.LoginRedisDTO;
import person.cyx.hotel.model.Admin;

import java.util.List;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-10-18 10:14
 **/
public interface AdminService {

    Admin findAdminByUsername(String username);

    int updateById(Admin admin);

    LoginRedisDTO loginUserLock(String username);

    LoginRedisDTO loginValdate(String username);

    Admin checkByPhone(String phone);

    int insertSelective(Admin admin);

    Admin login(String username, String password);

    List<Admin> list();

    List<Admin> fuzzyQueryUsername(String username);

    List<Admin> fuzzyQueryPhone(String phone);

    int delete(Long id);
}
