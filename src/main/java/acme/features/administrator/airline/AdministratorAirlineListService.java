
package acme.features.administrator.airline;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.entities.airlines.Airline;

public class AdministratorAirlineListService extends AbstractGuiService<Administrator, Airline> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirlineRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<AbstractEntity> airlines;

		airlines = this.repository.findAll();

		super.getBuffer().addData(airlines);
	}

	@Override
	public void unbind(final Airline airline) {
		Dataset dataset = super.unbindObject(airline, "name", "IATA", "website", "type", "email", "phoneNumber");
		super.addPayload(dataset, airline, "foundationMoment");

		super.getResponse().addData(dataset);
	}

}
