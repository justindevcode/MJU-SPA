package spa.spaserver.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParkingLotRequestDto {
	private String low;
	private String column;
	private String data;
}
