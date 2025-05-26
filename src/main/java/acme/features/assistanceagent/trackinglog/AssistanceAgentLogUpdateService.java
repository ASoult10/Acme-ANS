
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
public class AssistanceAgentLogUpdateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

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

		boolean correctMember = selectedLog.getClaim().getAssistanceAgent().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();

		status = correctMember && selectedLog.isDraftMode();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		TrackingLog log;

		id = super.getRequest().getData("id", int.class);
		log = this.repository.findLogById(id);

		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final TrackingLog log) {
		super.bindObject(log, "step", "resolutionPercentage", "indicator", "resolution");
	}

	@Override
	public void validate(final TrackingLog log) {
		if (!log.isDraftMode())
			super.state(log.isDraftMode(), "draftMode", "assistanceAgent.claim.form.error.draftMode");

		//validar que el % tiene que incrementar
		int claimId = log.getClaim().getId();
		Double lastPercentage = this.repository.findLastLog(claimId).getResolutionPercentage();
		Double prevPercentage = this.repository.findLogById(log.getId()).getResolutionPercentage();
		boolean valueChanged = prevPercentage == null || !prevPercentage.equals(log.getResolutionPercentage());

		if (log.getResolutionPercentage() != null && log.getResolutionPercentage() == 100.00) {
			int count = this.repository.countResolvedLogs(claimId);
			super.state(count < 2, "resolutionPercentage", "");
		}

		if (valueChanged && log.getResolutionPercentage() != null && lastPercentage != null) {
			boolean validPercentage = log.getResolutionPercentage() >= lastPercentage;
			super.state(validPercentage, "resolutionPercentage", "");
		}

	}

	@Override
	public void perform(final TrackingLog log) {
		this.repository.save(log);
	}

	@Override
	public void unbind(final TrackingLog log) {
		SelectChoices choices_status;
		Dataset dataset;

		choices_status = SelectChoices.from(TrackingLogStatus.class, log.getIndicator());

		dataset = super.unbindObject(log, "step", "resolutionPercentage", "resolution", "draftMode");
		dataset.put("indicator", choices_status);

		super.getResponse().addData(dataset);
	}
}
