package spa.spaserver.global.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spa.spaserver.global.test.domain.TestEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TestRequest {

	private String testProduct;

	private String testPrice;


	public static TestRequest toDto(TestEntity testEntity) {
		TestRequest testRequest = TestRequest.builder()
			.testProduct(testEntity.getTestProduct())
			.testPrice(testEntity.getTestPrice())
			.build();
		return testRequest;
	}
}