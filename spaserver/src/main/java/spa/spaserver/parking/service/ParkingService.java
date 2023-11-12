package spa.spaserver.parking.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import spa.spaserver.parking.dto.ParkingLotRequestDto;
import spa.spaserver.parking.dto.ParkingLotUseRequestDto;

@Service
public class ParkingService {

	public ResponseEntity<?> useParkingLot(ParkingLotUseRequestDto newParkingLot, UserDetails member) {
		return null;
	}



}
