
package acme.features.manager.flight;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.realms.Manager;

@GuiService
public class ManagerFlightPublishService extends AbstractGuiService<Manager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		Flight flight;
		Manager manager;

		flightId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(flightId);
		manager = flight == null ? null : flight.getManager();

		status = flight != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(manager) && flight.getHasAllLegsPublished() && flight.getHasPublishedLegs();

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
		{
			boolean tramosFuturos = true;
			List<Leg> legs = this.repository.findLegsByFlight(flight.getId());

			for (Leg leg : legs)
				if (MomentHelper.isPast(leg.getScheduledArrival())) {
					tramosFuturos = false;
					break;
				}

			super.state(tramosFuturos, "*", "acme.validation.flight.future-legs.message");
		}
	}

	@Override
	public void perform(final Flight flight) {
		flight.setDraftMode(false);
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "draftMode", "scheduledDeparture", "scheduledArrival", "originCity", "destinationCity", "numberOfLayovers", "hasPublishedLegs", "hasAllLegsPublished");

		super.getResponse().addData(dataset);
	}

}
