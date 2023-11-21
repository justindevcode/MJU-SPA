package spa.spaserver.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spa.spaserver.member.domain.Member;
import spa.spaserver.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {

	private final MemberRepository usersRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (username.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
			// 이메일 형식인 경우
			return usersRepository.findByEmail(username)
				.map(this::createUserDetails)
				.orElseThrow(() -> new UsernameNotFoundException("해당하는 이메일을 찾을 수 없습니다."));
		} else {
			// 소셜 ID로 간주하여 처리
			return usersRepository.findByEmail(username)
				.map(this::createSocialUserDetails)
				.orElseThrow(() -> new UsernameNotFoundException("해당하는 소셜 ID를 찾을 수 없습니다."));
		}
	}

	// 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
	private UserDetails createUserDetails(Member member) {
		return new User(member.getUsername(), member.getPassword(), member.getAuthorities());
	}
	private UserDetails createSocialUserDetails(Member member) {
		return new User(member.getUsername(), member.getPassword(), member.getAuthorities());
	}
}
