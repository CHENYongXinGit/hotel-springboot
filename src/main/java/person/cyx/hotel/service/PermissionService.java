package person.cyx.hotel.service;

import java.util.List;
import java.util.Map;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-11-10 14:15
 **/
public interface PermissionService {

    List<Long> getPermissionIdByRoleId(Long roleId);

    int updatePermission(Map<String,Long> permissions);

    List<Long> getRoleIdByUserId(Long userId);

    int updateRole(Map<String, Long> roles);
}

