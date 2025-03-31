
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
	private MemberFlightAssignmentCompletedListService		completedListService;

	@Autowired
	private MemberFlightAssignmentNotCompletedListService	notCompletedListService;

	@Autowired
	private MemberFlightAssignmentCreateService				createService;

	@Autowired
	private MemberFlightAssignmentUpdateService				updateService;

	@Autowired
	private MemberFlightAssignmentPublishService			publishService;

	@Autowired
	private MemberFlightAssignmentShowService				showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("completedlist", "list", this.completedListService);
		super.addCustomCommand("notCompletedlist", "list", this.notCompletedListService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
