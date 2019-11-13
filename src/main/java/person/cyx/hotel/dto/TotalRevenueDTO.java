package person.cyx.hotel.dto;

import lombok.Data;

import java.util.List;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-11-11 19:47
 **/
@Data
public class TotalRevenueDTO<T,D> {

    private List<T> name;
    private List<D> price;

}
