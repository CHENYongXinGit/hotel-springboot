package person.cyx.hotel.service;

import person.cyx.hotel.dto.RoomDTO;
import person.cyx.hotel.model.Room;

import java.util.List;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-11-01 18:35
 **/
public interface RoomService {

    int insert(Room room);

    Room checkRoomNumber(Long roomNumber);

    List<RoomDTO> roomList();

    int delete(Long id);

    int updateById(Room room);

    List<RoomDTO> queryRoom(Room room);
}
