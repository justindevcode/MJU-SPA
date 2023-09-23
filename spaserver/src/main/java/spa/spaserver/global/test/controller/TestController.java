package spa.spaserver.global.test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spa.spaserver.global.test.dto.Response;
import spa.spaserver.global.test.dto.TestRequest;
import spa.spaserver.global.test.service.TestService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

	private final TestService testService;

	private final Response response;


	@PostMapping
	public ResponseEntity<?> addTest(@RequestBody TestRequest testRequest) {
		return response.success(testService.save(testRequest),"success", HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<?> getAllTest() {
		return response.success(testService.getTestAll(),"success",HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<?> deleteAll() {
		return response.success(testService.deleteAllTest(), "success", HttpStatus.OK);
	}

	@GetMapping("/test")
	public ResponseEntity<?> getTest() {
		return response.success("테스트성공","success",HttpStatus.OK);
	}

	@GetMapping("/text")
	public ResponseEntity<?> getText() {
		return response.success("test CICD11","success",HttpStatus.OK);
	}

}