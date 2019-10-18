package person.cyx.hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-10-18 16:17
 **/
@Controller
public class IndexController {

    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "index";
    }
}
