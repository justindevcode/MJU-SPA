package spa.spaserver.map.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RouteRequestDto {

	String location1;
	String location2;
	String location3;

	double time1;
	double time2;
	double time3;

	int battery;

}
