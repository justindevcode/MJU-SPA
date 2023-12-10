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
import spa.spaserver.map.dto.ChargeRequestDto;
import spa.spaserver.map.dto.RouteRequestDto;

@Slf4j
@RequiredArgsConstructor
@Service
public class MapService {

	private static final String REST_API_KEY = "c1d66184b667318643d55ca43edb2511";

	public double[] main(String search) {
		String query = search;
		log.info("프론트입력확인 {}", search);

		try {
			double[] coordinates = getCoordinatesFromKakaoMap(query);
			log.info("서버결과확인 0번값 {}, 1번값", coordinates[0], coordinates[1]);
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

	public double[] chargeMain(ChargeRequestDto chargeRequestDto) {
		try {
			double[] coordinates = getCoordinatesFromKakaoMap(chargeRequestDto.getEndPoint());
			String[] result = calculateMidpoint(chargeRequestDto.getStartY(),
				chargeRequestDto.getStartX(), coordinates[0], coordinates[1]);
			System.out.println("result = " + result[1] +"sdg"+ result[0]);
			double[] chargeResult = getCoordinatesFromKakaoMap("전기차충전소", result);


			if (coordinates != null) {


				return chargeResult;
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
			String encodedQuery = apiUrl + query.replaceAll("\\s", "");

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

	private static double[] getCoordinatesFromKakaoMap(String query, String[] baseLocation) {
		try {
			String[] regionNames = sendKakaoMapRequest(baseLocation[1], baseLocation[0]);

//			String apiUrl =
//				"https://dapi.kakao.com/v2/local/search/keyword.json?y=" + baseLocation[1] + "&x="
//					+ baseLocation[0] + "&sort=distance&radius=" + baseLocation[2] + "&query=";
			String apiUrl =
				"https://dapi.kakao.com/v2/local/search/keyword.json?query="+regionNames[0]+regionNames[1]+"에있는";
			String encodedQuery = apiUrl + query;
			System.out.println("encodedQuery = " + encodedQuery);

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

	public static String[] calculateMidpoint(String startLat, String startLng, double endLat, double endLng) {
		System.out.println(
			"startLat = " + startLat + ", startLng = " + startLng + ", endLat = " + endLat
				+ ", endLng = " + endLng);
		double startLatDouble = Double.parseDouble(startLat);
		double startLngDouble = Double.parseDouble(startLng);
		double endLatDouble = endLat;
		double endLngDouble = endLng;

		double midLat = (startLatDouble + endLatDouble) / 2;
		double midLng;

		if (Math.abs(startLngDouble - endLngDouble) < 180) {
			midLng = (startLngDouble + endLngDouble) / 2;
		} else {
			midLng = (startLngDouble + endLngDouble + 360) / 2 - 360;
		}

		double distance = haversine(startLatDouble, startLngDouble, endLat, endLng);
		int distanceInt = (int) Math.floor(distance);
		return new String[]{String.valueOf(midLat), String.valueOf(midLng),String.valueOf(distanceInt)};
	}
	private static double haversine(double lat1, double lng1, double lat2, double lng2) {
		final int R = 6371; // 지구 반지름 (단위: km)

		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
			Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
				Math.sin(dLng / 2) * Math.sin(dLng / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return R * c * 1000; // 거리를 미터로 변환
	}

	public static String[] sendKakaoMapRequest(String latitude, String longitude) {
		try {
			String apiUrl = "https://dapi.kakao.com/v2/local/geo/coord2address.json?y="+longitude+"&x="+latitude;

			URI uri = new URI(apiUrl);
			HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.header("Authorization", "KakaoAK " + REST_API_KEY)
				.GET()
				.build();

			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			return parseKakaoMapResponse(response.body());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new String[0];
	}

	public static String[] parseKakaoMapResponse(String response) {
		JSONObject jsonResponse = new JSONObject(response);
		System.out.println(jsonResponse);
		JSONArray documents = jsonResponse.getJSONArray("documents");

		if (documents.length() > 0) {
			JSONObject firstResult = documents.getJSONObject(0);

			JSONObject roadAddress = firstResult.getJSONObject("address");
			String region1DepthName = roadAddress.getString("region_1depth_name").replaceAll("\\s", "");
			String region2DepthName = roadAddress.getString("region_2depth_name").replaceAll("\\s", "");
			System.out.println("region2DepthName = " + region2DepthName);
			System.out.println("region1DepthName = " + region1DepthName);

			return new String[]{region1DepthName, region2DepthName};
		}

		return new String[0];
	}

	public String routePrint(RouteRequestDto routeRequestDto) {
		StringBuilder stringBuilder = new StringBuilder();

		if (routeRequestDto.getBattery() <= 25) {
			stringBuilder.append("[stop 1] \n");
			stringBuilder.append("출발: 명지대학교 \n");
			stringBuilder.append("도착: 명지대학교 충전소 \n");
			stringBuilder.append("이동거리: 0.00km \n");
			stringBuilder.append("소요시간: " + (double)routeRequestDto.getBattery()/100*1.2 +"시간 \n");
			stringBuilder.append("\n");
			stringBuilder.append("[stop 2] \n");
			stringBuilder.append("출발: 명지대학교 충전소 \n");
			stringBuilder.append("도착: " + routeRequestDto.getLocation1() + "\n");
			stringBuilder.append("이동거리: " + routeRequestDto.getTime1()*42 +"Km \n");
			stringBuilder.append("소요시간: " + routeRequestDto.getTime1() + "시간 \n");
			stringBuilder.append("\n");
			stringBuilder.append("[stop 3] \n");
			stringBuilder.append("출발: " + routeRequestDto.getLocation1() + "\n");
			stringBuilder.append("도착: " + routeRequestDto.getLocation2() + "\n");
			stringBuilder.append("이동거리: " + routeRequestDto.getTime2()*45 +"Km \n");
			stringBuilder.append("소요시간: " + routeRequestDto.getTime2() + "시간 \n");
			stringBuilder.append("\n");
			stringBuilder.append("[stop 4] \n");
			stringBuilder.append("출발: " + routeRequestDto.getLocation2() + "\n");
			stringBuilder.append("도착: " + routeRequestDto.getLocation3() + "\n");
			stringBuilder.append("이동거리: " + routeRequestDto.getTime3()*45 +"Km \n");
			stringBuilder.append("소요시간: " + routeRequestDto.getTime3() + "시간 \n");
			stringBuilder.append("\n");
			stringBuilder.append("[경로 종합 정보] \n");
			stringBuilder.append(
				"총 이동거리:" + (routeRequestDto.getTime1() * 42 + routeRequestDto.getTime2() * 45
					+ routeRequestDto.getTime3() * 45) + "km \n");
			stringBuilder.append(
				"총 소요시간: " + Math.round((routeRequestDto.getTime1() + routeRequestDto.getTime2()
					+ routeRequestDto.getTime3() + ((double) routeRequestDto.getBattery() / 100 * 1.2))*100.0)/100.0
					+ "시간 경유시간 포함 \n");
			stringBuilder.append("\n");
			stringBuilder.append(
				"최종점수 : " + (routeRequestDto.getTime1() * 42 + routeRequestDto.getTime2() * 45
					+ routeRequestDto.getTime3() * 45) * (-1100)
					+ Math.round((routeRequestDto.getTime1() + routeRequestDto.getTime2()
					+ routeRequestDto.getTime3()
					+ (((double) routeRequestDto.getBattery() / 100 * 1.2)) * 100.0) / 100.0)
					* (-2500) + 80 * (500) + "\n");

			String result = stringBuilder.toString();
			System.out.println(result);
			return result;
		} else if (routeRequestDto.getBattery() <= 50) {
			stringBuilder.append("[stop 1] \n");
			stringBuilder.append("출발: 명지대학교 충전소 \n");
			stringBuilder.append("도착: " + routeRequestDto.getLocation1() + "\n");
			stringBuilder.append("이동거리: " + routeRequestDto.getTime1()*42 +"Km \n");
			stringBuilder.append("소요시간: " + routeRequestDto.getTime1() + "시간 \n");
			stringBuilder.append("\n");
			stringBuilder.append("[stop 2] \n");
			stringBuilder.append("출발: " + routeRequestDto.getLocation1() + " \n");
			stringBuilder.append("도착: " + routeRequestDto.getLocation1() + " 충전소 \n");
			stringBuilder.append("이동거리: 0.00km \n");
			stringBuilder.append("소요시간: " + (double)(routeRequestDto.getBattery()-20)/100*1.2 + "시간 \n");
			stringBuilder.append("\n");
			stringBuilder.append("[stop 3] \n");
			stringBuilder.append("출발: " + routeRequestDto.getLocation1() + "\n");
			stringBuilder.append("도착: " + routeRequestDto.getLocation2() + "\n");
			stringBuilder.append("이동거리: " + routeRequestDto.getTime2()*45 +"Km \n");
			stringBuilder.append("소요시간: " + routeRequestDto.getTime2() + "시간 \n");
			stringBuilder.append("\n");
			stringBuilder.append("[stop 4] \n");
			stringBuilder.append("출발: " + routeRequestDto.getLocation2() + "\n");
			stringBuilder.append("도착: " + routeRequestDto.getLocation3() + "\n");
			stringBuilder.append("이동거리: " + routeRequestDto.getTime3()*45 +"Km \n");
			stringBuilder.append("소요시간: " + routeRequestDto.getTime3() + "시간 \n");
			stringBuilder.append("\n");
			stringBuilder.append("[경로 종합 정보] \n");
			stringBuilder.append(
				"총 이동거리:" + (routeRequestDto.getTime1() * 42 + routeRequestDto.getTime2() * 45
					+ routeRequestDto.getTime3() * 45) + "km \n");
			stringBuilder.append(
				"총 소요시간: " + Math.round((routeRequestDto.getTime1() + routeRequestDto.getTime2()
					+ routeRequestDto.getTime3() + (((double) routeRequestDto.getBattery()-20) / 100 * 1.2))*100.0)/100.0
					+ "시간 경유시간 포함 \n");
			stringBuilder.append("\n");
			stringBuilder.append(
				"최종점수 : " + (routeRequestDto.getTime1() * 42 + routeRequestDto.getTime2() * 45
					+ routeRequestDto.getTime3() * 45) * (-1100)
					+ Math.round((routeRequestDto.getTime1() + routeRequestDto.getTime2()
					+ routeRequestDto.getTime3()
					+ (((double) (routeRequestDto.getBattery()-20) / 100 * 1.2)) * 100.0) / 100.0)
					* (-2500) + 80 * (500) + "\n");

			String result = stringBuilder.toString();
			System.out.println(result);
			return result;
		} else if (routeRequestDto.getBattery() <= 75) {
			stringBuilder.append("[stop 1] \n");
			stringBuilder.append("출발: 명지대학교 충전소 \n");
			stringBuilder.append("도착: " + routeRequestDto.getLocation1() + "\n");
			stringBuilder.append("이동거리: " + routeRequestDto.getTime1()*42 +"Km \n");
			stringBuilder.append("소요시간: " + routeRequestDto.getTime1() + "시간 \n");
			stringBuilder.append("\n");
			stringBuilder.append("[stop 2] \n");
			stringBuilder.append("출발: " + routeRequestDto.getLocation1() + "\n");
			stringBuilder.append("도착: " + routeRequestDto.getLocation2() + "\n");
			stringBuilder.append("이동거리: " + routeRequestDto.getTime2()*45 +"Km \n");
			stringBuilder.append("소요시간: " + routeRequestDto.getTime2() + "시간 \n");
			stringBuilder.append("\n");
			stringBuilder.append("[stop 3] \n");
			stringBuilder.append("출발: " + routeRequestDto.getLocation2() + " \n");
			stringBuilder.append("도착: " + routeRequestDto.getLocation2() + " 충전소 \n");
			stringBuilder.append("이동거리: 0.00km \n");
			stringBuilder.append("소요시간: " + (double)(routeRequestDto.getBattery()-50)/100*1.2 + "시간 \n");
			stringBuilder.append("\n");
			stringBuilder.append("[stop 4] \n");
			stringBuilder.append("출발: " + routeRequestDto.getLocation2() + "\n");
			stringBuilder.append("도착: " + routeRequestDto.getLocation3() + "\n");
			stringBuilder.append("이동거리: " + routeRequestDto.getTime3()*45 +"Km \n");
			stringBuilder.append("소요시간: " + routeRequestDto.getTime3() + "시간 \n");
			stringBuilder.append("\n");
			stringBuilder.append("[경로 종합 정보] \n");
			stringBuilder.append(
				"총 이동거리:" + (routeRequestDto.getTime1() * 42 + routeRequestDto.getTime2() * 45
					+ routeRequestDto.getTime3() * 45) + "km \n");
			stringBuilder.append(
				"총 소요시간: " + Math.round((routeRequestDto.getTime1() + routeRequestDto.getTime2()
					+ routeRequestDto.getTime3() + (((double) routeRequestDto.getBattery()-50) / 100 * 1.2))*100.0)/100.0
					+ "시간 경유시간 포함 \n");
			stringBuilder.append("\n");
			stringBuilder.append(
				"최종점수 : " + (routeRequestDto.getTime1() * 42 + routeRequestDto.getTime2() * 45
					+ routeRequestDto.getTime3() * 45) * (-1100)
					+ Math.round((routeRequestDto.getTime1() + routeRequestDto.getTime2()
					+ routeRequestDto.getTime3()
					+ (((double) (routeRequestDto.getBattery()-50) / 100 * 1.2)) * 100.0) / 100.0)
					* (-2500) + 80 * (500) + "\n");

			String result = stringBuilder.toString();
			System.out.println(result);
			return result;
		} else if (routeRequestDto.getBattery() <= 100) {
			stringBuilder.append("[stop 1] \n");
			stringBuilder.append("출발: 명지대학교 충전소 \n");
			stringBuilder.append("도착: " + routeRequestDto.getLocation1() + "\n");
			stringBuilder.append("이동거리: " + routeRequestDto.getTime1()*42 +"Km \n");
			stringBuilder.append("소요시간: " + routeRequestDto.getTime1() + "시간 \n");
			stringBuilder.append("\n");
			stringBuilder.append("[stop 2] \n");
			stringBuilder.append("출발: " + routeRequestDto.getLocation1() + "\n");
			stringBuilder.append("도착: " + routeRequestDto.getLocation2() + "\n");
			stringBuilder.append("이동거리: " + routeRequestDto.getTime2()*45 +"Km \n");
			stringBuilder.append("소요시간: " + routeRequestDto.getTime2() + "시간 \n");
			stringBuilder.append("\n");
			stringBuilder.append("[stop 3] \n");
			stringBuilder.append("출발: " + routeRequestDto.getLocation2() + "\n");
			stringBuilder.append("도착: " + routeRequestDto.getLocation3() + "\n");
			stringBuilder.append("이동거리: " + routeRequestDto.getTime3()*45 +"Km \n");
			stringBuilder.append("소요시간: " + routeRequestDto.getTime3() + "시간 \n");
			stringBuilder.append("\n");
			stringBuilder.append("[stop 4] \n");
			stringBuilder.append("출발: " + routeRequestDto.getLocation3() + " \n");
			stringBuilder.append("도착: " + routeRequestDto.getLocation3() + " 충전소 \n");
			stringBuilder.append("이동거리: 0.00km \n");
			stringBuilder.append("소요시간: " + (double)(routeRequestDto.getBattery()-75)/100*1.2 + "시간 \n");
			stringBuilder.append("\n");
			stringBuilder.append("[경로 종합 정보] \n");
			stringBuilder.append(
				"총 이동거리:" + (routeRequestDto.getTime1() * 42 + routeRequestDto.getTime2() * 45
					+ routeRequestDto.getTime3() * 45) + "km \n");
			stringBuilder.append(
				"총 소요시간: " + Math.round((routeRequestDto.getTime1() + routeRequestDto.getTime2()
					+ routeRequestDto.getTime3() + (((double) routeRequestDto.getBattery()-75) / 100 * 1.2))*100.0)/100.0
					+ "시간 경유시간 포함 \n");
			stringBuilder.append("\n");
			stringBuilder.append(
				"최종점수 : " + (routeRequestDto.getTime1() * 42 + routeRequestDto.getTime2() * 45
					+ routeRequestDto.getTime3() * 45) * (-1100)
					+ Math.round((routeRequestDto.getTime1() + routeRequestDto.getTime2()
					+ routeRequestDto.getTime3()
					+ (((double) (routeRequestDto.getBattery()-75) / 100 * 1.2)) * 100.0) / 100.0)
					* (-2500) + 80 * (500) + "\n");

			String result = stringBuilder.toString();
			System.out.println(result);
			return result;
		}
		return null;
	}

}
