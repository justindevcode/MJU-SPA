package spa.spaserver.parking.controller;


import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spa.spaserver.member.dto.MemberRequestDto;
import spa.spaserver.parking.dto.ParkingLotRequestDto;
import spa.spaserver.parking.dto.ParkingLotUseRequestDto;
import spa.spaserver.parking.service.ParkingService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/parking")
@RestController
public class ParkingController {

	private final ParkingService parkingService;

	@PostMapping("/useParkingLot")
	public ResponseEntity<?> useParkingLot(@RequestBody ParkingLotUseRequestDto newParkingLot,
		@Parameter(hidden = true) @AuthenticationPrincipal UserDetails member) {
		return parkingService.useParkingLot(newParkingLot, member);
	}
}
