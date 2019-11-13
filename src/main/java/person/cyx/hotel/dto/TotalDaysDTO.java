package person.cyx.hotel.dto;

import lombok.Data;

import java.util.List;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-11-13 14:05
 **/
@Data
public class TotalDaysDTO {

    private List<String> names;
    private List<Integer> orders;
    private List<Double> prices;
}
