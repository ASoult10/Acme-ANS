
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerListService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository customerPassengerRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		if (!super.getRequest().getData().isEmpty()) {
			Integer bookingId = super.getRequest().getData("bookingId", int.class);
			Booking booking = this.customerPassengerRepository.getBookingById(bookingId);
			Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			status = status && booking.getCustomer().getId() == customerId;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Passenger> passengers;
		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		if (!super.getRequest().getData().containsKey("bookingId"))
			passengers = this.customerPassengerRepository.getPassengersByCustomer(customerId);
		else {
			Integer bookingId = super.getRequest().getData("bookingId", int.class);
			passengers = this.customerPassengerRepository.findPassengerByBookingId(bookingId);
		}

		super.getBuffer().addData(passengers);
		System.out.println(super.getBuffer());
	}

	@Override
	public void unbind(final Passenger passenger) {
		assert passenger != null;

		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "birthDate", "specialNeeds", "isPublished");

		super.getResponse().addData(dataset);
		super.addPayload(dataset, passenger, "specialNeeds");
	}

}
