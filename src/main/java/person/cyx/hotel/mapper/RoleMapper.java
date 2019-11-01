package person.cyx.hotel.mapper;

import java.util.List;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-10-24 16:11
 **/
public interface RoleMapper {

    List<String> getAllRoleSn();

    List<String> getRoleSnByUserId(Long adminId);
}
