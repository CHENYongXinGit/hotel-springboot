package person.cyx.hotel.service;

import person.cyx.hotel.dto.LayuiResult;
import person.cyx.hotel.model.Customer;
import person.cyx.hotel.model.CustomerOrder;

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
}
