package person.cyx.hotel.dto;

import lombok.Data;
import person.cyx.hotel.model.RoomType;

@Data
public class RoomDTO {
    private Long id;

    private String roomName;

    private Long roomNumber;

    private String bedType;

    private String broadband;

    private Double standardPrice;

    private Double memberPrice;

    private Integer roomWindow;

    private String roomArea;

    private String roomStatus;

    private Long roomType;

    private String photo;

    private Long created;

    private Long updated;

    private RoomType type;
}