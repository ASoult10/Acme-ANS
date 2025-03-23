
package acme.features.aircraft;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;

@GuiService
public class AdministratorAircraftShowService extends AbstractGuiService<Administrator, Aircraft> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAircraftRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Aircraft aircraft;
		Administrator administrator;

		masterId = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(masterId);
		administrator = aircraft == null ? null : aircraft.getAdministrator();
		status = super.getRequest().getPrincipal().hasRealm(administrator) || aircraft != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Aircraft aircraft;
		int id;

		id = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(id);

		super.getBuffer().addData(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		//int administratorId;
		//Collection<Airline> airlines;
		//SelectChoices choices;
		Dataset dataset;

		//airlines = this.repository.findAllAirlines();
		//choices = SelectChoices.from(airlines, "name", aircraft.getAirline());
		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");
		//dataset.put("airline", choices.getSelected().getKey());
		//dataset.put("airlines", choices);
		super.getResponse().addData(dataset);
	}
}
