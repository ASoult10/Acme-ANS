
package acme.features.customer.booking;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.flights.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingUpdateService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository customerBookingRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Customer object;
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		object = this.customerBookingRepository.findCustomerByUserAccountId(userAccountId);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Booking booking) {
		Integer flightId = super.getRequest().getData("flight", int.class);
		Flight flight = this.customerBookingRepository.findFlightById(flightId);

		Integer customerId = super.getRequest().getData("customer", int.class);
		Customer customer = this.customerBookingRepository.findCustomerById(customerId);

		booking.setFlight(flight);
		booking.setCustomer(customer);

		super.bindObject(booking, "flight", "customer", "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble");
	}

	@Override
	public void validate(final Booking booking) {
		// TODO: Solo se pueden actualizar al ser publicados
	}

	@Override
	public void perform(final Booking booking) {
		this.customerBookingRepository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		assert booking != null;

		Dataset dataset;

		dataset = super.unbindObject(booking, "flight", "customer", "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble");
		super.getResponse().addData(dataset);
	}

}
