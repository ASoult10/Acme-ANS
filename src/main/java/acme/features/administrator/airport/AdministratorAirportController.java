
package acme.features.administrator.airport;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.airports.Airport;

@GuiController
public class AdministratorAirportController extends AbstractGuiController<Administrator, Airport> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirportListService		listService;

	@Autowired
	private AdministratorAirportShowService		showService;

	@Autowired
	private AdministratorAirportUpdateService	updateService;

	@Autowired
	private AdministratorAirportCreateService	createService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("create", this.createService);
	}

}
