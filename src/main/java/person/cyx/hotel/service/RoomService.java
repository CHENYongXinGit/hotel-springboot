package person.cyx.hotel.service;

import person.cyx.hotel.dto.LayuiResult;
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

    List<Room> roomList();

    LayuiResult<RoomDTO> roomList(Integer page, Integer limit);

    LayuiResult<RoomDTO> roomList(Integer page, Integer limit, String orderBy);

    int delete(Long id);

    int updateById(Room room);

    LayuiResult<RoomDTO> queryRoom(Integer page, Integer limit, Room room);

    List<Room> selectByRoomType(Long roomType);

    Room selectByRoomId(Long id);

    Integer countByRoomType(Long id);

    Integer countByRoomStatus(String roomStatus);
}
