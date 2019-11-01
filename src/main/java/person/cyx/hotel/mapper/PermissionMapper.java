package person.cyx.hotel.mapper;

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
}
