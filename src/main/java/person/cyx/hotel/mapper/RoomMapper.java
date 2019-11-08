package person.cyx.hotel.mapper;

import org.apache.ibatis.annotations.Param;
import person.cyx.hotel.model.Room;

import java.util.List;

public interface RoomMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Room record);

    int insertSelective(Room record);

    Room selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Room record);

    int updateByPrimaryKey(Room record);

    Room checkRoomNumber(@Param("roomNumber") Long roomNumber);

    List<Room> roomList();

    List<Room> queryRoom(Room record);

    int updateRoomStatus(@Param("roomStatus") String roomStatus, @Param("roomNumber") Long roomNumber);

    List<Room> selectByRoomType(@Param("roomType") Long roomType);
}