package person.cyx.hotel.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import person.cyx.hotel.dto.LayuiResult;
import person.cyx.hotel.dto.ResultDTO;
import person.cyx.hotel.dto.RoomDTO;
import person.cyx.hotel.exception.CustomizeErrorCode;
import person.cyx.hotel.mapper.RoomTypeMapper;
import person.cyx.hotel.model.Room;
import person.cyx.hotel.model.RoomType;
import person.cyx.hotel.provider.UCloudProvider;
import person.cyx.hotel.service.impl.RoomServiceImpl;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private UCloudProvider uCloudProvider;
    @Autowired
    private RoomServiceImpl roomService;

    private LayuiResult<RoomType> resultRoomType = new LayuiResult<RoomType>();
    private LayuiResult<RoomDTO> resultRoomDTO = new LayuiResult<RoomDTO>();

    @GetMapping("/toRoomTypeList")
    public String toRoomTypeList(){
        return "room/roomTypeList";
    }

    @GetMapping("/toRoomList")
    public String toRoomList(Model model){
        List<RoomType> roomTypes = roomTypeMapper.roomTypeList();
        model.addAttribute("roomTypes",roomTypes);
        return "room/roomList";
    }

    @GetMapping("/toRoomType")
    public String toRoomType(){
        return "room/roomType";
    }

    @GetMapping("/toAddRoom")
    public String toAddRoom(Model model){
        List<RoomType> roomTypes = roomTypeMapper.roomTypeList();
        model.addAttribute("roomTypes",roomTypes);
        return "room/addRoom";
    }

    @GetMapping("/toEditRoom")
    public String toEditRoom(Model model){
        List<RoomType> roomTypes = roomTypeMapper.roomTypeList();
        model.addAttribute("roomTypes",roomTypes);
        return "room/editRoom";
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
        resultRoomType.setData(roomTypes);
        resultRoomType.setCount(pageInfo.getTotal());
        return resultRoomType;
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
        resultRoomType.setData(roomTypes);
        resultRoomType.setCount(pageInfo.getTotal());
        return resultRoomType;
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

    /**
     * 检查房间号是否存在
     * @param roomNumber
     * @return
     */
    @ResponseBody
    @GetMapping("/roomNumber")
    public ResultDTO roomNumber(@RequestParam("roomNumber") Long roomNumber,
                                @RequestParam(name = "defaultRoom", defaultValue = "0") Long defaultRoom){
        if (roomNumber==null || roomNumber==0){
            return ResultDTO.errorOf(CustomizeErrorCode.USER_IS_EMPTY);
        }
        Room room = roomService.checkRoomNumber(roomNumber);
        if (room == null || room.getRoomNumber() == defaultRoom) {
            return ResultDTO.okOf();
        }
        return ResultDTO.errorOf(CustomizeErrorCode.ROOMNUMBER_FOUND);
    }

    /**
     * 添加房间
     * @param file
     * @param room
     * @return
     */
    @ResponseBody
    @PostMapping("/addRoom")
    public ResultDTO addRoom(@RequestParam("file") MultipartFile file, Room room){

        try {
            String fileName = uCloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            System.out.println(fileName);
            room.setPhoto(fileName);
            int insert = roomService.insert(room);
            if (insert >= 1){
                return ResultDTO.okOf();
            } else {
                return ResultDTO.errorOf(CustomizeErrorCode.ADD_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultDTO.errorOf(CustomizeErrorCode.FILE_UPLOAD_FAIL);
    }

    /**
     * 房间列表
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("/roomList")
    public LayuiResult<RoomDTO> roomList(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                          @RequestParam(value = "limit", defaultValue = "10")Integer limit){
        PageHelper.startPage(page, limit);
        List<RoomDTO> rooms = roomService.roomList();
        PageInfo pageInfo = new PageInfo(rooms, 5);
        resultRoomDTO.setData(rooms);
        resultRoomDTO.setCount(pageInfo.getTotal());
        return resultRoomDTO;
    }

    /**
     * 删除房间
     * @param id
     * @return
     */
    @ResponseBody
    @GetMapping("/delRoom")
    public ResultDTO delRoom(@RequestParam("id") Long id){
        int delete = roomService.delete(id);
        if (delete >= 1){
            return ResultDTO.okOf();
        } else {
            return ResultDTO.errorOf(CustomizeErrorCode.DELETE_FAIL);
        }
    }

    /**
     * 更换房间图片
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/updatePhoto")
    public ResultDTO upload(HttpServletRequest request, @RequestParam("id") Long id){
        if (id==null){
            return ResultDTO.errorOf(CustomizeErrorCode.USER_IS_EMPTY);
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");
        try {
            String fileName = uCloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            System.out.println(fileName);
            Room room = new Room();
            room.setId(id);
            room.setPhoto(fileName);
            int update = roomService.updateById(room);
            if (update >= 1){
                return ResultDTO.okOf();
            } else {
                return ResultDTO.errorOf(CustomizeErrorCode.FILE_UPLOAD_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultDTO.errorOf(CustomizeErrorCode.FILE_UPLOAD_FAIL);
    }

    /**
     * 编辑房间信息
     * @param room
     * @return
     */
    @ResponseBody
    @PostMapping("/editRoom")
    public ResultDTO editRoom(@RequestBody Room room){
        if (room==null){
            return ResultDTO.errorOf(CustomizeErrorCode.USER_IS_EMPTY);
        }
        int update = roomService.updateById(room);
        if (update >= 1){
            return ResultDTO.okOf();
        } else {
            return ResultDTO.errorOf(CustomizeErrorCode.UPDATE_FAIL);
        }
    }

    @ResponseBody
    @GetMapping("/queryRoom")
    public LayuiResult<RoomDTO> queryRoom(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                           @RequestParam(value = "limit", defaultValue = "10")Integer limit,
                                           @RequestParam("roomNumber") Long roomNumber,
                                           @RequestParam("roomName") String roomName,
                                           @RequestParam("roomStatus") String roomStatus,
                                           @RequestParam("roomType") Long roomType){

        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setRoomName(roomName);
        room.setRoomStatus(roomStatus);
        room.setRoomType(roomType);

        PageHelper.startPage(page, limit);
        List<RoomDTO> roomDTOS = roomService.queryRoom(room);
        PageInfo pageInfo = new PageInfo(roomDTOS, 5);
        resultRoomDTO.setData(roomDTOS);
        resultRoomDTO.setCount(pageInfo.getTotal());
        return resultRoomDTO;
    }

}
