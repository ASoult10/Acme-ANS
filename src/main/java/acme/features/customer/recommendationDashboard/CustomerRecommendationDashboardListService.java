
package acme.features.customer.recommendationDashboard;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.recommendationForCustomer.RecommendationForCustomer;
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

		List<RecommendationForCustomer> recommendationsForCustomer = this.customerRecommendationDashboardRepository.findRecommendationsByCityAndCountry(city, country);

		for (RecommendationForCustomer rec : recommendationsForCustomer) {
			RecommendationDashboard recDash = new RecommendationDashboard();
			recDash.setCity(rec.getCity());
			recDash.setCountry(rec.getCountry());
			recDash.setName(rec.getName());
			recDash.setDescription(rec.getCountry());
			recommendationDashboards.add(recDash);
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
