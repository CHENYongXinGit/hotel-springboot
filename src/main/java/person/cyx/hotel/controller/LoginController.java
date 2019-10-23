package person.cyx.hotel.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import person.cyx.hotel.dto.LoginDTO;
import person.cyx.hotel.dto.LoginRedisDTO;
import person.cyx.hotel.dto.ResultDTO;
import person.cyx.hotel.exception.CustomizeErrorCode;
import person.cyx.hotel.model.Admin;
import person.cyx.hotel.service.impl.AdminServiceImpl;
import person.cyx.hotel.util.RedisUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 登录认证Controller
 *
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-10-17 14:34
 **/
@Controller
@RequestMapping("/Login")
public class LoginController {

    @Value("${redis.login.countFailKey}")
    private String loginCountFailKey;

    @Autowired
    private AdminServiceImpl adminService;

    @Resource
    private RedisUtil redisUtil;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    /**
     * 登录认证
     * @param loginDTO
     * @return
     */
    @ResponseBody
    @PostMapping("/login")
    public ResultDTO doLogin(@RequestBody LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        boolean rememberMe = loginDTO.getRememberMe() == null ? false : true;
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            System.out.println("用户名或密码为空!");
            return ResultDTO.errorOf(CustomizeErrorCode.USER_IS_EMPTY);
        }
        //判断当前用户是否被限制登录
        LoginRedisDTO loginRedisDTO = adminService.loginUserLock(username);
        loginRedisDTO.setName(username);
        if (loginRedisDTO.getFlag()){
            System.out.println("因 "+loginRedisDTO.getName()+" 用户超过了限制登录次数，已被禁止登录。还剩："+loginRedisDTO.getLoginLockTime());
            return ResultDTO.errorOf(loginRedisDTO);
        } else {
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
                loginRedisDTO = adminService.loginValdate(username);
                System.out.println("密码错误,在"+loginRedisDTO.getLoginInvalidTime()+"内还允许输入错误" + loginRedisDTO.getAllowNum() + "次");
                return ResultDTO.errorOf(loginRedisDTO);
            } catch (Exception e){
                System.out.println("其他异常信息");
                e.printStackTrace();
                return ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            //验证是否登录成功
            if(currentUser.isAuthenticated()){
                //清空对应的所有key
                String key = loginCountFailKey+""+username;
                if (redisUtil.hasKey(key)){
                    redisUtil.del(key);
                }
                //把当前用户放入session
                Session session = currentUser.getSession();
                Admin admin = adminService.findAdminByUsername(username);
                System.out.println("currentUser:"+admin.getUsername()+"登录成功！");
                session.setAttribute("currentUser",admin);
                loginRedisDTO.setToken(currentUser.getSession().getId());
                return ResultDTO.okOf(loginRedisDTO);
            }else{
                token.clear();
                return ResultDTO.errorOf(CustomizeErrorCode.USER_PARAM_WRONG);
            }
        }
    }

    /**
     * 退出登录
     * @param session
     */
    @GetMapping("/out")
    public void out(HttpSession session){
        session.invalidate();
    }

}
