package com.API.APIApplication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
		fetchData();
	}
	public static void fetchData() {
		String city = "Indore";
		String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
		String api = "f16929e7663da1362b4e2b1e73ce87af";
		String url = "http://api.weatherstack.com/current?access_key="+api+"&query="+encodedCity;

		try {
			HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();
			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() == 200) {
				parseAndDisplay(response.body());
			} else {
				System.out.println("Error: " + response.statusCode());
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static void parseAndDisplay(String jsonResponse) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(jsonResponse);

		// Extract main data
		String location = rootNode.path("location").path("name").asText();
		String country = rootNode.path("location").path("country").asText();
		String region = rootNode.path("location").path("region").asText();
		String localTime = rootNode.path("location").path("localtime").asText();

		// Current Weather
		JsonNode current = rootNode.path("current");
		String temperature = current.path("temperature").asText();
		String weatherDesc = current.path("weather_descriptions").get(0).asText();
		String windSpeed = current.path("wind_speed").asText();
		String windDir = current.path("wind_dir").asText();
		String humidity = current.path("humidity").asText();
		String pressure = current.path("pressure").asText();
		String visibility = current.path("visibility").asText();
		String uvIndex = current.path("uv_index").asText();
		String isDay = current.path("is_day").asText().equals("yes") ? "Day" : "Night";

		// Air Quality
		JsonNode airQuality = current.path("air_quality");
		String co = airQuality.path("co").asText();
		String no2 = airQuality.path("no2").asText();
		String o3 = airQuality.path("o3").asText();
		String so2 = airQuality.path("so2").asText();
		String pm2_5 = airQuality.path("pm2_5").asText();
		String pm10 = airQuality.path("pm10").asText();
		String epaIndex = airQuality.path("us-epa-index").asText();

		// Astronomy
		JsonNode astro = current.path("astro");
		String sunrise = astro.path("sunrise").asText();
		String sunset = astro.path("sunset").asText();
		String moonPhase = astro.path("moon_phase").asText();

		// Display structured data
		System.out.println("Weather Report:");
		System.out.println("--------------------------");
		System.out.println("Location: " + location + ", " + region + ", " + country);
		System.out.println("Local Time: " + localTime);
		System.out.println("Temperature: " + temperature + "°C");
		System.out.println("Condition: " + weatherDesc);
		System.out.println("Wind: " + windSpeed + " km/h " + windDir);
		System.out.println("Humidity: " + humidity + "%");
		System.out.println("Pressure: " + pressure + " hPa");
		System.out.println("Visibility: " + visibility + " km");
		System.out.println("Day/Night: " + isDay);
		System.out.println("UV Index: " + uvIndex);

		System.out.println("\nAir Quality:");
		System.out.println("CO: " + co + " µg/m³");
		System.out.println("NO2: " + no2 + " µg/m³");
		System.out.println("O3: " + o3 + " µg/m³");
		System.out.println("SO2: " + so2 + " µg/m³");
		System.out.println("PM2.5: " + pm2_5 + " µg/m³");
		System.out.println("PM10: " + pm10 + " µg/m³");
		System.out.println("EPA Index: " + epaIndex);

		System.out.println("\nAstronomy:");
		System.out.println("Sunrise: " + sunrise);
		System.out.println("Sunset: " + sunset);
		System.out.println("Moon Phase: " + moonPhase);
		System.out.println("--------------------------");
	}


}
