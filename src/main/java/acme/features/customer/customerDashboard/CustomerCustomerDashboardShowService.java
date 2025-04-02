
package acme.features.customer.customerDashboard;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.forms.customers.CustomerDashboard;
import acme.realms.Customer;

@GuiService
public class CustomerCustomerDashboardShowService extends AbstractGuiService<Customer, CustomerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerCustomerDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Integer userAccountId = this.getRequest().getPrincipal().getAccountId();

		CustomerDashboard dashboard = new CustomerDashboard();

		Collection<String> lastFiveDestinations = this.repository.lastFiveDestinations(userAccountId).stream().map(Flight::getDestinationCity).toList();
		Money spentMoney = this.repository.spentMoney(userAccountId);
		Integer economyBookings = this.repository.economyBookings(userAccountId);
		Integer businessBookings = this.repository.businessBookings(userAccountId);
		Money bookingTotalCost = this.repository.bookingTotalCost(userAccountId);
		Money bookingAverageCost = this.repository.bookingAverageCost(userAccountId);
		Money bookingMinimumCost = this.repository.bookingMinimumCost(userAccountId);
		Money bookingMaximumCost = this.repository.bookingMaximumCost(userAccountId);
		Money bookingDeviationCost = this.repository.bookingDeviationCost(userAccountId);
		Integer bookingTotalPassengers = this.repository.bookingTotalPassengers(userAccountId);
		Double bookingAveragePassengers = this.repository.bookingAveragePassengers(userAccountId);
		Integer bookingMinimumPassengers = this.repository.bookingMinimumPassengers(userAccountId);
		Integer bookingMaximumPassengers = this.repository.bookingMaximumPassengers(userAccountId);
		Double bookingDeviationPassengers = this.repository.bookingDeviationPassengers(userAccountId);

		dashboard.setLastFiveDestinations(lastFiveDestinations);
		dashboard.setSpentMoneyLastYear(spentMoney);
		dashboard.setEconomyBookings(economyBookings);
		dashboard.setBusinessBookings(businessBookings);
		dashboard.setBookingCountCost(bookingTotalCost);
		dashboard.setBookingAverageCost(bookingAverageCost);
		dashboard.setBookingMinimumCost(bookingMinimumCost);
		dashboard.setBookingMaximumCost(bookingMaximumCost);
		dashboard.setBookingDeviationCost(bookingDeviationCost);
		dashboard.setBookingCountPassengers(bookingTotalPassengers);
		dashboard.setBookingAveragePassengers(bookingAveragePassengers);
		dashboard.setBookingMinimumPassengers(bookingMinimumPassengers);
		dashboard.setBookingMaximumPassengers(bookingMaximumPassengers);
		dashboard.setBookingDeviationPassengers(bookingDeviationPassengers);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final CustomerDashboard object) {
		Dataset dataset = super.unbindObject(object, //
			"lastFiveDestinations", "spentMoneyLastYear", // 
			"economyBookings", "businessBookings", //
			"bookingCountCost", "bookingAverageCost", //
			"bookingMinimumCost", "bookingMaximumCost", //
			"bookingDeviationCost", "bookingCountPassengers", //
			"bookingAveragePassengers", "bookingMinimumPassengers", //
			"bookingMaximumPassengers", "bookingDeviationPassengers");

		super.getResponse().addData(dataset);
	}

}
