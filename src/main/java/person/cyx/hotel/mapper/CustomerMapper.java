package person.cyx.hotel.mapper;

import org.apache.ibatis.annotations.Param;
import person.cyx.hotel.model.Customer;

public interface CustomerMapper {
    int deleteByPrimaryKey(Long cId);

    int insert(Customer record);

    int insertSelective(Customer record);

    Customer selectByPrimaryKey(Long cId);

    int updateByPrimaryKeySelective(Customer record);

    int updateByPrimaryKey(Customer record);

    Customer checkCName(@Param("cName") String cName);

    Customer checkByPhone(@Param("cPhone") String phone);

    Customer login(@Param("cPhone") String phone, @Param("cPassword") String password);
}