package spa.spaserver.routerecommend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import spa.spaserver.routerecommend.dto.RouteRequestDto2;

@Service
public class RouteService {

	public ResponseEntity<?> saveRoute(RouteRequestDto2 routeService, UserDetails member) {
		return null;
	}

}
