package person.cyx.hotel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement // 启注解事务管理
@MapperScan(value = "person.cyx.hotel.mapper")
@SpringBootApplication
public class HotelSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelSpringbootApplication.class, args);
    }

}
