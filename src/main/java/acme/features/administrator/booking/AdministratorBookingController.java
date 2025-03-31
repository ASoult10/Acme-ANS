
package acme.features.administrator.booking;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.booking.Booking;

@GuiController
public class AdministratorBookingController extends AbstractGuiController<Administrator, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorBookingListService	administratorBookingListService;

	@Autowired
	private AdministratorBookingShowService	administratorBookingShowService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.administratorBookingListService);
		super.addBasicCommand("show", this.administratorBookingShowService);
	}

}
