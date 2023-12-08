package spa.spaserver.map.dto;

import java.text.DecimalFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MapResponseDto {

	public void setLocation(double[] location) {
		DecimalFormat decimalFormat = new DecimalFormat("#.######");
		String stringValueX = decimalFormat.format(location[0]);
		String stringValueY = decimalFormat.format(location[1]);
		this.x = stringValueX;
		this.y = stringValueY;
	}


	String x;
	String y;

}
