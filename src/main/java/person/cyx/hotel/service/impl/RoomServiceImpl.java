package person.cyx.hotel.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import person.cyx.hotel.dto.RoomDTO;
import person.cyx.hotel.mapper.RoomMapper;
import person.cyx.hotel.mapper.RoomTypeMapper;
import person.cyx.hotel.model.Room;
import person.cyx.hotel.model.RoomType;
import person.cyx.hotel.service.RoomService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-11-01 18:35
 **/
@Service("roomServiceImpl")
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private RoomTypeMapper roomTypeMapper;

    @Override
    public int insert(Room room) {
        if (room.getRoomWindow()==null){
            room.setRoomWindow(0);
        }
        room.setCreated(System.currentTimeMillis());
        room.setUpdated(System.currentTimeMillis());
        return roomMapper.insertSelective(room);
    }

    @Override
    public Room checkRoomNumber(Long roomNumber) {
        return roomMapper.checkRoomNumber(roomNumber);
    }

    @Override
    public List<RoomDTO> roomList() {
        List<Room> rooms = roomMapper.roomList();
        return getRoomDTOS(rooms);
    }

    @Override
    public int delete(Long id) {
        return roomMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(Room room) {
        if (room.getRoomWindow()==null){
            room.setRoomWindow(0);
        }
        room.setUpdated(System.currentTimeMillis());
        return roomMapper.updateByPrimaryKeySelective(room);
    }

    @Override
    public List<RoomDTO> queryRoom(Room queryRoom) {
        List<Room> rooms = roomMapper.queryRoom(queryRoom);
        return getRoomDTOS(rooms);
    }

    private List<RoomDTO> getRoomDTOS(List<Room> rooms) {
        List<RoomType> roomTypes = roomTypeMapper.roomTypeList();

        Map<Long, RoomType> roomTypeMap = roomTypes.stream().collect(Collectors.toMap(roomType -> roomType.getId(), roomType -> roomType));

        List<RoomDTO> roomDTOS = rooms.stream().map(room -> {
            RoomDTO roomDTO = new RoomDTO();
            BeanUtils.copyProperties(room, roomDTO);
            roomDTO.setType(roomTypeMap.get(room.getRoomType()));
            return roomDTO;
        }).collect(Collectors.toList());

        return roomDTOS;
    }
}
