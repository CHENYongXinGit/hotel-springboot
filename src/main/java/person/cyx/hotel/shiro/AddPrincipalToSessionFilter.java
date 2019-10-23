package person.cyx.hotel.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import person.cyx.hotel.model.Admin;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-10-22 15:22
 **/
public class AddPrincipalToSessionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        //查询当前用户的信息
        Subject subject = SecurityUtils.getSubject();
        //判断用户是不是通过自动登录进来的
        if (subject.isRemembered()) {
            //如果是，则获取它的手机号（我的登录是以手机号+密码登录的）
            Admin admin = (Admin) subject.getPrincipal();
            if (admin == null) {
                return;
            }
            //由于是继承的OncePerRequestFilter，没办法设置session
            //这里发现可以将servletReques强转为子类，所以使用request.getsiion())
            HttpServletRequest servletRequest = (HttpServletRequest) request;
            HttpSession session = servletRequest.getSession();
            //当session为空的时候
            if (session.getAttribute("currentUser")==null){
                //把查询到的用户信息设置为session，时效为3600秒
                session.setAttribute("currentUser",admin);
                //session.setMaxInactiveInterval(3600);
            }
        }
        chain.doFilter(request, response);
    }
}
