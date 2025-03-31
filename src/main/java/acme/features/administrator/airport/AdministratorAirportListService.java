
package acme.features.administrator.airport;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airports.Airport;

@GuiService
public class AdministratorAirportListService extends AbstractGuiService<Administrator, Airport> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirportRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Airport> companies;

		companies = this.repository.findAllAirports();

		super.getBuffer().addData(companies);
	}

	@Override
	public void unbind(final Airport company) {
		Dataset dataset;

		dataset = super.unbindObject(company, "name", "code", "country");

		super.getResponse().addData(dataset);
	}

}
