package person.cyx.hotel.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import person.cyx.hotel.util.ToolUtil;

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

    @GetMapping("/toRoomTypeList")
    @RequiresPermissions("room:list")
    public String toRoomTypeList(){
        return "room/roomTypeList";
    }

    @GetMapping("/toRoomList")
    @RequiresPermissions("room:list")
    public String toRoomList(Model model){
        List<RoomType> roomTypes = roomTypeMapper.roomTypeList();
        model.addAttribute("roomTypes",roomTypes);
        return "room/roomList";
    }

    @GetMapping("/toRoomType")
    @RequiresPermissions("room:list")
    public String toRoomType(){
        return "room/roomType";
    }

    @GetMapping("/toAddRoom")
    @RequiresPermissions("room:add")
    public String toAddRoom(Model model){
        List<RoomType> roomTypes = roomTypeMapper.roomTypeList();
        model.addAttribute("roomTypes",roomTypes);
        return "room/addRoom";
    }

    @GetMapping("/toEditRoom")
    @RequiresPermissions("room:edit")
    public String toEditRoom(Model model){
        List<RoomType> roomTypes = roomTypeMapper.roomTypeList();
        model.addAttribute("roomTypes",roomTypes);
        return "room/editRoom";
    }

    /**
     * 显示房间状态
     * @param page
     * @param limit
     * @param model
     * @return
     */
    @GetMapping("/roomState")
    @RequiresPermissions("room:list")
    public String toRoomState(@RequestParam(value = "page", defaultValue = "1") Integer page,
                              @RequestParam(value = "limit", defaultValue = "12") Integer limit,
                              Model model){
        PageHelper.startPage(page, limit, "room_number asc");
        List<Room> rooms = roomService.roomList();
        PageInfo pageInfo = new PageInfo(rooms, 5);
        model.addAttribute("rooms",rooms);
        model.addAttribute("total",pageInfo.getTotal());
        model.addAttribute("page",page);
        model.addAttribute("limit",limit);
        return "room/roomState";
    }

    /**
     * 房间类型列表
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("/roomTypeList")
    @RequiresPermissions("room:list")
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
    @RequiresPermissions({"room:add","room:edit"})
    public ResultDTO roomType(@RequestParam("file") MultipartFile file, RoomType roomType){
        if (file.isEmpty() && roomType.getId()==null){
            return ResultDTO.errorOf(CustomizeErrorCode.CHOOSE_FILE_UPLOAD);
        }
        String fileName = null;
        if (!file.isEmpty()){
            try {
                fileName = uCloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            } catch (Exception e) {
                e.printStackTrace();
                return ResultDTO.errorOf(CustomizeErrorCode.FILE_UPLOAD_FAIL);
            }
        }
        if (roomType.getId()!=null){
            if (fileName != null){
                roomType.setPhoto(fileName);
            }
            int update = roomTypeMapper.updateByPrimaryKeySelective(roomType);
            if (update >= 1){
                return ResultDTO.okOf();
            } else {
                delPhoto(fileName);
                return ResultDTO.errorOf(CustomizeErrorCode.UPDATE_FAIL);
            }
        } else {
            roomType.setPhoto(fileName);
            roomType.setCreated(System.currentTimeMillis());
            int insert = roomTypeMapper.insertSelective(roomType);
            if (insert >= 1){
                return ResultDTO.okOf();
            } else {
                delPhoto(fileName);
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
    @RequiresPermissions("room:del")
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
        return ResultDTO.errorOf(CustomizeErrorCode.ROOM_NUMBER_FOUND);
    }

    /**
     * 添加房间
     * @param file
     * @param room
     * @return
     */
    @ResponseBody
    @PostMapping("/addRoom")
    @RequiresPermissions("room:add")
    public ResultDTO addRoom(@RequestParam("file") MultipartFile file, Room room){

        try {
            String fileName = uCloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            room.setPhoto(fileName);
            int insert = roomService.insert(room);
            if (insert >= 1){
                return ResultDTO.okOf();
            } else {
                delPhoto(fileName);
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
    @RequiresPermissions("room:list")
    public LayuiResult<RoomDTO> roomList(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                          @RequestParam(value = "limit", defaultValue = "10")Integer limit){

        LayuiResult<RoomDTO> roomDTOLayuiResult = roomService.roomList(page, limit);
        return roomDTOLayuiResult;
    }

    /**
     * 删除房间
     * @param id
     * @return
     */
    @ResponseBody
    @GetMapping("/delRoom")
    @RequiresPermissions("room:del")
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
    @RequiresPermissions("room:edit")
    public ResultDTO upload(HttpServletRequest request, @RequestParam("id") Long id){
        if (id==null){
            return ResultDTO.errorOf(CustomizeErrorCode.USER_IS_EMPTY);
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");
        try {
            String fileName = uCloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            Room room = new Room();
            room.setId(id);
            room.setPhoto(fileName);
            int update = roomService.updateById(room);
            if (update >= 1){
                return ResultDTO.okOf();
            } else {
                delPhoto(fileName);
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
    @RequiresPermissions("room:edit")
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

    /**
     * 模糊查询房间
     * @param page
     * @param limit
     * @param roomNumber
     * @param roomName
     * @param roomStatus
     * @param roomType
     * @return
     */
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

        LayuiResult<RoomDTO> roomDTOLayuiResult = roomService.queryRoom(page, limit, room);
        return roomDTOLayuiResult;
    }

    /**
     * 房间列表排序
     * @param page
     * @param limit
     * @param field
     * @param order
     * @return
     */
    @ResponseBody
    @GetMapping("/sortRoom")
    @RequiresPermissions("room:list")
    public LayuiResult<RoomDTO> sortRoom(@RequestParam(value = "page", defaultValue = "1")Integer page,
                                         @RequestParam(value = "limit", defaultValue = "10")Integer limit,
                                         @RequestParam("field") String field,
                                         @RequestParam("order") String order){

        String hump = ToolUtil.humpToLine2(field);
        String orderBy = hump+" "+order;
        LayuiResult<RoomDTO> roomDTOLayuiResult = roomService.roomList(page, limit, orderBy);
        return roomDTOLayuiResult;
    }


    /**
     * 删除云端图片
     * @param fileName
     */
    private void delPhoto(String fileName) {
        String[] filePaths = fileName.split("\\?")[0].split("/");
        fileName = filePaths[filePaths.length - 1];
        uCloudProvider.delete(fileName);
    }

}
