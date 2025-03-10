
package acme.features.authenticated.customer.dashboard;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.forms.CustomerDashboard;
import acme.realms.Customer;

@GuiService
public class CustomerDashboardService extends AbstractGuiService<Customer, CustomerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Integer userAccountId = this.getRequest().getPrincipal().getAccountId();

		CustomerDashboard dashboard = new CustomerDashboard();

		//		List<String> lastFiveDestinations = this.repository.lastFiveDestinations(userAccountId);
		//		Money spentMoney = this.repository.spentMoney(userAccountId);
		//		Integer economyBookings = this.repository.economyBookings(userAccountId);
		//		Integer businessBookings = this.repository.businessBookings(userAccountId);
		//		Money bookingTotalCost = this.repository.bookingTotalCost(userAccountId);
		//		Money bookingAverageCost = this.repository.bookingAverageCost(userAccountId);
		//		Money bookingMinimumCost = this.repository.bookingMinimumCost(userAccountId);
		//		Money bookingMaximumCost = this.repository.bookingMaximumCost(userAccountId);
		//		Money bookingDeviationCost = this.repository.bookingDeviationCost(userAccountId);
		//		Integer bookingTotalPassengers = this.repository.bookingTotalPassengers(userAccountId);
		//		Double bookingAveragePassengers = this.repository.bookingAveragePassengers(userAccountId);
		//		Integer bookingMinimumPassengers = this.repository.bookingMinimumPassengers(userAccountId);
		//		Integer bookingMaximumPassengers = this.repository.bookingMaximumPassengers(userAccountId);
		//		Double bookingDeviationPassengers = this.repository.bookingDeviationPassengers(userAccountId);
		//
		//		dashboard.setLastFiveDestinations(lastFiveDestinations);
		//		dashboard.setSpentMoney(spentMoney);
		//		dashboard.setEconomyBookings(economyBookings);
		//		dashboard.setBusinessBookings(businessBookings);
		//		dashboard.setBookingTotalCost(bookingTotalCost);
		//		dashboard.setBookingAverageCost(bookingAverageCost);
		//		dashboard.setBookingMinimumCost(bookingMinimumCost);
		//		dashboard.setBookingMaximumCost(bookingMaximumCost);
		//		dashboard.setBookingDeviationCost(bookingDeviationCost);
		//		dashboard.setBookingTotalPassengers(bookingTotalPassengers);
		//		dashboard.setBookingAveragePassengers(bookingAveragePassengers);
		//		dashboard.setBookingMinimumPassengers(bookingMinimumPassengers);
		//		dashboard.setBookingMaximumPassengers(bookingMaximumPassengers);
		//		dashboard.setBookingDeviationPassengers(bookingDeviationPassengers);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final CustomerDashboard object) {
		Dataset dataset;

		dataset = super.unbindObject(object, //
			"lastFiveDestinations", "spentMoney", // 
			"economyBookings", "businessBookings", //
			"bookingTotalCost", "bookingAverageCost", //
			"bookingMinimumCost", "bookingMaximumCost", //
			"bookingDeviationCost", "bookingTotalPassengers", //
			"bookingAveragePassengers", "bookingMinimumPassengers", //
			"bookingMaximumPassengers", "bookingDeviationPassengers");

		super.getResponse().addData(dataset);
	}

}
