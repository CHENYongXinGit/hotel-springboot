package person.cyx.hotel.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-10-24 16:21
 **/
public interface PermissionMapper {

    List<String> getPermissionResourcesByUserId(Long adminId);

    List<String> getAllResources();

    List<Long> getPermissionIdByRoleId(Long roleId);

    int delRolePermission(@Param("roleId") Long roleId);

    int insertRolePermission(@Param("roleId") Long roleId, @Param("permission") Long permission);

}
