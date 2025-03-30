
package acme.features.technician.involvedIn;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.mappings.InvolvedIn;
import acme.realms.Technician;

@GuiController
public class TechnicianInvolvedInController extends AbstractGuiController<Technician, InvolvedIn> {

	@Autowired
	private TechnicianInvolvedInListService	listService;

	@Autowired
	private TechnicianInvolvedInShowService	showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);

	}

}
