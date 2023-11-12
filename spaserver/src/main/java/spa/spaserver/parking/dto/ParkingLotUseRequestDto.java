package spa.spaserver.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParkingLotUseRequestDto {
	private String ParkingLotId;
	private String UsingCoordinate;
}
