package person.cyx.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import person.cyx.hotel.model.Admin;
import person.cyx.hotel.provider.UCloudProvider;
import person.cyx.hotel.service.impl.AdminServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

    @ResponseBody
    @PostMapping("/changePwd")
    public String doChangePwd(){
        return "updatePassword";
    }

    @GetMapping("/ModifyInformation")
    public String ModifyInformation(){
        return "safety/modifyInformation";
    }

    @ResponseBody
    @GetMapping("/doModifyInformation")
    public boolean doModifyInformation(@RequestParam("phone") String phone, HttpSession session){

        Admin currentUser = (Admin) session.getAttribute("currentUser");
        currentUser.setPhone(phone);
        int update = adminService.updateById(currentUser);
        if (update >= 1){
            session.setAttribute("currentUser",currentUser);
            return true;
        } else {
            return false;
        }
    }

    @ResponseBody
    @PostMapping("/upload")
    public boolean upload(HttpServletRequest request){

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
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
