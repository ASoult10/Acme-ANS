
package acme.features.customer.passenger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.BookingPassenger;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerDeleteService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		int passengerId = super.getRequest().getData("id", int.class);
		Passenger passenger = this.repository.findPassengerById(passengerId);

		super.getResponse().setAuthorised(customerId == passenger.getCustomer().getId() && !passenger.getIsPublished());
	}

	@Override
	public void load() {
		Passenger passenger = new Passenger();
		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passportNumber", "birthDate", "specialNeeds");
	}

	@Override
	public void validate(final Passenger passenger) {
		List<BookingPassenger> bookingPassengersOfPassenger = this.repository.findAllBookingPassengersByPassengerId(passenger.getId());
		super.state(bookingPassengersOfPassenger.isEmpty(), "*", "customer.passenger.form.error.associatedBookings");

	}

	@Override
	public void perform(final Passenger passenger) {

		this.repository.delete(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		assert passenger != null;
		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "birthDate", "specialNeeds", "isPublished");
		super.getResponse().addData(dataset);

	}

}
