package person.cyx.hotel.mapper;

import org.apache.ibatis.annotations.Param;
import person.cyx.hotel.model.Customer;

import java.util.List;

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

    int countByCustomer();

    List<Customer> memberList();

    List<Customer> queryCustomer(Customer customer);

    int updateMember(@Param("cId") Long cId, @Param("cMember") Integer cMember);
}