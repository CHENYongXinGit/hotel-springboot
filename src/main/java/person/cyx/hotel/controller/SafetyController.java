package person.cyx.hotel.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import person.cyx.hotel.dto.ResultDTO;
import person.cyx.hotel.exception.CustomizeErrorCode;
import person.cyx.hotel.model.Admin;
import person.cyx.hotel.provider.UCloudProvider;
import person.cyx.hotel.service.impl.AdminServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 安全管理Controller
 * 修改密码、注册用户、用户信息修改
 *
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-10-20 12:40
 **/
@Controller
@RequestMapping("/Safety")
public class SafetyController {

    @Autowired
    private AdminServiceImpl adminService;
    @Autowired
    private UCloudProvider uCloudProvider;

    @GetMapping("/changePwd")
    public String changePwd(){
        return "safety/updatePassword";
    }

    @PostMapping("/changePwd")
    public String doChangePwd(){
        return "safety/updatePassword";
    }

    @GetMapping("/ModifyInformation")
    public String ModifyInformation(){
        return "safety/modifyInformation";
    }

    @GetMapping("/SecurityManagement")
    public String SecurityManagement(){
        return "safety/securityManagement";
    }

    /**
     * 修改密码
     * @param map
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping("/updatePwd")
    public ResultDTO updatePwd(@RequestBody Map<String,String> map, HttpSession session){
        String password = map.get("password");
        Admin currentUser = (Admin) session.getAttribute("currentUser");
        SimpleHash simpleHash = new SimpleHash("md5", password, currentUser.getUsername(), 1024);
        String md5 = simpleHash.toString();
        currentUser.setPassword(md5);
        int update = adminService.updateById(currentUser);
        if (update >= 1){
            return ResultDTO.okOf();
        } else {
            return ResultDTO.errorOf(CustomizeErrorCode.UPDATE_FAIL);
        }
    }

    /**
     * 修改手机号
     * @param phone
     * @param session
     * @return
     */
    @ResponseBody
    @GetMapping("/doModifyInformation")
    public ResultDTO doModifyInformation(@RequestParam("phone") String phone, HttpSession session){
        if(StringUtils.isBlank(phone)){
            return ResultDTO.errorOf(CustomizeErrorCode.USER_IS_EMPTY);
        }
        Admin currentUser = (Admin) session.getAttribute("currentUser");
        currentUser.setPhone(phone);
        int update = adminService.updateById(currentUser);
        if (update >= 1){
            session.setAttribute("currentUser",currentUser);
            return ResultDTO.okOf();
        } else {
            return ResultDTO.errorOf(CustomizeErrorCode.UPDATE_FAIL);
        }
    }

    /**
     * 检查当前密码是否正确
     * @param password
     * @param session
     * @return
     */
    @ResponseBody
    @GetMapping("/rePassword")
    public ResultDTO rePassword(@RequestParam("password") String password,HttpSession session){
        if(StringUtils.isBlank(password)){
            System.out.println("密码为空!");
            return ResultDTO.errorOf(CustomizeErrorCode.USER_IS_EMPTY);
        }
        Admin currentUser = (Admin) session.getAttribute("currentUser");
        SimpleHash simpleHash = new SimpleHash("md5", password, currentUser.getUsername(), 1024);
        String md5 = simpleHash.toString();
        if (md5.equals(currentUser.getPassword())){
            System.out.println("当前密码正确");
            return ResultDTO.okOf();
        }
        System.out.println("当前密码不正确");
        return ResultDTO.errorOf(CustomizeErrorCode.USER_PASSWORD_FOUND);
    }

    /**
     * 更换头像
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/upload")
    public ResultDTO upload(HttpServletRequest request){

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");

        try {
            String fileName = uCloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            System.out.println(fileName);
            Admin currentUser = (Admin) request.getSession().getAttribute("currentUser");
            currentUser.setPhoto(fileName);
            int update = adminService.updateById(currentUser);
            if (update >= 1){
                request.getSession().setAttribute("currentUser",currentUser);
                return ResultDTO.okOf();
            } else {
                return ResultDTO.errorOf(CustomizeErrorCode.FILE_UPLOAD_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultDTO.errorOf(CustomizeErrorCode.FILE_UPLOAD_FAIL);
    }
}
