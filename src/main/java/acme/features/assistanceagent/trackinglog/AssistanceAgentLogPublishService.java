
package acme.features.assistanceagent.trackinglog;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.trackinglogs.TrackingLog;
import acme.entities.trackinglogs.TrackingLogStatus;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentLogPublishService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

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
		boolean isClaimPublished = !selectedLog.getClaim().isDraftMode();

		status = correctMember && isClaimPublished && selectedLog.isDraftMode();
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
		super.bindObject(log, "step", "resolutionPercentage", "indicator", "resolution");
	}

	@Override
	public void validate(final TrackingLog log) {
		//validar que la claim asociada esta publicada y que el log no
		Claim claim;
		claim = log.getClaim();
		Date now = MomentHelper.getCurrentMoment();

		if (claim.isDraftMode())
			super.state(!claim.isDraftMode(), "draftMode", "assistanceAgent.claim.form.error.draftMode");

		if (!log.isDraftMode())
			super.state(log.isDraftMode(), "draftMode", "assistanceAgent.claim.form.error.draftMode");

		if (MomentHelper.isBefore(now, claim.getLeg().getScheduledArrival()))
			super.state(false, "leg", "");
	}

	@Override
	public void perform(final TrackingLog log) {
		log.setDraftMode(false);
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
