package spa.spaserver.map.service;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MapService {

	private static final String REST_API_KEY = "c1d66184b667318643d55ca43edb2511";

	public double[] main(String search) {
		String query = search;

		try {
			double[] coordinates = getCoordinatesFromKakaoMap(query);
			if (coordinates != null) {
				System.out.println("위도: " + coordinates[0]);
				System.out.println("경도: " + coordinates[1]);
				return coordinates;
			} else {
				System.out.println("좌표를 찾을 수 없습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static double[] getCoordinatesFromKakaoMap(String query) {
		try {
			String apiUrl = "https://dapi.kakao.com/v2/local/search/keyword.json?y=37.222451765&x=127.187139999&radius=20000&query=";
			String encodedQuery = apiUrl + query;

			URI uri = new URI(encodedQuery);
			HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.header("Authorization", "KakaoAK " + REST_API_KEY)
				.GET()
				.build();



			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			// JSON 파싱
			JSONObject jsonResponse = new JSONObject(response.body());
			JSONArray documents = jsonResponse.getJSONArray("documents");
			System.out.println("이거맞나?????" +response.body());


			// 첫 번째 검색 결과에서 x와 y 값을 추출
			if (documents.length() > 0) {
				JSONObject firstResult = documents.getJSONObject(0);
				String x = firstResult.getString("x");
				System.out.println(x + "x좌표값");
				String y = firstResult.getString("y");

				// String 값을 double로 변환하여 배열에 담아 리턴
				return new double[]{Double.parseDouble(y), Double.parseDouble(x)};
			}
		} catch (IOException | URISyntaxException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

}
