
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
public class AssistanceAgentLogDeleteService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssitanceAgentLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int logId;
		TrackingLog selectedLog;

		logId = super.getRequest().getData("id", int.class);
		selectedLog = this.repository.findLogById(logId);

		boolean correctMember = super.getRequest().getPrincipal().getActiveRealm().getId() == selectedLog.getClaim().getAssistanceAgent().getId();
		status = selectedLog.isDraftMode() && correctMember;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog log;
		int id;

		id = super.getRequest().getData("id", int.class);
		log = this.repository.findLogById(id);

		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final TrackingLog log) {
		super.bindObject(log, "lastUpdateMoment", "step", "resolutionPercentage", "resolution", "indicator", "creationMoment", "draftMode");
	}

	@Override
	public void validate(final TrackingLog log) {
		if (!log.isDraftMode())
			super.state(log.isDraftMode(), "draftMode", "assistanceAgent.claim.form.error.draftMode");
	}

	@Override
	public void perform(final TrackingLog log) {
		this.repository.delete(log);
	}

	@Override
	public void unbind(final TrackingLog log) {
		SelectChoices choices_status;
		Dataset dataset;

		choices_status = SelectChoices.from(TrackingLogStatus.class, log.getIndicator());

		dataset = super.unbindObject(log, "lastUpdateMoment", "resolutionPercentage", "resolution", "draftMode");
		dataset.put("indicator", choices_status);

		super.getResponse().addData(dataset);
	}
}
