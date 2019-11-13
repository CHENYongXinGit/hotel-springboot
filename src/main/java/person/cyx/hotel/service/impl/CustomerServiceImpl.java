package person.cyx.hotel.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import person.cyx.hotel.dto.LayuiResult;
import person.cyx.hotel.dto.TotalDaysDTO;
import person.cyx.hotel.dto.ViewDTO;
import person.cyx.hotel.exception.CustomizeErrorCode;
import person.cyx.hotel.exception.CustomizeException;
import person.cyx.hotel.mapper.CustomerMapper;
import person.cyx.hotel.mapper.CustomerOrderMapper;
import person.cyx.hotel.mapper.RoomMapper;
import person.cyx.hotel.model.Customer;
import person.cyx.hotel.model.CustomerOrder;
import person.cyx.hotel.service.CustomerService;

import java.text.SimpleDateFormat;
import java.util.*;

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
    private LayuiResult<Customer> customerLayuiResult = new LayuiResult();

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
    public LayuiResult<Customer> memberList(Integer page, Integer limit) {
        PageHelper.startPage(page, limit, "c_created desc");
        List<Customer> customers = customerMapper.memberList();
        return getCustomerLayuiResult(customers);
    }

    @Override
    public LayuiResult<Customer> memberList(Integer page, Integer limit, String orderBy) {
        PageHelper.startPage(page, limit, orderBy);
        List<Customer> customers = customerMapper.memberList();
        return getCustomerLayuiResult(customers);
    }

    @Override
    public LayuiResult<Customer> queryCustomer(Integer page, Integer limit, Customer customer) {
        PageHelper.startPage(page, limit, "c_created desc");
        List<Customer> customers = customerMapper.queryCustomer(customer);
        return getCustomerLayuiResult(customers);
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
    public LayuiResult<CustomerOrder> checkinList(Integer page, Integer limit) {
        PageHelper.startPage(page, limit, "room_number asc");
        List<CustomerOrder> customerOrders = customerOrderMapper.checkinList();
        return getCustomerOrderLayuiResult(customerOrders);
    }

    @Override
    public LayuiResult<CustomerOrder> checkinList(Integer page, Integer limit, String orderBy) {
        PageHelper.startPage(page, limit, orderBy);
        List<CustomerOrder> customerOrders = customerOrderMapper.checkinList();
        return getCustomerOrderLayuiResult(customerOrders);
    }

    @Override
    public LayuiResult<CustomerOrder> queryOrderCheckin(Integer page, Integer limit, CustomerOrder customerOrder) {
        PageHelper.startPage(page, limit, "room_number asc");
        List<CustomerOrder> customerOrders = customerOrderMapper.queryOrderCheckin(customerOrder);
        return getCustomerOrderLayuiResult(customerOrders);
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
    public int delCustomer(Long cId) {
        if (cId == null || cId == 0) {
            throw new CustomizeException(CustomizeErrorCode.ORDER_NOT_FOUND);
        }
        return customerMapper.deleteByPrimaryKey(cId);
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

    @Override
    public Customer checkByPhone(String phone) {
        return customerMapper.checkByPhone(phone);
    }

    @Override
    public int insert(Customer customer) {
        customer.setcMember(0);
        customer.setcCreated(System.currentTimeMillis());
        customer.setcUpdated(System.currentTimeMillis());
        return customerMapper.insertSelective(customer);
    }

    @Override
    public Customer login(String phone, String password) {
        return customerMapper.login(phone, password);
    }

    @Override
    public List<CustomerOrder> selectByCPhone(String phone) {
        return customerOrderMapper.selectByCPhone(phone);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int customerBooking(Customer customer, CustomerOrder customerOrder) {
        customerOrder.setName(customer.getcName());
        customerOrder.setSex(customer.getcSex());
        customerOrder.setMember(customer.getcMember());
        customerOrder.setIdentity(customer.getcIdentity());
        customerOrder.setPhone(customer.getcPhone());
        customerOrder.setState("已预订");
        int updateRoomStatus = roomMapper.updateRoomStatus("预订", customerOrder.getRoomNumber());
        int insert = customerOrderMapper.insertSelective(customerOrder);
        return insert + updateRoomStatus;
    }

    @Override
    public int countByCustomer() {
        return customerMapper.countByCustomer();
    }

    @Override
    public List<Double> selectByPrice() {
        List<Double> list = new ArrayList<>();
        List<Double> countPrice = customerOrderMapper.countPrice();
        Double size = Double.valueOf(countPrice.size());
        list.add(size);
        double sum = countPrice.stream().mapToDouble(Double::doubleValue).sum();
        list.add(sum);
        return list;
    }

    @Override
    public Double countPriceByStartTime(String s) {
        return customerOrderMapper.countPriceByStartTime(s);
    }

    @Override
    public TotalDaysDTO dayPriceByStartTime() {
        List<String> names = new ArrayList<>();
        List<Integer> orders = new ArrayList<>();
        List<Double> prices = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        c.setTime(date);
        for (int i = 0; i <= 6; i++) {
            ViewDTO viewDTO = customerOrderMapper.dayPriceByStartTime(sdf.format(c.getTime()));
            names.add(sdf.format(c.getTime()));
            orders.add(viewDTO.getOrders());
            prices.add(viewDTO.getTotalPrice());
            c.add(Calendar.DATE, -1);
        }
        Collections.reverse(names);
        Collections.reverse(orders);
        Collections.reverse(prices);
        TotalDaysDTO totalDaysDTO = new TotalDaysDTO();
        totalDaysDTO.setNames(names);
        totalDaysDTO.setOrders(orders);
        totalDaysDTO.setPrices(prices);
        return totalDaysDTO;
    }

    @Override
    public int customerMember(Long cId, Integer cMember) {
        if (cMember == 0){
            return customerMapper.updateMember(cId, 1);
        } else {
            return customerMapper.updateMember(cId, 0);
        }
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
        if (id == null || id == 0 || roomNumber == null || roomNumber == 0) {
            throw new CustomizeException(CustomizeErrorCode.ORDER_NOT_FOUND);
        }
        int updateRoomStatus = roomMapper.updateRoomStatus("空闲", roomNumber);
        Date dt = new Date();
        //最后的aa表示“上午”或“下午”    HH表示24小时制    如果换成hh表示12小时制
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endTime = sdf.format(dt);
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setId(id);
        customerOrder.setState(state);
        customerOrder.setEndTime(endTime);
        int update = customerOrderMapper.updateByPrimaryKeySelective(customerOrder);
        return update + updateRoomStatus;
    }

    /**
     * 订单分页返回数据
     * @param customerOrders
     * @return
     */
    private LayuiResult<CustomerOrder> getCustomerOrderLayuiResult(List<CustomerOrder> customerOrders) {
        PageInfo pageInfo = new PageInfo(customerOrders, 5);
        customerOrderLayuiResult.setData(customerOrders);
        customerOrderLayuiResult.setCount(pageInfo.getTotal());
        return customerOrderLayuiResult;
    }

    /**
     * 会员分页返回数据
     * @param customers
     * @return
     */
    private LayuiResult<Customer> getCustomerLayuiResult(List<Customer> customers) {
        PageInfo pageInfo = new PageInfo(customers, 5);
        customerLayuiResult.setData(customers);
        customerLayuiResult.setCount(pageInfo.getTotal());
        return customerLayuiResult;
    }
}
