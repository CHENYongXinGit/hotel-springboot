package person.cyx.hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-10-17 14:34
 **/
@Controller
public class LoginController {

    @ResponseBody
    @RequestMapping("/")
    public String checklogin(){
        return "Hello login";
    }
}
