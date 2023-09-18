package spa.spaserver.global.test.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spa.spaserver.global.test.domain.TestEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TestResponse {

	private String testProduct;

	private String testPrice;

	private LocalDateTime getCreatedAt;

	private LocalDateTime getUpdateAt;

	public static TestResponse toDto(TestEntity testEntity) {
		TestResponse testResponse = TestResponse.builder()
			.testProduct(testEntity.getTestProduct())
			.testPrice(testEntity.getTestPrice())
			.getCreatedAt(testEntity.getCreatedAt())
			.getUpdateAt(testEntity.getUpdateAt())
			.build();
		return testResponse;
	}
}
