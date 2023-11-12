package spa.spaserver.routerecommend.controller;


import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spa.spaserver.routerecommend.dto.RouteRequestDto;
import spa.spaserver.routerecommend.service.RouteService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/route")
@RestController
public class RouteController {

	private final RouteService routeService;

	@PostMapping("/newRoute")
	public ResponseEntity<?> newRoute(@RequestBody RouteRequestDto newRoute ,
		@Parameter(hidden = true) @AuthenticationPrincipal UserDetails member) {
		return routeService.saveRoute(newRoute, member);
	}
}
