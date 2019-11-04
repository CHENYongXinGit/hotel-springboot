package person.cyx.hotel.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import person.cyx.hotel.dto.LayuiResult;
import person.cyx.hotel.mapper.CustomerMapper;
import person.cyx.hotel.mapper.CustomerOrderMapper;
import person.cyx.hotel.mapper.RoomMapper;
import person.cyx.hotel.model.Customer;
import person.cyx.hotel.model.CustomerOrder;
import person.cyx.hotel.service.CustomerService;

import java.util.List;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-11-04 15:29
 **/
@Service("customerServiceImpl")
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerOrderMapper customerOrderMapper;
    @Autowired
    private RoomMapper roomMapper;

    private LayuiResult<CustomerOrder> customerOrderLayuiResult = new LayuiResult();

    @Override
    public Customer checkCName(String cName) {
        return customerMapper.checkCName(cName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int customerCheckin(CustomerOrder customerOrder) {
        customerOrder.setState("已入住");
        int update = roomMapper.updateRoomStatus("入住",customerOrder.getRoomNumber());
        int insert = customerOrderMapper.insertSelective(customerOrder);
        return insert+update;
    }

    @Override
    public LayuiResult<CustomerOrder> orderList(Integer page, Integer limit) {
        PageHelper.startPage(page, limit, "start_time desc");
        List<CustomerOrder> customerOrders = customerOrderMapper.orderList();
        return getCustomerOrderLayuiResult(customerOrders);
    }

    @Override
    public LayuiResult<CustomerOrder> orderList(Integer page, Integer limit, String orderBy) {
        PageHelper.startPage(page, limit, orderBy);
        List<CustomerOrder> customerOrders = customerOrderMapper.orderList();
        return getCustomerOrderLayuiResult(customerOrders);
    }

    @Override
    public LayuiResult<CustomerOrder> queryOrder(Integer page, Integer limit, CustomerOrder customerOrder) {
        PageHelper.startPage(page, limit, "start_time desc");
        List<CustomerOrder> customerOrders = customerOrderMapper.queryOrder(customerOrder);
        return getCustomerOrderLayuiResult(customerOrders);
    }

    /**
     * 分页返回数据
     * @param customerOrders
     * @return
     */
    private LayuiResult<CustomerOrder> getCustomerOrderLayuiResult(List<CustomerOrder> customerOrders) {
        PageInfo pageInfo = new PageInfo(customerOrders, 5);
        customerOrderLayuiResult.setData(customerOrders);
        customerOrderLayuiResult.setCount(pageInfo.getTotal());
        return customerOrderLayuiResult;
    }
}
