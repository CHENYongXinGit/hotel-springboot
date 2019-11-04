package person.cyx.hotel.mapper;

import person.cyx.hotel.model.CustomerOrder;

import java.util.List;

public interface CustomerOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CustomerOrder record);

    int insertSelective(CustomerOrder record);

    CustomerOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomerOrder record);

    int updateByPrimaryKey(CustomerOrder record);

    List<CustomerOrder> orderList();

    List<CustomerOrder> orderCompletedList();

    List<CustomerOrder> queryOrder(CustomerOrder customerOrder);
}