package spa.spaserver.member.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spa.spaserver.global.lib.Helper;
import spa.spaserver.member.domain.Member;
import spa.spaserver.member.dto.MemberRequestDto;
import spa.spaserver.member.dto.MemberRequestDto.SignUp;
import spa.spaserver.member.jwt.JwtTokenProvider;
import spa.spaserver.member.service.MemberService;
import spa.spaserver.global.test.dto.Response;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class MemberController {

	private final JwtTokenProvider jwtTokenProvider;
	private final MemberService memberService;
	private final Response response;

	@PostMapping("/sign-up")
	public ResponseEntity<?> signUp(@Validated @RequestBody SignUp signUp, Errors errors) {
		// validation check
		if (errors.hasErrors()) {
			return response.invalidFields(Helper.refineErrors(errors));
		}
		return memberService.signUp(signUp);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Validated @RequestBody MemberRequestDto.Login login, Errors errors) {
		// validation check
		if (errors.hasErrors()) {
			return response.invalidFields(Helper.refineErrors(errors));
		}
		return memberService.login(login);
	}

	@PostMapping("/reissue")
	public ResponseEntity<?> reissue(@Validated @RequestBody MemberRequestDto.Reissue reissue, Errors errors) {
		// validation check
		if (errors.hasErrors()) {
			return response.invalidFields(Helper.refineErrors(errors));
		}
		return memberService.reissue(reissue);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@Validated @RequestBody MemberRequestDto.Logout logout, Errors errors) {
		// validation check
		if (errors.hasErrors()) {
			return response.invalidFields(Helper.refineErrors(errors));
		}
		return memberService.logout(logout);
	}

	@GetMapping("/authority")
	public ResponseEntity<?> authority() {
		log.info("ADD ROLE_ADMIN");
		return memberService.authority();
	}

	@GetMapping("/userTest")
	public ResponseEntity<?> userTest(@Parameter(hidden = true) @AuthenticationPrincipal UserDetails member) {
		log.info("ROLE_USER TEST");
		log.info(member.getUsername());
		return response.success();
	}

	@GetMapping("/adminTest")
	public ResponseEntity<?> adminTest() {
		log.info("ROLE_ADMIN TEST");
		return response.success();
	}

}
