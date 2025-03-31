
package acme.features.member.activityLog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.activityLog.ActivityLog;
import acme.realms.Member;

@GuiController
public class MemberActivityLogController extends AbstractGuiController<Member, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private MemberActivityLogListService	listService;

	@Autowired
	private MemberActivityLogShowService	showService;

	@Autowired
	private MemberActivityLogCreateService	createService;

	@Autowired
	private MemberActivityLogUpdateService	updateService;

	@Autowired
	private MemberActivityLogPublishService	publishService;

	@Autowired
	private MemberActivityLogDeleteService	deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
