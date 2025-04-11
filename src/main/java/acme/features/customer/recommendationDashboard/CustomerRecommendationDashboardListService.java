
package acme.features.customer.recommendationDashboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.forms.recommendations.RecommendationDashboard;
import acme.forms.recommendations.RecommendationType;
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
		RecommendationDashboard recommendationDashboard = new RecommendationDashboard();
		recommendationDashboard.setCity("Sevilla");
		recommendationDashboard.setCountry("España");
		recommendationDashboard.setName("Hola");
		recommendationDashboard.setDescription("Adios");
		recommendationDashboard.setRecommendationType(RecommendationType.ACTIVITY);
		super.getBuffer().addData(List.of(recommendationDashboard));
	}

	@Override
	public void unbind(final RecommendationDashboard recommendationsDashboard) {
		Dataset dataset = super.unbindObject(recommendationsDashboard, "city", "country", "name", "description", "recommendationType");
		super.getResponse().addData(dataset);
	}

}
