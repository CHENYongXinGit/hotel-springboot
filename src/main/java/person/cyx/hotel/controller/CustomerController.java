package person.cyx.hotel.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import person.cyx.hotel.dto.LayuiResult;
import person.cyx.hotel.dto.ResultDTO;
import person.cyx.hotel.exception.CustomizeErrorCode;
import person.cyx.hotel.model.Customer;
import person.cyx.hotel.model.CustomerOrder;
import person.cyx.hotel.service.impl.CustomerServiceImpl;
import person.cyx.hotel.util.ToolUtil;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-11-04 15:19
 **/
@Controller
@RequestMapping("/Customer")
public class CustomerController {

    @Autowired
    private CustomerServiceImpl customerService;

    @GetMapping("/toOrderList")
    public String toOrderList(){
        return "customer/orderList";
    }

    /**
     * 查询顾客是否存在，存在返回顾客信息
     * @param cName
     * @return
     */
    @ResponseBody
    @GetMapping("/inquireCName")
    public ResultDTO inquireCName(@RequestParam("cName") String cName){
        if (StringUtils.isBlank(cName)){
            return ResultDTO.errorOf(CustomizeErrorCode.USER_IS_EMPTY);
        }
        Customer customer = customerService.checkCName(cName);
        if (customer != null) {
            return ResultDTO.okOf(customer);
        }
        return ResultDTO.errorOf(CustomizeErrorCode.CUSTOMER_NOT_FOUND);
    }

    /**
     * 顾客入住房间
     * @param customerOrder
     * @return
     */
    @ResponseBody
    @PostMapping("/customerCheckin")
    public ResultDTO customerCheckin(@RequestBody CustomerOrder customerOrder){
        int checkIn = customerService.customerCheckin(customerOrder);
        if (checkIn >= 2){
            return ResultDTO.okOf();
        }
        return ResultDTO.errorOf(CustomizeErrorCode.CHECKIN_FAIL);
    }

    /**
     * 预订列表
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("/orderList")
    public LayuiResult<CustomerOrder> roomList(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                         @RequestParam(value = "limit", defaultValue = "10")Integer limit){
        LayuiResult<CustomerOrder> customerOrderLayuiResult = customerService.orderList(page, limit);
        return customerOrderLayuiResult;
    }

    /**
     * 预订列表排序
     * @param page
     * @param limit
     * @param field
     * @param order
     * @return
     */
    @ResponseBody
    @GetMapping("/sortOrderList")
    public LayuiResult<CustomerOrder> sortRoom(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                         @RequestParam(value = "limit", defaultValue = "10")Integer limit,
                                         @RequestParam("field") String field,
                                         @RequestParam("order") String order){

        String hump = ToolUtil.humpToLine2(field);
        String orderBy = hump+" "+order;
        LayuiResult<CustomerOrder> customerOrderLayuiResult = customerService.orderList(page, limit, orderBy);
        return customerOrderLayuiResult;
    }

    /**
     * 模糊查询订单
     * @param page
     * @param limit
     * @param roomNumber
     * @param name
     * @param state
     * @return
     */
    @ResponseBody
    @GetMapping("/queryOrder")
    public LayuiResult<CustomerOrder> queryOrder(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                          @RequestParam(value = "limit", defaultValue = "10")Integer limit,
                                          @RequestParam("roomNumber") Long roomNumber,
                                          @RequestParam("name") String name,
                                          @RequestParam("state") String state){

        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setRoomNumber(roomNumber);
        customerOrder.setName(name);
        customerOrder.setState(state);

        LayuiResult<CustomerOrder> customerOrderLayuiResult = customerService.queryOrder(page, limit, customerOrder);
        return customerOrderLayuiResult;
    }

}
