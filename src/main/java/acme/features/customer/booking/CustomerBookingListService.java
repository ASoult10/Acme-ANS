
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.realms.Customer;

@GuiService
public class CustomerBookingListService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository customerBookingRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Booking> bookings = this.customerBookingRepository.findAllBookingsByCustomerId(customerId);
		super.getBuffer().addData(bookings);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "isPublished", "id");
		super.getResponse().addData(dataset);
	}

}
