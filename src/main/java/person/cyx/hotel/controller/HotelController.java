package person.cyx.hotel.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import person.cyx.hotel.dto.ResultDTO;
import person.cyx.hotel.exception.CustomizeErrorCode;
import person.cyx.hotel.mapper.RoomTypeMapper;
import person.cyx.hotel.model.Customer;
import person.cyx.hotel.model.CustomerOrder;
import person.cyx.hotel.model.Room;
import person.cyx.hotel.model.RoomType;
import person.cyx.hotel.service.impl.CustomerServiceImpl;
import person.cyx.hotel.service.impl.RoomServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-11-05 20:11
 **/
@Controller
@RequestMapping("/hotel")
public class HotelController {

    @Autowired
    private RoomTypeMapper roomTypeMapper;
    @Autowired
    private RoomServiceImpl roomService;
    @Autowired
    private CustomerServiceImpl customerService;

    /**
     * 去主页
     * @return
     */
    @GetMapping("/home")
    public String home(){
        return "hotel/home";
    }

    /**
     * 显示房间类型
     * @param model
     * @return
     */
    @GetMapping("/roomSort")
    public String roomType(Model model){
        List<RoomType> roomTypes = roomTypeMapper.roomTypeList();
        model.addAttribute("roomTypes",roomTypes);
        return "hotel/roomSort";
    }

    /**
     * 房间展示
     * @param page
     * @param limit
     * @param roomType
     * @param model
     * @return
     */
    @GetMapping("/rooms")
    public String rooms(@RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "limit", defaultValue = "6") Integer limit,
                        @RequestParam(value = "roomType", required = false) Long roomType,
                        Model model){
        List<RoomType> roomTypes = roomTypeMapper.roomTypeList();
        model.addAttribute("roomTypes",roomTypes);
        List<Room> rooms;
        PageHelper.startPage(page, limit, "room_number asc");
        if (roomType == null){
            rooms = roomService.roomList();
        } else {
            rooms = roomService.selectByRoomType(roomType);
        }
        PageInfo pageInfo = new PageInfo(rooms, 5);
        model.addAttribute("rooms",rooms);
        model.addAttribute("total",pageInfo.getTotal());
        model.addAttribute("page",page);
        model.addAttribute("limit",limit);
        model.addAttribute("type",roomType);
        return "hotel/rooms";
    }

    /**
     * 房间详情
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/roomsDetail")
    public String roomsDetail(@RequestParam("id") Long id, Model model){
        Room room = roomService.selectByRoomId(id);
        model.addAttribute("room",room);
        return "hotel/roomsDetail";
    }

    /**
     * 关于我们
     * @return
     */
    @GetMapping("/about")
    public String about(){
        return "hotel/about";
    }

    /**
     * 我的位置
     * @return
     */
    @GetMapping("/myPosition")
    public String myPosition(){
        return "hotel/myPosition";
    }

    /**
     * 去登录页面
     * @return
     */
    @GetMapping("/toLoginRegister")
    public String toLoginRegister(){
        return "hotel/loginRegister";
    }

    /**
     * 用户登录
     * @param map
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/login")
    public ResultDTO login(@RequestBody Map<String, String> map, HttpServletRequest request) {

        String phone = map.get("cPhone");
        String password = map.get("cPassword");
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(password)){
            return ResultDTO.errorOf(CustomizeErrorCode.USER_IS_EMPTY);
        }
        Customer customer = customerService.login(phone,password);
        if (customer==null){
            return ResultDTO.errorOf(CustomizeErrorCode.PHONE_PASSWORD_WRONG);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("customer",customer);
            return ResultDTO.okOf();
        }
    }

    /**
     * 检查手机号是否存在
     * @param phone
     * @return
     */
    @ResponseBody
    @GetMapping("/checkByPhone")
    public ResultDTO checkByPhone(@RequestParam("cPhone") String phone){
        if (StringUtils.isBlank(phone)){
            return ResultDTO.errorOf(CustomizeErrorCode.USER_IS_EMPTY);
        }
        Customer customer = customerService.checkByPhone(phone);
        if (customer==null){
            return ResultDTO.okOf();
        }
        return ResultDTO.errorOf(CustomizeErrorCode.PHONE_FOUND);
    }

    /**
     * 用户注册
     * @param customer
     * @return
     */
    @ResponseBody
    @PostMapping("/register")
    public ResultDTO register(@RequestBody Customer customer){
        if (StringUtils.isBlank(customer.getcPhone())){
            return ResultDTO.errorOf(CustomizeErrorCode.USER_IS_EMPTY);
        }
        int insert = customerService.insert(customer);
        if (insert >= 1){
            return ResultDTO.okOf();
        }
        return ResultDTO.errorOf(CustomizeErrorCode.REGISTER_FAIL);
    }

    /**
     * 用户的预约列表
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/reserve")
    public String reserve(Model model, HttpSession session){
        Customer customer = (Customer) session.getAttribute("customer");
        List<CustomerOrder> customerOrders = new ArrayList<>();
        if (!StringUtils.isBlank(customer.getcPhone())){
            customerOrders = customerService.selectByCPhone(customer.getcPhone());
        }
        model.addAttribute("customerOrders",customerOrders);
        return "hotel/reserve";
    }

    /**
     * 用户退出
     * @param session
     * @return
     */
    @GetMapping("/customerOut")
    public String customerOut(HttpSession session){
        session.removeAttribute("customer");
        return "redirect:home";
    }

    /**
     * 用户取消预约
     * @param id
     * @param roomNumber
     * @return
     */
    @ResponseBody
    @GetMapping("/customerUnsubscribe")
    public ResultDTO customerUnsubscribe(@RequestParam("id") Long id,
                                         @RequestParam("roomNumber") Long roomNumber){
        int unsubscribe = customerService.customerUnsubscribe(id, roomNumber);
        if (unsubscribe >= 2){
            return ResultDTO.okOf();
        }
        return ResultDTO.errorOf(CustomizeErrorCode.UNSUBSCRIBE_FAIL);
    }

    @GetMapping("/reservation")
    public String toReservation(){
        return "hotel/reservation";
    }

    @ResponseBody
    @PostMapping("reservation")
    public ResultDTO doReservation(@RequestBody CustomerOrder customerOrder, HttpSession session){
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer==null){
            return ResultDTO.errorOf(CustomizeErrorCode.USER_PARAM_WRONG);
        }
        Long roomNumber = customerOrder.getRoomNumber();
        String startTime = customerOrder.getStartTime();
        if (roomNumber==null || startTime ==null || startTime==""){
            return ResultDTO.errorOf(CustomizeErrorCode.USER_IS_EMPTY);
        }
        String roomStatus = roomService.checkRoomNumber(roomNumber).getRoomStatus();
        if (!roomStatus.equals("空闲")){
            return ResultDTO.errorOf(CustomizeErrorCode.ROOM_ALREADY_BOOKED);
        }
        int booking = customerService.customerBooking(customer, customerOrder);
        if (booking >= 2){
            return ResultDTO.okOf();
        }
        return ResultDTO.errorOf(CustomizeErrorCode.BOOKED_FAIL);
    }

}
