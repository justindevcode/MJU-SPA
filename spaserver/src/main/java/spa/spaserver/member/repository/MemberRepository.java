package spa.spaserver.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import spa.spaserver.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByEmail(String email);
	boolean existsByEmail(String email);
}
