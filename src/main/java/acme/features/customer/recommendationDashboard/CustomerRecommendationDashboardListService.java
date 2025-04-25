
package acme.features.customer.recommendationDashboard;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.forms.recommendations.RecommendationDashboard;
import acme.realms.Customer;

@GuiService
public class CustomerRecommendationDashboardListService extends AbstractGuiService<Customer, RecommendationDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerRecommendationDashboardRepository customerRecommendationDashboardRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		List<RecommendationDashboard> recommendationDashboards = new ArrayList<>();

		String city = super.getRequest().getData("city", String.class);
		String country = super.getRequest().getData("country", String.class);
		String apiKey = "kv47Ard-jTcCTDgXI6mCtoZUIR3X_TTg2-DtAv5paENfz_WZvFiwn8zH2LNZZ-lTcZJtVyGAU5btj4KA5oWEbcAplnoeQ1qzRQtzMbm_eSaJlR6mThHF9x9HbCD5Z3Yx";
		String term = "tourist";
		Integer count = 5;

		try {
			String url = "https://api.yelp.com/v3/businesses/search?location={0}&term={1}&limit={2}";

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + apiKey);
			HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, city, term, count);

			assert response != null && response.getBody() != null;

			JSONObject contJSON = new JSONObject(response.getBody());
			JSONArray businesses = contJSON.getJSONArray("businesses");

			for (int i = 0; i < count && i < businesses.length(); i++) {
				JSONObject iObj = businesses.getJSONObject(i);
				RecommendationDashboard recommendationDashboard = new RecommendationDashboard();
				recommendationDashboard.setCity(city);
				recommendationDashboard.setCountry(country);
				recommendationDashboard.setName(iObj.getString("name"));
				JSONArray categoryArray = iObj.getJSONArray("categories");
				recommendationDashboard.setRecommendationType(categoryArray.getJSONObject(0).getString("title"));
				recommendationDashboards.add(recommendationDashboard);
			}

		} catch (final Exception e) {
			e.printStackTrace();
			return;
		}

		super.getBuffer().addData(recommendationDashboards);
	}

	@Override
	public void unbind(final RecommendationDashboard recommendationsDashboard) {
		Dataset dataset = super.unbindObject(recommendationsDashboard, "city", "country", "name", "description", "recommendationType");
		dataset.put("cityName", recommendationsDashboard.getCity());
		dataset.put("countryName", recommendationsDashboard.getCountry());
		super.getResponse().addData(dataset);
	}

}
