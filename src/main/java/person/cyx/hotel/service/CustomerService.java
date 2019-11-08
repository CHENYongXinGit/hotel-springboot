package person.cyx.hotel.service;

import person.cyx.hotel.dto.LayuiResult;
import person.cyx.hotel.model.Customer;
import person.cyx.hotel.model.CustomerOrder;

import java.util.List;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-11-04 20:38
 **/
public interface CustomerService {

    Customer checkCName(String cName);

    int customerCheckin(CustomerOrder customerOrder);

    LayuiResult<CustomerOrder> orderList(Integer page, Integer limit);

    LayuiResult<CustomerOrder> orderList(Integer page, Integer limit, String orderBy);

    LayuiResult<CustomerOrder> queryOrder(Integer page, Integer limit, CustomerOrder customerOrder);

    int customerUnsubscribe(Long id, Long roomNumber);

    int customerCheckout(Long id, Long roomNumber);

    CustomerOrder viewCustomerRoom(Long roomNumber, String state);

    int changeCustomerCheckin(CustomerOrder customerOrder);

    LayuiResult<CustomerOrder> orderCompletedList(Integer page, Integer limit);

    LayuiResult<CustomerOrder> orderCompletedList(Integer page, Integer limit, String orderBy);

    LayuiResult<CustomerOrder> queryOrderCompleted(Integer page, Integer limit, CustomerOrder customerOrder);

    int delOrder(Long id);

    int batchDel(Long[] ids);

    Customer checkByPhone(String phone);

    int insert(Customer customer);

    Customer login(String phone, String password);

    List<CustomerOrder> selectByCPhone(String phone);

    int customerBooking(Customer customer, CustomerOrder customerOrder);
}
