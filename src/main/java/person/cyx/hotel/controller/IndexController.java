package person.cyx.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import person.cyx.hotel.dto.PieDTO;
import person.cyx.hotel.dto.PieResultDTO;
import person.cyx.hotel.dto.TotalDaysDTO;
import person.cyx.hotel.dto.TotalRevenueDTO;
import person.cyx.hotel.mapper.RoomTypeMapper;
import person.cyx.hotel.model.RoomType;
import person.cyx.hotel.service.impl.CustomerServiceImpl;
import person.cyx.hotel.service.impl.RoomServiceImpl;
import person.cyx.hotel.shiro.MyRealm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-10-18 16:17
 **/
@Controller
public class IndexController {

    @Autowired
    private MyRealm myRealm;
    @Autowired
    private CustomerServiceImpl customerService;
    @Autowired
    private RoomServiceImpl roomService;
    @Autowired
    private RoomTypeMapper roomTypeMapper;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 首页
     * @param model
     * @return
     */
    @GetMapping("/home")
    public String home(Model model){
        int countCustomers = customerService.countByCustomer();
        List<Double> list = customerService.selectByPrice();
        model.addAttribute("countCustomers",countCustomers);
        model.addAttribute("countOrder",(new Double(list.get(0))).intValue());
        model.addAttribute("sum",list.get(1));
        return "home";
    }

    /**
     * 房间统计
     * @return
     */
    @GetMapping("/roomStatistics")
    public String roomStatistics(){
        return "room/roomStatistics";
    }

    /**
     * 刷新权限
     * @return
     */
    @ResponseBody
    @GetMapping("/refresh")
    public boolean refresh(){
        myRealm.clearCache();
        return true;
    }

    /**
     * 月总收入图表
     * @return
     */
    @ResponseBody
    @GetMapping("/totalRevenue")
    public TotalRevenueDTO<String, Double> totalRevenue(){
        TotalRevenueDTO<String, Double> totalRevenueDTO = new TotalRevenueDTO<>();
        List<String> name = new ArrayList<>();
        List<Double> price = new ArrayList<>();
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String time = sdf.format(dt);
        String[] split = time.split("-");
        Integer s1 = Integer.valueOf(split[0]);
        Integer s2 = Integer.valueOf(split[1]);
        Double prices;
        for (int i = 0; i < 5; i++) {
            if (s2 == 1){
                name.add(s1+"-0"+s2);
                prices = customerService.countPriceByStartTime(s1+"-0"+s2);
                price.add(prices);
                s1 = s1-1;
                s2 = 12;
                continue;
            }
            if (s2 < 10){
                name.add(s1+"-0"+s2);
                prices = customerService.countPriceByStartTime(s1+"-0"+s2);
                price.add(prices);
            } else {
                name.add(s1+"-"+s2);
                prices = customerService.countPriceByStartTime(s1+"-"+s2);
                price.add(prices);
            }
            s2 = s2 - 1;
        }
        Collections.reverse(name);
        Collections.reverse(price);
        totalRevenueDTO.setName(name);
        totalRevenueDTO.setPrice(price);
        return totalRevenueDTO;
    }

    @ResponseBody
    @GetMapping("/days")
    public TotalDaysDTO days(){
        TotalDaysDTO totalDaysDTO = customerService.dayPriceByStartTime();
        return totalDaysDTO;
    }

    @ResponseBody
    @GetMapping("/type")
    public PieResultDTO type(){
        List<String> types = new ArrayList<>();
        List<PieDTO> pieDTOS = new ArrayList<>();
        List<RoomType> roomTypes = roomTypeMapper.roomTypeList();
        for (RoomType roomType : roomTypes) {
            PieDTO pieDTO = new PieDTO();
            Integer count = roomService.countByRoomType(roomType.getId());
            pieDTO.setValue(count);
            pieDTO.setName(roomType.getName());
            pieDTOS.add(pieDTO);
            types.add(roomType.getName());
        }
        PieResultDTO pieResultDTO = new PieResultDTO();
        pieResultDTO.setName(types);
        pieResultDTO.setData(pieDTOS);
        return pieResultDTO;
    }

    @ResponseBody
    @GetMapping("/status")
    public PieResultDTO status(){

        List<PieDTO> pieDTOS = new ArrayList<>();
        List<String> status = new ArrayList<>();
        status.add("空闲");
        status.add("入住");
        status.add("预订");
        for (String s : status) {
            PieDTO pieDTO = new PieDTO();
            Integer count = roomService.countByRoomStatus(s);
            pieDTO.setValue(count);
            pieDTO.setName(s);
            pieDTOS.add(pieDTO);
        }
        PieResultDTO pieResultDTO = new PieResultDTO();
        pieResultDTO.setName(status);
        pieResultDTO.setData(pieDTOS);
        return pieResultDTO;
    }
}
