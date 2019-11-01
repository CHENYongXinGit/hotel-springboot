package person.cyx.hotel.mapper;

import org.apache.ibatis.annotations.Param;
import person.cyx.hotel.model.RoomType;

import java.util.List;

public interface RoomTypeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RoomType record);

    int insertSelective(RoomType record);

    RoomType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoomType record);

    int updateByPrimaryKey(RoomType record);

    List<RoomType> roomTypeList();

    List<RoomType> fuzzyQueryRoomType(@Param("name") String name);
}