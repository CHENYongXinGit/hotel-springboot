package person.cyx.hotel.dto;

import lombok.Data;

import java.util.List;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-11-13 16:07
 **/
@Data
public class PieResultDTO {

    private List<String> name;
    private List<PieDTO> data;
}
