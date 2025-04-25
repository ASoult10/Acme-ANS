
package acme.features.administrator.recommendationForCustomer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airports.Airport;
import acme.entities.recommendationForCustomer.RecommendationForCustomer;

@GuiService
public class AdministratorRecommendationForCustomerCreateService extends AbstractGuiService<Administrator, RecommendationForCustomer> {

	final String												apiKey	= "";

	@Autowired
	private AdministratorRecommendationForCustomerRepository	repository;


	public List<RecommendationForCustomer> loadRecommendationAPI() {

		this.repository.deleteAllRecommendations();

		List<RecommendationForCustomer> recommendations = new ArrayList<>();

		List<Airport> airports = this.repository.findAllAirports();

		List<PairCityCountry> uniquePairs = airports.stream().map(a -> new PairCityCountry(a.getCity(), a.getCountry())).distinct().collect(Collectors.toList());

		for (PairCityCountry pair : uniquePairs) {
			String city = pair.city();
			String country = pair.country();
			String term = "tourist";
			Integer count = 5;

			try {
				String url = "https://api.yelp.com/v3/businesses/search?location={0}&term={1}&limit={2}";

				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + this.apiKey);
				HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

				RestTemplate restTemplate = new RestTemplate();
				ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, city, term, count);

				if (response != null && response.getBody() != null) {

					JSONObject contJSON = new JSONObject(response.getBody());
					JSONArray businesses = contJSON.getJSONArray("businesses");

					for (int i = 0; i < count && i < businesses.length(); i++) {
						JSONObject iObj = businesses.getJSONObject(i);
						RecommendationForCustomer recommendationDashboard = new RecommendationForCustomer();
						recommendationDashboard.setCity(city);
						recommendationDashboard.setCountry(country);
						recommendationDashboard.setName(iObj.getString("name"));
						JSONArray categoryArray = iObj.getJSONArray("categories");
						recommendationDashboard.setRecommendationType(categoryArray.getJSONObject(0).getString("title"));
						recommendations.add(recommendationDashboard);
					}
				}

			} catch (final Exception e) {
				System.out.println("Recomendaciones en ciudad " + city + " y país " + country + " no encontradas.");
			}
		}

		this.repository.saveAll(recommendations);
		return recommendations;
	}

}
