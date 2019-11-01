package person.cyx.hotel.service.impl;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import person.cyx.hotel.dto.LoginRedisDTO;
import person.cyx.hotel.mapper.AdminMapper;
import person.cyx.hotel.model.Admin;
import person.cyx.hotel.service.AdminService;
import person.cyx.hotel.util.RedisUtil;
import person.cyx.hotel.util.TimeSwitchUtil;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-10-18 10:16
 **/
@Service("adminServiceImpl")
public class AdminServiceImpl implements AdminService {

    @Value("${redis.login.countFailKey}")
    private String loginCountFailKey;
    @Value("${redis.login.timeLockKey}")
    private String loginTimeLockKey;
    @Value("${redis.login.num}")
    private Long loginNum;
    @Value("${redis.login.invalidTime}")
    private Long loginInvalidTime;
    @Value("${redis.login.lockTime}")
    private Long loginLockTime;

    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private AdminMapper adminMapper;

    private LoginRedisDTO loginRedisDTO = new LoginRedisDTO();

    @Override
    public Admin findAdminByUsername(String username) {
        return adminMapper.findAdminByUsername(username);
    }

    @Override
    public int updateById(Admin admin) {
        admin.setUpdated(System.currentTimeMillis());
        return adminMapper.updateByPrimaryKeySelective(admin);
    }

    /**
     * 1判断当前登录的用户是否被限制登录
     * 查询当前KEY是否存在，如果存在，就被限制注意:需要给用户做提示:您当前的用户已被限制，还剩多长时间
     * 如果不存在，就不被限制。
     */
    @Override
    public LoginRedisDTO loginUserLock(String username) {
        String key = loginTimeLockKey+""+username;
        if (redisUtil.hasKey(key)) {
            //以秒为单位进行返回
            Long longLockTime = redisUtil.getExpire(key);
            //如果存在
            loginRedisDTO.setFlag(true);
            //清空对应的key
            String countFailKey = loginCountFailKey+""+username;
            if (redisUtil.hasKey(countFailKey)){
                redisUtil.del(countFailKey);
            }
            //还剩多长时间
            loginRedisDTO.setLoginLockTime(TimeSwitchUtil.secToTime(longLockTime));
        } else {
            loginRedisDTO.setFlag(false);
        }
        return loginRedisDTO;
    }

    /**
     * 登录不成功相应操作
     */
    @Override
    public synchronized LoginRedisDTO loginValdate(String username) {

        //记录登录错误次数key
        String key = loginCountFailKey+""+username;
        //登录错误的次数
        long allowNum;
        //如果不存在
        if (!redisUtil.hasKey(key)) {
            //是第一次登录失败次数为1赋值为1和设置失效期30分钟user:loginCount:fail:用户名进行赋值，同时设置失效期
            //再设置失效期30分钟
            redisUtil.set(key, 1, loginInvalidTime);
            //在30分钟内还允许输入错误 + (loginNum - 1) + 次
            allowNum = loginNum - 1;
            loginRedisDTO.setLoginInvalidTime(TimeSwitchUtil.secToTime(loginInvalidTime));
            loginRedisDTO.setAllowNum(allowNum);
            return loginRedisDTO;
        } else {//如果存在
            //查询登录失败次数的key结果
            long loginFailCount = ((Integer) redisUtil.get(key)).longValue();
            //代表如果当前登录失败次数<4意思:还有资格继续进行登录
            if (loginFailCount < (loginNum - 1)) {
                //user:loginCount:fail:+1登录次数+1
                //对指定KEY 增加指定数据
                redisUtil.incr(key, 1);
                //在 + loginInvalidTime + 内还允许输入错误
                loginInvalidTime = redisUtil.getExpire(key);
                //还允许输入错误 + (loginNum - loginFailCount - 1) + 次
                allowNum = loginNum - loginFailCount - 1;
                loginRedisDTO.setLoginInvalidTime(TimeSwitchUtil.secToTime(loginInvalidTime));
                loginRedisDTO.setAllowNum(allowNum);
                return loginRedisDTO;
            } else { //超过指定登录次数
                //限制登录KEY存在,同时设置限制登录时间锁定1小时。
                redisUtil.set(loginTimeLockKey+""+username, "1", loginLockTime);
                //"因登录失败次数超过限制" + loginNum + "次，已对其限制登录1小时"
                loginRedisDTO.setFlag(true);
                loginRedisDTO.setAllowNum(0L);
                loginRedisDTO.setLoginLockTime(TimeSwitchUtil.secToTime(loginLockTime));
                return loginRedisDTO;
            }
        }
    }

    @Override
    public Admin checkByPhone(String phone) {
        return adminMapper.checkByPhone(phone);
    }

    @Override
    public int insertSelective(Admin admin) {
        SimpleHash simpleHash = new SimpleHash("md5", admin.getPassword(), admin.getUsername(), 1024);
        String md5 = simpleHash.toString();
        admin.setPassword(md5);
        admin.setCreated(System.currentTimeMillis());
        admin.setUpdated(System.currentTimeMillis());
        return adminMapper.insertSelective(admin);
    }

    @Override
    public Admin login(String username, String password) {
        return adminMapper.login(username,password);
    }

    @Override
    public List<Admin> list() {
        return adminMapper.list();
    }

    @Override
    public List<Admin> fuzzyQueryUsername(String username) {
        return adminMapper.fuzzyQueryUsername(username);
    }

    @Override
    public List<Admin> fuzzyQueryPhone(String phone) {
        return adminMapper.fuzzyQueryPhone(phone);
    }

    @Override
    public int delete(Long id) {
        return adminMapper.deleteByPrimaryKey(id);
    }
}
