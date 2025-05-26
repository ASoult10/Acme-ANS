
package acme.features.assistanceagent.trackinglog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.trackinglogs.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiController
public class AssistanceAgentLogController extends AbstractGuiController<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentLogListService		listService;

	@Autowired
	private AssistanceAgentLogShowService		showService;

	@Autowired
	private AssistanceAgentLogCreateService		createService;

	@Autowired
	private AssistanceAgentLogUpdateService		updateService;

	@Autowired
	private AssistanceAgentLogDeleteService		deleteService;

	@Autowired
	private AssistanceAgentLogPublishService	publishService;


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
