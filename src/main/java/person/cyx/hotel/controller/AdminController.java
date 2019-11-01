package person.cyx.hotel.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.LogoutAware;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import person.cyx.hotel.dto.LayuiResult;
import person.cyx.hotel.dto.ResultDTO;
import person.cyx.hotel.exception.CustomizeErrorCode;
import person.cyx.hotel.model.Admin;
import person.cyx.hotel.service.impl.AdminServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-10-23 19:01
 **/
@Controller
@RequestMapping("/Admin")
public class AdminController {

    @Autowired
    private RedisSessionDAO redisSessionDAO;
    @Autowired
    private AdminServiceImpl adminService;

    private LayuiResult<Admin> result = new LayuiResult<Admin>();

    @GetMapping("/register")
    @RequiresPermissions("user:add")
    public String toRegister(){
        return "admin/register";
    }

    @GetMapping("/toList")
    @RequiresPermissions("user:list")
    public String toList(){
        return "admin/adminList";
    }

    /**
     * 用户注册
     * @param admin
     * @return
     */
    @ResponseBody
    @PostMapping("/register")
    @RequiresPermissions("user:add")
    public ResultDTO doRegister(@RequestBody Admin admin){
        if(StringUtils.isBlank(admin.getUsername()) || StringUtils.isBlank(admin.getPhone()) || StringUtils.isBlank(admin.getPassword())){
            return ResultDTO.errorOf(CustomizeErrorCode.USER_IS_EMPTY);
        }
        Admin adminUsername = adminService.findAdminByUsername(admin.getUsername());
        if (adminUsername != null) {
            return ResultDTO.errorOf(CustomizeErrorCode.USERNAME_FOUND);
        }
        int insert = adminService.insertSelective(admin);
        if (insert >= 1) {
            return ResultDTO.okOf();
        } else {
            return ResultDTO.errorOf(CustomizeErrorCode.REGISTER_FAIL);
        }
    }

    /**
     * 检查手机号是否存在
     * @param phone
     * @return
     */
    @ResponseBody
    @GetMapping("/checkByPhone")
    public ResultDTO checkByPhone(@RequestParam("phone") String phone){
        if(StringUtils.isBlank(phone)){
            return ResultDTO.errorOf(CustomizeErrorCode.USER_IS_EMPTY);
        }
        Admin admin = adminService.checkByPhone(phone);
        if (admin==null){
            System.out.println("手机号可用");
            return ResultDTO.okOf();
        } else {
            System.out.println("手机号已存在");
            return ResultDTO.errorOf(CustomizeErrorCode.PHONE_FOUND);
        }
    }

    /**
     * 用户列表
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("user:list")
    public LayuiResult<Admin> list(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                   @RequestParam(value = "limit", defaultValue = "10")Integer limit){
        PageHelper.startPage(page, limit);
        List<Admin> admins = adminService.list();
        PageInfo pageInfo = new PageInfo(admins, 5);
        result.setData(admins);
        result.setCount(pageInfo.getTotal());
        return result;
    }

    /**
     * 模糊查询用户名
     * @param page
     * @param limit
     * @param username
     * @return
     */
    @ResponseBody
    @GetMapping("/fuzzyQueryUsername")
    public LayuiResult<Admin> fuzzyQueryUsername(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                               @RequestParam(value = "limit", defaultValue = "10")Integer limit,
                                                 @RequestParam("username") String username){
        PageHelper.startPage(page, limit);
        List<Admin> admins = adminService.fuzzyQueryUsername(username);
        PageInfo pageInfo = new PageInfo(admins, 5);
        result.setData(admins);
        result.setCount(pageInfo.getTotal());
        return result;
    }

    /**
     * 模糊查询手机号
     * @param page
     * @param limit
     * @param phone
     * @return
     */
    @ResponseBody
    @GetMapping("/fuzzyQueryPhone")
    public LayuiResult<Admin> fuzzyQueryPhone(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                                 @RequestParam(value = "limit", defaultValue = "10")Integer limit,
                                                 @RequestParam("phone") String phone){
        PageHelper.startPage(page, limit);
        List<Admin> admins = adminService.fuzzyQueryPhone(phone);
        PageInfo pageInfo = new PageInfo(admins, 5);
        result.setData(admins);
        result.setCount(pageInfo.getTotal());
        return result;
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @ResponseBody
    @GetMapping("/delete")
    public ResultDTO delete(@RequestParam("id") Long id){
        int delete = adminService.delete(id);
        if (delete>=1){
            return ResultDTO.okOf();
        } else {
            return ResultDTO.errorOf(CustomizeErrorCode.DELETE_FAIL);
        }
    }

    /**
     * 查看用户登录信息
     * @param username
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/view")
    public String view(@RequestParam("username") String username, Model model) throws Exception{

        System.out.println(username);
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");

        for(Session session:sessions){
            if (session.getAttribute("currentUser") == null){
                continue;
            }
            Admin currentUser = (Admin) session.getAttribute("currentUser");
            if (username.equals(currentUser.getUsername())){
                String startTimestamp = dateFormat.format(session.getStartTimestamp());
                String lastAccessTime = dateFormat.format(session.getLastAccessTime());
                model.addAttribute("getHost",session.getHost());
                model.addAttribute("getStartTimestamp",startTimestamp);
                model.addAttribute("getLastAccessTime",lastAccessTime);
                System.out.println("登录ip:"+session.getHost());
                System.out.println("登录用户"+currentUser.getUsername());
                System.out.println("登录日期:"+ startTimestamp);
                System.out.println("最后操作日期:"+lastAccessTime);
                break;
            }
        }
        return "admin/viewUser";
    }

    /**
     * 下线用户
     * @param username
     * @return
     */
    @ResponseBody
    @GetMapping("/offline")
    public ResultDTO offline(@RequestParam("username") String username){
        if (doOffline(username)){
            return ResultDTO.okOf();
        }
        return ResultDTO.errorOf(CustomizeErrorCode.OFFLINE_FAIL);
    }

    @ResponseBody
    @PostMapping("/updateUser")
    public ResultDTO updateUser(@RequestBody Admin admin){
        if (admin.getStatus()==1 && admin.getUsername()!=null){
            if (!doOffline(admin.getUsername())){
                return ResultDTO.errorOf(CustomizeErrorCode.OFFLINE_FAIL);
            }
        }
        int update = adminService.updateById(admin);
        if (update >= 1){
            return ResultDTO.okOf();
        }
        return ResultDTO.errorOf(CustomizeErrorCode.UPDATE_FAIL);
    }

    private boolean doOffline(String username) {
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        Object attribute = null;

        for(Session session:sessions) {
            if (session.getAttribute("currentUser") == null){
                continue;
            }
            Admin currentUser = (Admin) session.getAttribute("currentUser");
            if (username.equals(currentUser.getUsername())) {
                redisSessionDAO.delete(session);
                attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);

                //删除Cache，再访问受限接口时会重新授权
                DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
                Authenticator authc = securityManager.getAuthenticator();
                ((LogoutAware) authc).onLogout((SimplePrincipalCollection) attribute);
                return true;
            }
        }
        return false;
    }
}
