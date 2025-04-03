
package acme.features.customer.customerDashboard;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.forms.customers.CustomerDashboard;
import acme.realms.Customer;

@GuiService
public class CustomerCustomerDashboardShowService extends AbstractGuiService<Customer, CustomerDashboard> {

	// Internal state ----------------------------

	@Autowired
	private CustomerCustomerDashboardRepository repository;

	// AbstractGuiService interface --------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void load() {
		Integer customerId = this.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Booking> bookings = this.repository.findAllBookingsOf(customerId);
		String currency = bookings.stream().findFirst().get().getPrice().getCurrency();
		Integer thisYear = MomentHelper.getCurrentMoment().getYear();
		List<Booking> lastFiveYearsBookings = bookings.stream().filter(booking -> booking.getPurchaseMoment().getYear() > thisYear - 5).toList();
		Integer total5YearsBookings = lastFiveYearsBookings.size() > 1 ? lastFiveYearsBookings.size() : 1;
		CustomerDashboard dashboard = new CustomerDashboard();
		Collection<String> last5destinations = bookings.stream().map(b -> b.getFlight().getDestinationCity()).distinct().limit(5).toList();

		dashboard.setLastFiveDestinations((List<String>) last5destinations);
		Double totalMoney = bookings.stream().filter(booking -> booking.getPurchaseMoment().getYear() > thisYear - 1).map(Booking::getPrice).map(Money::getAmount).reduce(0.0, Double::sum);
		Money spentMoney = new Money();
		spentMoney.setAmount(totalMoney != null ? totalMoney : 0.0);

		spentMoney.setCurrency(currency);
		dashboard.setSpentMoneyLastYear(spentMoney);

		Integer economyBookings = (int) bookings.stream().filter(b -> b.getTravelClass().equals(TravelClass.ECONOMY)).count();
		dashboard.setEconomyBookings(economyBookings);

		Integer businessBookings = (int) bookings.stream().filter(b -> b.getTravelClass().equals(TravelClass.BUSINESS)).count();
		dashboard.setBusinessBookings(businessBookings);

		Money bookingTotalCost = new Money();
		bookingTotalCost.setAmount(lastFiveYearsBookings.stream().map(Booking::getPrice).map(Money::getAmount).reduce(0.0, Double::sum));
		bookingTotalCost.setCurrency(currency);
		dashboard.setBookingCountCost(bookingTotalCost);

		Money bookingAverageCost = new Money();
		bookingAverageCost.setAmount(bookingTotalCost.getAmount() / total5YearsBookings);
		bookingAverageCost.setCurrency(currency);
		dashboard.setBookingAverageCost(bookingAverageCost);

		Money bookingMinimumCost = new Money();
		bookingMinimumCost.setAmount(lastFiveYearsBookings.stream().map(Booking::getPrice).map(Money::getAmount).min(Double::compare).orElse(0.0));
		bookingMinimumCost.setCurrency(currency);
		dashboard.setBookingMinimumCost(bookingMinimumCost);

		Money bookingMaximumCost = new Money();
		bookingMaximumCost.setAmount(lastFiveYearsBookings.stream().map(Booking::getPrice).map(Money::getAmount).max(Double::compare).orElse(0.0));
		bookingMaximumCost.setCurrency(currency);
		dashboard.setBookingMaximumCost(bookingMaximumCost);

		Money bookingDeviationCost = new Money();
		double varianza = lastFiveYearsBookings.stream().map(Booking::getPrice).map(Money::getAmount).map(price -> Math.pow(price - bookingAverageCost.getAmount(), 2)).reduce(0.0, Double::sum) / total5YearsBookings;
		double deviation = Math.sqrt(varianza);
		bookingDeviationCost.setAmount(deviation);
		bookingDeviationCost.setCurrency(currency);
		dashboard.setBookingDeviationCost(bookingDeviationCost);

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
