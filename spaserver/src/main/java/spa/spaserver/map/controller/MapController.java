package spa.spaserver.map.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spa.spaserver.global.test.dto.Response;
import spa.spaserver.map.dto.ChargeRequestDto;
import spa.spaserver.map.dto.MapRequestDto;
import spa.spaserver.map.dto.MapResponseDto;
import spa.spaserver.map.dto.RouteRequestDto;
import spa.spaserver.map.dto.RouteResponseDto;
import spa.spaserver.map.service.MapService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/map")
@RestController
public class MapController {

	private final MapService mapService;

	private final Response response;

	@PostMapping("/location")
	public ResponseEntity<?> getLocation(@RequestBody MapRequestDto mapRequestDto) {
		MapResponseDto mapResponseDto = new MapResponseDto();
		log.info("컨트롤러단 확인 {}", mapRequestDto.getLocation());
		mapResponseDto.setLocation(mapService.main(mapRequestDto.getLocation()));
		return  response.success(mapResponseDto, "위치좌표를 찾았습니다.", HttpStatus.OK);
	}

	@PostMapping("/charge")
	private ResponseEntity<?> getCharge(@RequestBody ChargeRequestDto chargeRequestDto) {
		MapResponseDto mapResponseDto = new MapResponseDto();
		mapResponseDto.setLocation(mapService.chargeMain(chargeRequestDto));
		return  response.success(mapResponseDto, "위치좌표를 찾았습니다.", HttpStatus.OK);
	}

	@PostMapping("/route")
	public ResponseEntity<?> getRoute(@RequestBody RouteRequestDto routeRequestDto) {
		String result = mapService.routePrint(routeRequestDto);
		RouteResponseDto routeResponseDto = new RouteResponseDto(result);
		return response.success(routeResponseDto, "결과출력", HttpStatus.OK);
	}


}
