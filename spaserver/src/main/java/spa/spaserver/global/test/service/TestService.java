package spa.spaserver.global.test.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spa.spaserver.global.test.domain.TestEntity;
import spa.spaserver.global.test.dto.TestRequest;
import spa.spaserver.global.test.dto.TestResponse;
import spa.spaserver.global.test.repository.TestRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestService {

	private final TestRepository testRepository;

	@Transactional
	public TestResponse save(TestRequest testRequest) {
		TestEntity testEntity = new TestEntity(testRequest.getTestProduct(),
			testRequest.getTestPrice());
		testRepository.save(testEntity);
		return TestResponse.toDto(testEntity);
	}

	@Transactional
	public List<TestResponse> getTestAll() {
		List<TestResponse> testResponseList = new ArrayList<>();
		List<TestEntity> testEntityList = testRepository.findAll();
		for (TestEntity testEntity : testEntityList) {
			testResponseList.add(TestResponse.toDto(testEntity));
		}
		return testResponseList;
	}

	@Transactional
	public String deleteAllTest() {
		testRepository.deleteAll();
		return "success deleteAllTest";
	}

}
