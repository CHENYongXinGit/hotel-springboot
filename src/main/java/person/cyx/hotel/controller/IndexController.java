package person.cyx.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import person.cyx.hotel.shiro.MyRealm;

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

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @ResponseBody
    @GetMapping("/refresh")
    public boolean refresh(){
        myRealm.clearCache();
        return true;
    }
}
