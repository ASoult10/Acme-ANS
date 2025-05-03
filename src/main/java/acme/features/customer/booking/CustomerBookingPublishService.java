
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingPassenger;
import acme.entities.booking.TravelClass;
import acme.entities.flights.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingPublishService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository customerBookingRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		Integer bookingId = super.getRequest().getData("id", int.class);
		Booking booking = this.customerBookingRepository.findBookingById(bookingId);

		status = status && booking != null;

		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		status = status && booking.getCustomer().getId() == customerId && !booking.getIsPublished();

		Integer flightId = super.getRequest().getData("flight", Integer.class);
		if (flightId == null || flightId != 0) {
			Flight flight = this.customerBookingRepository.findFlightById(flightId);
			status = status && flight != null && !flight.isDraftMode();
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Integer id = super.getRequest().getData("id", int.class);
		Booking booking = this.customerBookingRepository.findBookingById(id);
		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "flight", "locatorCode", "travelClass", "lastNibble");
	}

	@Override
	public void validate(final Booking booking) {
		Booking bookingWithSameLocatorCode = this.customerBookingRepository.findBookingByLocatorCode(booking.getLocatorCode());
		boolean status = bookingWithSameLocatorCode == null || bookingWithSameLocatorCode.getId() == booking.getId();
		super.state(status, "locatorCode", "acme.validation.identifier.repeated.message");

		Collection<BookingPassenger> bookingPassengers = this.customerBookingRepository.findAllBookingPassengersByBookingId(booking.getId());
		status = !bookingPassengers.isEmpty();
		super.state(status, "*", "customer.validation.booking.form.error.noPassengers");

		status = bookingPassengers.stream().filter(br -> !br.getPassenger().getIsPublished()).findFirst().isEmpty();
		super.state(status, "*", "customer.booking.form.error.publishPassengers");

		status = booking.getLastNibble() != null && !booking.getLastNibble().isBlank();
		super.state(status, "lastNibble", "acme.validation.lastNibble.blank.message");
	}

	@Override
	public void perform(final Booking booking) {
		booking.setIsPublished(true);
		this.customerBookingRepository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Collection<Flight> flights = this.customerBookingRepository.findAllFlight();

		Dataset dataset = super.unbindObject(booking, "flight", "customer", "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble", "isPublished", "id");
		dataset.put("travelClass", travelClasses);

		if (!flights.isEmpty()) {
			SelectChoices flightChoices = SelectChoices.from(flights, "flightSummary", booking.getFlight());
			dataset.put("flights", flightChoices);
		}

		super.getResponse().addData(dataset);
	}

}
