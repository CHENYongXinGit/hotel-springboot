package person.cyx.hotel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import person.cyx.hotel.mapper.PermissionMapper;
import person.cyx.hotel.mapper.RoleMapper;
import person.cyx.hotel.service.PermissionService;

import java.util.List;
import java.util.Map;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-11-10 14:16
 **/
@Service("permissionServiceImpl")
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Long> getPermissionIdByRoleId(Long roleId) {
        return permissionMapper.getPermissionIdByRoleId(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePermission(Map<String,Long> permissions) {
        Long role = permissions.get("role");
        permissionMapper.delRolePermission(role);
        int insert = 0;
        for (int i = 1; i <= 18; i++) {
            Long permission = permissions.get("permission" + i);
            if (permission != null && permission <= 18){
                insert += permissionMapper.insertRolePermission(role, permission);
            }
        }
        return insert;
    }

    @Override
    public List<Long> getRoleIdByUserId(Long userId) {
        return roleMapper.getRoleIdByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRole(Map<String, Long> roles) {
        Long id = roles.get("id");
        roleMapper.delAdminRole(id);
        int insert = 0;
        for (int i = 1; i <= 4; i++) {
            Long role = roles.get("role" + i);
            if (role != null && role <= 4){
                insert += roleMapper.insertAdminRole(id, role);
            }
        }
        return insert;
    }
}
