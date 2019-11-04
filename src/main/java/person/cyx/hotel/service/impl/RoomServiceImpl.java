package person.cyx.hotel.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import person.cyx.hotel.dto.LayuiResult;
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

    private LayuiResult<RoomDTO> resultRoomDTO = new LayuiResult();

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
    public List<Room> roomList() {
        return roomMapper.roomList();
    }

    @Override
    public LayuiResult<RoomDTO> roomList(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        return getRoomDTOLayuiResult();
    }

    @Override
    public LayuiResult<RoomDTO> roomList(Integer page, Integer limit, String orderBy) {
        PageHelper.startPage(page, limit, orderBy);
        return getRoomDTOLayuiResult();
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
    public LayuiResult<RoomDTO> queryRoom(Integer page, Integer limit, Room room) {
        PageHelper.startPage(page, limit);
        List<Room> rooms = roomMapper.queryRoom(room);
        PageInfo pageInfo = new PageInfo(rooms, 5);
        List<RoomDTO> roomDTOS = getRoomDTOS(rooms);
        pageInfo.setList(roomDTOS);
        resultRoomDTO.setData(roomDTOS);
        resultRoomDTO.setCount(pageInfo.getTotal());
        return resultRoomDTO;
    }

    /**
     * 添加房间类型字段
     * @param rooms
     * @return
     */
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

    /**
     * 房间列表分页
     * @return
     */
    private LayuiResult<RoomDTO> getRoomDTOLayuiResult() {
        List<Room> rooms = roomMapper.roomList();
        PageInfo pageInfo = new PageInfo(rooms, 5);
        List<RoomDTO> roomDTOS = getRoomDTOS(rooms);
        pageInfo.setList(roomDTOS);
        resultRoomDTO.setData(roomDTOS);
        resultRoomDTO.setCount(pageInfo.getTotal());
        return resultRoomDTO;
    }
}
