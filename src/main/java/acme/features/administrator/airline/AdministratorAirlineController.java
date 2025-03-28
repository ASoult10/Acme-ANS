
package acme.features.administrator.airline;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.entities.airlines.Airline;

public class AdministratorAirlineController extends AbstractGuiController<Administrator, Airline> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirlineListService	listService;

	@Autowired
	private AdministratorAirlineShowService	showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initials() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}
}
