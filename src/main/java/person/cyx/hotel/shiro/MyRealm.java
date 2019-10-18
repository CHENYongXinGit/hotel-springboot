package person.cyx.hotel.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import person.cyx.hotel.model.Admin;
import person.cyx.hotel.service.impl.AdminServiceImpl;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-10-17 16:01
 **/
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private AdminServiceImpl adminService;

    /**
     * @param principals
     * @return
     *      授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * @param token
     * @return
     * @throws AuthenticationException
     *      认证登录，查询数据库，如果该用户名正确，得到正确的数据，并返回正确的数据
     *      AuthenticationInfo的实现类SimpleAuthenticationInfo保存正确的用户信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        //1. token 中获取登录的 username! 注意不需要获取password.
        String username = ((UsernamePasswordToken) token).getUsername();

        //2. 利用 username 查询数据库得到用户的信息.
        Admin admin= adminService.findAdminByUsername(username);
        if (admin == null){
            return null;
        }
        String password = admin.getPassword();
        //3.设置盐值 ，（加密的调料，让加密出来的东西更具安全性，一般是通过数据库查询出来的。 简单的说，就是把密码根据特定的东西而进行动态加密，如果别人不知道你的盐值，就解不出你的密码）
        String source = username;

        //返回值实例化
        SimpleAuthenticationInfo info =
                new SimpleAuthenticationInfo(admin, password, ByteSource.Util.bytes(source), getName());
        return info;
    }

    /*public static void main(String[] args) {
        SimpleHash simpleHash = new SimpleHash("MD5", "123456", ByteSource.Util.bytes("糖果"), 1024);
        System.out.println("密码===>"+simpleHash);
    }*/
}
