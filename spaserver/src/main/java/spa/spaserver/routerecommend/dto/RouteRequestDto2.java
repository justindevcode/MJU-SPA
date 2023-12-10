package spa.spaserver.routerecommend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RouteRequestDto2 {
	private String routeCount;
	private String routeCoordinate;
	private String start;
	private String End;
	private String carStatus;
	private String chargingStation;
}
