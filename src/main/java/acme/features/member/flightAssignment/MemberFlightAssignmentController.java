
package acme.features.member.flightAssignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.Member;

@GuiController
public class MemberFlightAssignmentController extends AbstractGuiController<Member, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private MemberFlightAssignmentListService		listService;

	@Autowired
	private MemberFlightAssignmentCreateService		createService;

	@Autowired
	private MemberFlightAssignmentUpdateService		updateService;

	@Autowired
	private MemberFlightAssignmentPublishService	publishService;

	@Autowired
	private MemberFlightAssignmentShowService		showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("publish", "update", this.publishService);
	}

}
