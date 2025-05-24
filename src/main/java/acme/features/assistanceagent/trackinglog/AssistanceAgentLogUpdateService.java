
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
		//TODO: validar que el % tiene que incrementar

		if (log.isDraftMode())
			super.state(log.isDraftMode(), "draftMode", "assistanceAgent.claim.form.error.draftMode");

		/*
		 * int claimId = trackingLog.getClaim().getId();
		 * Double maxExisting = this.repository.findMaxResolutionPercentageByClaimId(claimId);
		 * TrackingLog existing = this.repository.findLogById(trackingLog.getId());
		 * Double previousPercentage = existing.getResolutionPercentage();
		 * boolean valueChanged = previousPercentage == null || !previousPercentage.equals(trackingLog.getResolutionPercentage());
		 * 
		 * if (!trackingLog.isDraftMode())
		 * super.state(trackingLog.isDraftMode(), "*", "assistance-agent.tracking-log.form.error.draftMode");
		 * 
		 * if (trackingLog.getResolutionPercentage() != null && trackingLog.getResolutionPercentage() == 100.00) {
		 * int existingCount = this.repository.countFullyResolvedLogs(claimId);
		 * super.state(existingCount < 2, "resolutionPercentage", "acme.validation.trackingLog.limit-100.message");
		 * }
		 * 
		 * if (valueChanged && trackingLog.getResolutionPercentage() != null && maxExisting != null) {
		 * boolean validPercentage = trackingLog.getResolutionPercentage() >= maxExisting;
		 * super.state(validPercentage, "resolutionPercentage", "acme.validation.trackingLog.strict-increase.message");
		 * }
		 */
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
