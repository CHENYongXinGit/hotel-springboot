package person.cyx.hotel.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import person.cyx.hotel.dto.LoginDTO;
import person.cyx.hotel.dto.ResultDTO;
import person.cyx.hotel.exception.CustomizeErrorCode;
import person.cyx.hotel.model.Admin;
import person.cyx.hotel.service.impl.AdminServiceImpl;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-10-17 14:34
 **/
@Controller
@RequestMapping("/Admin")
public class LoginController {

    @Autowired
    private AdminServiceImpl adminService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @ResponseBody
    @PostMapping("/doLogin")
    public ResultDTO doLogin(@RequestBody LoginDTO loginDTO) {

        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        boolean rememberMe = loginDTO.getRememberMe() == null ? false : true;

        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            System.out.println("用户名或密码为空!");
            return ResultDTO.errorOf(CustomizeErrorCode.USER_IS_EMPTY);
        }

        //创建Subject实例对象
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        //rememberMe
        token.setRememberMe(rememberMe);

        try {
            //执行登录
            currentUser.login(token);
        } catch (UnknownAccountException e) {
            System.out.println("账户不存在");
            return ResultDTO.errorOf(CustomizeErrorCode.USER_NOT_FOUND);
        } catch (IncorrectCredentialsException e){
            System.out.println("密码错误");
            return ResultDTO.errorOf(CustomizeErrorCode.PASSWORD_ERROR);
        } catch (Exception e){
            System.out.println("其他异常信息");
            e.printStackTrace();
            return ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
        }
        //验证是否登录成功
        if(currentUser.isAuthenticated()){
            //把当前用户放入session
            Session session = currentUser.getSession();
            Admin admin = adminService.findAdminByUsername(username);
            session.setAttribute("currentUser",admin);
            return ResultDTO.okOf();
        }else{
            token.clear();
            return ResultDTO.errorOf(CustomizeErrorCode.USER_PARAM_WRONG);
        }
    }

}
