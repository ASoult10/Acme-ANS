
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingPassenger;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.realms.Manager;

@GuiService
public class ManagerFlightDeleteService extends AbstractGuiService<Manager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Flight flight;
		Manager manager;

		masterId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(masterId);
		manager = flight == null ? null : flight.getManager();
		status = flight != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight;
		int id;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(id);

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");
	}

	@Override
	public void validate(final Flight flight) {
		boolean hasPublishedLegs;

		hasPublishedLegs = flight.getHasAllLegsPublished();

		super.state(!hasPublishedLegs, "*", "acme.validation.flight.cant-delete.message");
	}

	@Override
	public void perform(final Flight flight) {
		Collection<Leg> legs;
		Collection<Booking> bookings;
		Collection<BookingPassenger> bookingPassengers;

		legs = this.repository.findLegsByFlightId(flight.getId());
		bookings = this.repository.findBookingsByFlightId(flight.getId());
		bookingPassengers = this.repository.findBookingPassengersByBookingIds(bookings.stream().map(Booking::getId).toList());

		this.repository.deleteAll(bookingPassengers);
		this.repository.deleteAll(bookings);
		this.repository.deleteAll(legs);
		this.repository.delete(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "draftMode", "scheduledDeparture", "scheduledArrival", "originCity", "destinationCity", "hasPublishedLegs", "hasAllLegsPublished");

		super.getResponse().addData(dataset);
	}

}
