
package acme.features.assistanceagent.trackinglog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.trackinglogs.TrackingLog;
import acme.entities.trackinglogs.TrackingLogStatus;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentLogShowService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssitanceAgentLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int logId;
		TrackingLog selectedLog;

		logId = super.getRequest().getData("masterId", int.class);
		selectedLog = this.repository.findLogById(logId);
		boolean correctMember = selectedLog.getClaim().getAssistanceAgent().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();
		status = correctMember && (selectedLog != null || !selectedLog.isDraftMode());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		TrackingLog log;

		id = super.getRequest().getData("masterId", int.class);
		log = this.repository.findLogById(id);

		super.getBuffer().addData(log);
	}

	@Override
	public void unbind(final TrackingLog log) {
		SelectChoices choices_status;
		Dataset dataset;

		choices_status = SelectChoices.from(TrackingLogStatus.class, log.getIndicator());

		dataset = super.unbindObject(log, "lastUpdateMoment", "resolutionPercentage", "creationMoment", "draftMode");
		dataset.put("indicator", choices_status);

		super.getResponse().addData(dataset);
	}
}
