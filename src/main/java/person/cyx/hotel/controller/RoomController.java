package person.cyx.hotel.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import person.cyx.hotel.dto.LayuiResult;
import person.cyx.hotel.dto.ResultDTO;
import person.cyx.hotel.exception.CustomizeErrorCode;
import person.cyx.hotel.mapper.RoomTypeMapper;
import person.cyx.hotel.model.RoomType;

import java.util.List;

/**
 * @program: hotel-springboot
 * @description
 * @author: chenyongxin
 * @create: 2019-11-01 14:12
 **/
@Controller
@RequestMapping("/Room")
public class RoomController {

    @Autowired
    private RoomTypeMapper roomTypeMapper;

    private LayuiResult<RoomType> result = new LayuiResult<RoomType>();

    @GetMapping("/toRoomTypeList")
    public String toRoomTypeList(){
        return "room/roomTypeList";
    }

    @GetMapping("/toRoomType")
    public String toRoomType(){
        return "room/roomType";
    }

    /**
     * 房间类型列表
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("/roomTypeList")
    public LayuiResult<RoomType> roomTypeList(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                   @RequestParam(value = "limit", defaultValue = "10")Integer limit){
        PageHelper.startPage(page, limit);
        List<RoomType> roomTypes = roomTypeMapper.roomTypeList();
        PageInfo pageInfo = new PageInfo(roomTypes, 5);
        result.setData(roomTypes);
        result.setCount(pageInfo.getTotal());
        return result;
    }

    /**
     * 房间类型添加&编辑
     * @param roomType
     * @return
     */
    @ResponseBody
    @PostMapping("/roomTypeOperate")
    public ResultDTO roomType(@RequestBody RoomType roomType){
        if (roomType.getId()!=null){
            int update = roomTypeMapper.updateByPrimaryKeySelective(roomType);
            if (update >= 1){
                return ResultDTO.okOf();
            } else {
                return ResultDTO.errorOf(CustomizeErrorCode.UPDATE_FAIL);
            }
        } else {
            roomType.setCreated(System.currentTimeMillis());
            int insert = roomTypeMapper.insertSelective(roomType);
            if (insert >= 1){
                return ResultDTO.okOf();
            } else {
                return ResultDTO.errorOf(CustomizeErrorCode.ADD_FAIL);
            }
        }
    }

    /**
     * 模糊查询房间类型
     * @param page
     * @param limit
     * @param name
     * @return
     */
    @ResponseBody
    @GetMapping("/fuzzyQueryRoomType")
    public LayuiResult<RoomType> fuzzyQueryRoomType(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                              @RequestParam(value = "limit", defaultValue = "10")Integer limit,
                                              @RequestParam("name") String name){
        PageHelper.startPage(page, limit);
        List<RoomType> roomTypes = roomTypeMapper.fuzzyQueryRoomType(name);
        PageInfo pageInfo = new PageInfo(roomTypes, 5);
        result.setData(roomTypes);
        result.setCount(pageInfo.getTotal());
        return result;
    }

    /**
     * 删除房间类型
     * @param id
     * @return
     */
    @ResponseBody
    @GetMapping("/delRoomType")
    public ResultDTO delRoomType(@RequestParam("id") Long id){
        int delete = roomTypeMapper.deleteByPrimaryKey(id);
        if (delete >= 1){
            return ResultDTO.okOf();
        } else {
            return ResultDTO.errorOf(CustomizeErrorCode.DELETE_FAIL);
        }
    }
}
