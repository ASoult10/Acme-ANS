
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractRealm;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flights.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository customerBookingRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;

		try {
			if (super.getRequest().hasData("id")) {
				Integer flightId = super.getRequest().getData("flight", Integer.class);
				if (flightId == null || flightId != 0) {
					Flight flight = this.customerBookingRepository.findFlightById(flightId);
					status = flight != null && !flight.isDraftMode();
				}
			}
		} catch (Throwable E) {
			status = false;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking = new Booking();

		AbstractRealm principal = super.getRequest().getPrincipal().getActiveRealm();
		int customerId = principal.getId();
		Customer customer = this.customerBookingRepository.findCustomerById(customerId);
		Date date = MomentHelper.getCurrentMoment();

		booking.setCustomer(customer);
		booking.setPurchaseMoment(date);
		booking.setIsPublished(false);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "flight", "locatorCode", "travelClass", "lastNibble");
	}

	@Override
	public void validate(final Booking booking) {
		boolean status = this.customerBookingRepository.findBookingByLocatorCode(booking.getLocatorCode()) == null;
		super.state(status, "locatorCode", "acme.validation.identifier.repeated.message");

		status = booking.getFlight() != null;
		super.state(status, "flight", "acme.validation.noChoice");
	}

	@Override
	public void perform(final Booking booking) {
		this.customerBookingRepository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Collection<Flight> flights = this.customerBookingRepository.findAllFlight();

		Dataset dataset = super.unbindObject(booking, "flight", "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble", "isPublished", "id");
		dataset.put("travelClass", travelClasses);

		SelectChoices flightChoices = null;
		try {
			flightChoices = SelectChoices.from(flights, "flightSummary", booking.getFlight());
		} catch (Throwable e) {

		}

		dataset.put("flights", flightChoices);

		super.getResponse().addData(dataset);
	}

}
