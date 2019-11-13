package person.cyx.hotel.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/toMemberList")
    @RequiresPermissions("member:list")
    public String toMemberList(){
        return "customer/memberList";
    }

    @GetMapping("/toOrderList")
    @RequiresPermissions("order:list")
    public String toOrderList(){
        return "customer/orderList";
    }

    @GetMapping("/toOrderCompletedList")
    @RequiresPermissions("completed:list")
    public String toOrderCompletedList(){
        return "customer/orderCompletedList";
    }

    @GetMapping("/toCheckinList")
    @RequiresPermissions("checkin:list")
    public String toCheckinList(){
        return "customer/checkinList";
    }

    @GetMapping("/toCustomerCheckin")
    @RequiresPermissions("customer:checkin")
    public String toCustomerCheckin(@RequestParam(name = "id",required = false) Long id){
        if (id == null){
            return "room/roomCheckin1";
        } else {
            return "room/roomCheckin2";
        }
    }

    /**
     * 查看该顾客入住、预订信息
     * @param roomNumber
     * @param roomStatus
     * @param model
     * @return
     */
    @GetMapping("/viewCustomerRoom")
    @RequiresPermissions({"order:list","checkin:list"})
    public String viewCustomerRoom(@RequestParam("roomNumber") Long roomNumber,
                                   @RequestParam("roomStatus") String roomStatus,
                                   Model model){
        CustomerOrder customerOrder = customerService.viewCustomerRoom(roomNumber, "已"+roomStatus);
        model.addAttribute("customerOrder",customerOrder);
        return "customer/checkinInformation";
    }

    /**
     * 查询顾客是否存在，存在返回顾客信息
     * @param cPhone
     * @return
     */
    @ResponseBody
    @GetMapping("/inquireCPhone")
    @RequiresPermissions("customer:checkin")
    public ResultDTO inquireCPhone(@RequestParam("cPhone") String cPhone){
        if (StringUtils.isBlank(cPhone)){
            return ResultDTO.errorOf(CustomizeErrorCode.USER_IS_EMPTY);
        }
        Customer customer = customerService.checkByPhone(cPhone);
        if (customer != null) {
            return ResultDTO.okOf(customer);
        }
        return ResultDTO.errorOf(CustomizeErrorCode.CUSTOMER_NOT_FOUND);
    }

    /**
     * 顾客退房
     * @param id
     * @param roomNumber
     * @return
     */
    @ResponseBody
    @GetMapping("/customerCheckout")
    @RequiresPermissions("customer:checkout")
    public ResultDTO customerCheckout(@RequestParam("id") Long id,
                                      @RequestParam("roomNumber") Long roomNumber){
        int checkOut = customerService.customerCheckout(id, roomNumber);
        if (checkOut >= 2){
            return ResultDTO.okOf();
        }
        return ResultDTO.errorOf(CustomizeErrorCode.CHECKOUT_FAIL);
    }

    /**
     * 顾客入住房间
     * @param customerOrder
     * @return
     */
    @ResponseBody
    @PostMapping("/customerCheckin")
    @RequiresPermissions("customer:checkin")
    public ResultDTO customerCheckin(@RequestBody CustomerOrder customerOrder){
        int checkIn = customerService.customerCheckin(customerOrder);
        if (checkIn >= 2){
            return ResultDTO.okOf();
        }
        return ResultDTO.errorOf(CustomizeErrorCode.CHECKIN_FAIL);
    }

    /**
     * 顾客入住信息修改
     * @param customerOrder
     * @return
     */
    @ResponseBody
    @PostMapping("/changeCustomerCheckin")
    @RequiresPermissions("customer:checkin")
    public ResultDTO changeCustomerCheckin(@RequestBody CustomerOrder customerOrder){
        int checkIn = customerService.changeCustomerCheckin(customerOrder);
        if (checkIn >= 1){
            return ResultDTO.okOf();
        }
        return ResultDTO.errorOf(CustomizeErrorCode.UPDATE_FAIL);
    }

    /**
     * 顾客退订房间
     * @param id
     * @param roomNumber
     * @return
     */
    @ResponseBody
    @GetMapping("/customerUnsubscribe")
    @RequiresPermissions("customer:unsubscribe")
    public ResultDTO customerUnsubscribe(@RequestParam("id") Long id,
                                         @RequestParam("roomNumber") Long roomNumber){
        int unsubscribe = customerService.customerUnsubscribe(id, roomNumber);
        if (unsubscribe >= 2){
            return ResultDTO.okOf();
        }
        return ResultDTO.errorOf(CustomizeErrorCode.UNSUBSCRIBE_FAIL);
    }

    /**
     * 完成列表
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("/orderCompletedList")
    @RequiresPermissions("completed:list")
    public LayuiResult<CustomerOrder> orderCompletedList(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                               @RequestParam(value = "limit", defaultValue = "10")Integer limit){
        LayuiResult<CustomerOrder> customerOrderLayuiResult = customerService.orderCompletedList(page, limit);
        return customerOrderLayuiResult;
    }

    /**
     * 预订列表
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("/orderList")
    @RequiresPermissions("order:list")
    public LayuiResult<CustomerOrder> roomList(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                         @RequestParam(value = "limit", defaultValue = "10")Integer limit){
        LayuiResult<CustomerOrder> customerOrderLayuiResult = customerService.orderList(page, limit);
        return customerOrderLayuiResult;
    }

    /**
     * 会员列表
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("/memberList")
    @RequiresPermissions("member:list")
    public LayuiResult<Customer> memberList(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                               @RequestParam(value = "limit", defaultValue = "10")Integer limit){
        LayuiResult<Customer> customerLayuiResult = customerService.memberList(page, limit);
        return customerLayuiResult;
    }

    /**
     * 入住列表
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("/checkinList")
    @RequiresPermissions("checkin:list")
    public LayuiResult<CustomerOrder> checkinList(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                               @RequestParam(value = "limit", defaultValue = "10")Integer limit){
        LayuiResult<CustomerOrder> customerOrderLayuiResult = customerService.checkinList(page, limit);
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
    @RequiresPermissions("order:list")
    public LayuiResult<CustomerOrder> sortOrderList(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                               @RequestParam(value = "limit", defaultValue = "10")Integer limit,
                                               @RequestParam("field") String field,
                                               @RequestParam("order") String order){

        String hump = ToolUtil.humpToLine2(field);
        String orderBy = hump+" "+order;
        LayuiResult<CustomerOrder> customerOrderLayuiResult = customerService.orderList(page, limit, orderBy);
        return customerOrderLayuiResult;
    }

    /**
     * 会员列表排序
     * @param page
     * @param limit
     * @param field
     * @param order
     * @return
     */
    @ResponseBody
    @GetMapping("/sortMemberList")
    @RequiresPermissions("member:list")
    public LayuiResult<Customer> sortMemberList(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                                    @RequestParam(value = "limit", defaultValue = "10")Integer limit,
                                                    @RequestParam("field") String field,
                                                    @RequestParam("order") String order){

        String hump = ToolUtil.humpToLine2(field);
        String orderBy = hump+" "+order;
        LayuiResult<Customer> customerLayuiResult = customerService.memberList(page, limit, orderBy);
        return customerLayuiResult;
    }

    /**
     * 完成列表排序
     * @param page
     * @param limit
     * @param field
     * @param order
     * @return
     */
    @ResponseBody
    @GetMapping("/sortOrderCompletedList")
    @RequiresPermissions("completed:list")
    public LayuiResult<CustomerOrder> sortOrderCompletedList(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                               @RequestParam(value = "limit", defaultValue = "10")Integer limit,
                                               @RequestParam("field") String field,
                                               @RequestParam("order") String order){

        String hump = ToolUtil.humpToLine2(field);
        String orderBy = hump+" "+order;
        LayuiResult<CustomerOrder> customerOrderLayuiResult = customerService.orderCompletedList(page, limit, orderBy);
        return customerOrderLayuiResult;
    }

    /**
     * 入住列表排序
     * @param page
     * @param limit
     * @param field
     * @param order
     * @return
     */
    @ResponseBody
    @GetMapping("/sortCheckinList")
    @RequiresPermissions("checkin:list")
    public LayuiResult<CustomerOrder> sortCheckinList(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                                             @RequestParam(value = "limit", defaultValue = "10")Integer limit,
                                                             @RequestParam("field") String field,
                                                             @RequestParam("order") String order){

        String hump = ToolUtil.humpToLine2(field);
        String orderBy = hump+" "+order;
        LayuiResult<CustomerOrder> customerOrderLayuiResult = customerService.checkinList(page, limit, orderBy);
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
    @RequiresPermissions("order:list")
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

    @ResponseBody
    @GetMapping("/queryCustomer")
    @RequiresPermissions("member:list")
    public LayuiResult<Customer> queryCustomer(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                                 @RequestParam(value = "limit", defaultValue = "10")Integer limit,
                                                 @RequestParam("cName") String cName,
                                                 @RequestParam("cPhone") String cPhone){

        Customer customer = new Customer();
        customer.setcName(cName);
        customer.setcPhone(cPhone);

        LayuiResult<Customer> customerLayuiResult = customerService.queryCustomer(page, limit, customer);
        return customerLayuiResult;
    }

    /**
     * 模糊查询完成的订单
     * @param page
     * @param limit
     * @param roomNumber
     * @param name
     * @param state
     * @return
     */
    @ResponseBody
    @GetMapping("/queryOrderCompleted")
    @RequiresPermissions("completed:list")
    public LayuiResult<CustomerOrder> queryOrderCompleted(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                          @RequestParam(value = "limit", defaultValue = "10")Integer limit,
                                          @RequestParam("roomNumber") Long roomNumber,
                                          @RequestParam("name") String name,
                                          @RequestParam("state") String state){

        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setRoomNumber(roomNumber);
        customerOrder.setName(name);
        customerOrder.setState(state);

        LayuiResult<CustomerOrder> customerOrderLayuiResult = customerService.queryOrderCompleted(page, limit, customerOrder);
        return customerOrderLayuiResult;
    }

    /**
     * 模糊查询入住的订单
     * @param page
     * @param limit
     * @param roomNumber
     * @param name
     * @return
     */
    @ResponseBody
    @GetMapping("/queryOrderCheckin")
    @RequiresPermissions("checkin:list")
    public LayuiResult<CustomerOrder> queryOrderCheckin(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                                 @RequestParam(value = "limit", defaultValue = "10")Integer limit,
                                                 @RequestParam("roomNumber") Long roomNumber,
                                                 @RequestParam("name") String name){

        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setRoomNumber(roomNumber);
        customerOrder.setName(name);

        LayuiResult<CustomerOrder> customerOrderLayuiResult = customerService.queryOrderCheckin(page, limit, customerOrder);
        return customerOrderLayuiResult;
    }

    /**
     * 编辑会员
     * @param cId
     * @param cMember
     * @return
     */
    @ResponseBody
    @GetMapping("/customerMember")
    @RequiresPermissions("member:edit")
    public ResultDTO customerMember(@RequestParam("cId") Long cId,
                                    @RequestParam("cMember") Integer cMember){
        int edit = customerService.customerMember(cId, cMember);
        if (edit >= 1){
            return ResultDTO.okOf();
        }
        return ResultDTO.errorOf(CustomizeErrorCode.UPDATE_FAIL);
    }

    /**
     * 删除会员
     * @param cId
     * @return
     */
    @ResponseBody
    @GetMapping("/delCustomer")
    @RequiresPermissions("member:del")
    public ResultDTO delCustomer(@RequestParam("cId") Long cId){
        int del = customerService.delCustomer(cId);
        if (del >= 1){
            return ResultDTO.okOf();
        }
        return ResultDTO.errorOf(CustomizeErrorCode.DELETE_FAIL);
    }

    /**
     * 删除订单
     * @param id
     * @return
     */
    @ResponseBody
    @GetMapping("/delOrder")
    @RequiresPermissions("order:del")
    public ResultDTO delOrder(@RequestParam("id") Long id){
        int del = customerService.delOrder(id);
        if (del >= 1){
            return ResultDTO.okOf();
        }
        return ResultDTO.errorOf(CustomizeErrorCode.DELETE_FAIL);
    }

    /**
     * 批量删除订单
     * @param ids
     * @return
     */
    @ResponseBody
    @PostMapping("/batchDel")
    @RequiresPermissions("order:del")
    public ResultDTO batchDel(Long[] ids){
        if (ids==null){
            return ResultDTO.errorOf(CustomizeErrorCode.USER_IS_EMPTY);
        }
        int del = customerService.batchDel(ids);
        if (del >= ids.length){
            return ResultDTO.okOf();
        }
        return ResultDTO.errorOf(CustomizeErrorCode.DELETE_FAIL);
    }
}
