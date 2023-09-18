package spa.spaserver.global.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spa.spaserver.global.test.domain.TestEntity;

public interface TestRepository extends JpaRepository<TestEntity, Long> {

}