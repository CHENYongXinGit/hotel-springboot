package person.cyx.hotel.mapper;

import org.apache.ibatis.annotations.Param;
import person.cyx.hotel.model.Admin;

import java.util.List;

public interface AdminMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Admin record);

    int insertSelective(Admin record);

    Admin selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);

    Admin findAdminByUsername(@Param("username") String username);

    Admin checkByPhone(@Param("phone") String phone);

    Admin login(@Param("username") String username, @Param("password") String password);

    List<Admin> list();

    List<Admin> fuzzyQueryUsername(@Param("username") String username);

    List<Admin> fuzzyQueryPhone(@Param("phone") String phone);
}