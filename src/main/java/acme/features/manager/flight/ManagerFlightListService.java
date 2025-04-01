
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.realms.Manager;

@GuiService
public class ManagerFlightListService extends AbstractGuiService<Manager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Flight> jobs;
		int managerId;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		jobs = this.repository.findFlightByEmployerId(managerId);

		super.getBuffer().addData(jobs);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "cost", "requiresSelfTransfer");

		super.getResponse().addData(dataset);
	}

}
