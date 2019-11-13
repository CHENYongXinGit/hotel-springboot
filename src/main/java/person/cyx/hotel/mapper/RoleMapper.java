package person.cyx.hotel.mapper;

import org.apache.ibatis.annotations.Param;

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

    List<Long> getRoleIdByUserId(Long userId);

    int delAdminRole(@Param("adminId") Long id);

    int insertAdminRole(@Param("adminId") Long id, @Param("role") Long role);
}
