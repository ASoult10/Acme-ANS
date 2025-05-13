
package acme.features.customer.bookingPassenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingPassenger;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingPassengerCreateService extends AbstractGuiService<Customer, BookingPassenger> {

	@Autowired
	private CustomerBookingPassengerRepository customerBookingPassengerRepository;


	@Override
	public void authorise() {
		Boolean status = true;

		try {

			Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			Integer bookingId = super.getRequest().getData("bookingId", Integer.class);
			Booking booking = this.customerBookingPassengerRepository.findBookingById(bookingId);
			status = status && booking != null && customerId == booking.getCustomer().getId();

			if (super.getRequest().hasData("id")) {
				String locatorCode = super.getRequest().getData("locatorCode", String.class);
				status = status && booking.getLocatorCode().equals(locatorCode);

				Integer passengerId = super.getRequest().getData("passenger", Integer.class);
				Passenger passenger = null;
				if (passengerId != null)
					passenger = this.customerBookingPassengerRepository.findPassengerById(passengerId);
				status = status && passengerId != null && (passenger != null && customerId == passenger.getCustomer().getId() || passengerId == 0);

				Collection<Passenger> alreadyAddedPassengers = this.customerBookingPassengerRepository.findAllPassengersByBookingId(bookingId);
				status = status && !alreadyAddedPassengers.stream().anyMatch(p -> p.getId() == passengerId);
			}

		} catch (Throwable E) {
			status = false;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Integer bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.customerBookingPassengerRepository.findBookingById(bookingId);
		BookingPassenger bookingPassenger = new BookingPassenger();
		bookingPassenger.setBooking(booking);
		super.getBuffer().addData(bookingPassenger);
	}

	@Override
	public void bind(final BookingPassenger bookingPassenger) {
		super.bindObject(bookingPassenger, "passenger", "locatorCode");
	}

	@Override
	public void validate(final BookingPassenger bookingPassenger) {
		;

	}

	@Override
	public void perform(final BookingPassenger bookingPassenger) {
		this.customerBookingPassengerRepository.save(bookingPassenger);
	}

	@Override
	public void unbind(final BookingPassenger bookingPassenger) {
		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		Integer bookingId = super.getRequest().getData("bookingId", int.class);

		Collection<Passenger> alreadyAddedPassengers = this.customerBookingPassengerRepository.findAllPassengersByBookingId(bookingId);
		Collection<Passenger> noAddedPassengers = this.customerBookingPassengerRepository.findAllPassengersByCustomerId(customerId).stream().filter(p -> !alreadyAddedPassengers.contains(p)).toList();
		SelectChoices passengerChoices = SelectChoices.from(noAddedPassengers, "fullName", bookingPassenger.getPassenger());

		Dataset dataset = super.unbindObject(bookingPassenger, "passenger", "booking");
		dataset.put("locatorCode", bookingPassenger.getBooking().getLocatorCode());
		dataset.put("passengers", passengerChoices);

		super.getResponse().addData(dataset);

	}

}
