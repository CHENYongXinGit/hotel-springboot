package person.cyx.hotel.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import person.cyx.hotel.dto.LayuiResult;
import person.cyx.hotel.exception.CustomizeErrorCode;
import person.cyx.hotel.exception.CustomizeException;
import person.cyx.hotel.mapper.CustomerMapper;
import person.cyx.hotel.mapper.CustomerOrderMapper;
import person.cyx.hotel.mapper.RoomMapper;
import person.cyx.hotel.model.Customer;
import person.cyx.hotel.model.CustomerOrder;
import person.cyx.hotel.service.CustomerService;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        int updateRoomStatus = roomMapper.updateRoomStatus("入住",customerOrder.getRoomNumber());
        if (customerOrder.getId()==null){
            int insert = customerOrderMapper.insertSelective(customerOrder);
            return insert+updateRoomStatus;
        } else {
            int update = customerOrderMapper.updateByPrimaryKeySelective(customerOrder);
            if (update == 0) {
                throw new CustomizeException(CustomizeErrorCode.ORDER_NOT_FOUND);
            }
            return update+updateRoomStatus;
        }
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

    @Override
    public int customerUnsubscribe(Long id, Long roomNumber) {
        String state = "已退订";
        return updateCustomerOperation(id, roomNumber, state);
    }

    @Override
    public int customerCheckout(Long id, Long roomNumber) {
        String state = "已完成";
        return updateCustomerOperation(id, roomNumber, state);
    }

    @Override
    public CustomerOrder viewCustomerRoom(Long roomNumber, String state) {
        CustomerOrder customerOrder = customerOrderMapper.viewCustomerRoom(roomNumber, state);
        return customerOrder;
    }

    @Override
    public int changeCustomerCheckin(CustomerOrder customerOrder) {
        if (customerOrder.getId() == null || customerOrder.getId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.ORDER_NOT_FOUND);
        }
        int update = customerOrderMapper.updateByPrimaryKeySelective(customerOrder);
        return update;
    }

    @Override
    public LayuiResult<CustomerOrder> orderCompletedList(Integer page, Integer limit) {
        PageHelper.startPage(page, limit, "end_time desc");
        List<CustomerOrder> customerOrders = customerOrderMapper.orderCompletedList();
        return getCustomerOrderLayuiResult(customerOrders);
    }

    @Override
    public LayuiResult<CustomerOrder> orderCompletedList(Integer page, Integer limit, String orderBy) {
        PageHelper.startPage(page, limit, orderBy);
        List<CustomerOrder> customerOrders = customerOrderMapper.orderCompletedList();
        return getCustomerOrderLayuiResult(customerOrders);
    }

    @Override
    public LayuiResult<CustomerOrder> queryOrderCompleted(Integer page, Integer limit, CustomerOrder customerOrder) {
        PageHelper.startPage(page, limit, "end_time desc");
        List<CustomerOrder> customerOrders = customerOrderMapper.queryOrderCompleted(customerOrder);
        return getCustomerOrderLayuiResult(customerOrders);
    }

    @Override
    public int delOrder(Long id) {
        if (id == null || id == 0) {
            throw new CustomizeException(CustomizeErrorCode.ORDER_NOT_FOUND);
        }
        return customerOrderMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDel(Long[] ids) {
        int del = 0;
        for (Long id : ids) {
            del += customerOrderMapper.deleteByPrimaryKey(id);
        }
        return del;
    }

    /**
     * 顾客退订、退房操作
     * @param id
     * @param roomNumber
     * @param state
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateCustomerOperation(Long id, Long roomNumber, String state) {
        int updateRoomStatus = roomMapper.updateRoomStatus("空闲", roomNumber);
        if (id == null || id == 0) {
            throw new CustomizeException(CustomizeErrorCode.ORDER_NOT_FOUND);
        } else {
            Date dt = new Date();
            //最后的aa表示“上午”或“下午”    HH表示24小时制    如果换成hh表示12小时制
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String endTime = sdf.format(dt);
            CustomerOrder customerOrder = new CustomerOrder();
            customerOrder.setId(id);
            customerOrder.setState(state);
            customerOrder.setEndTime(endTime);
            int update = customerOrderMapper.updateByPrimaryKeySelective(customerOrder);
            if (update == 0) {
                throw new CustomizeException(CustomizeErrorCode.ORDER_NOT_FOUND);
            }
            return update + updateRoomStatus;
        }
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
