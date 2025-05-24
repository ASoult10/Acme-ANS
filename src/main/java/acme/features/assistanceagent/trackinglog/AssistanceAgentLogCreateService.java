
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
public class AssistanceAgentLogCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssitanceAgentLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		boolean correctMember;
		int logId;
		TrackingLog selectedLog;

		logId = super.getRequest().getData("id", int.class);
		selectedLog = this.repository.findLogById(logId);
		correctMember = super.getRequest().getPrincipal().getActiveRealm().getId() == selectedLog.getClaim().getAssistanceAgent().getId();

		status = correctMember && selectedLog != null && !selectedLog.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog log;
		Date creationMoment;
		Claim claim;
		int id;

		id = super.getRequest().getData("masterId", int.class);
		creationMoment = MomentHelper.getCurrentMoment();
		claim = this.repository.findClaimById(id);

		log = new TrackingLog();
		log.setCreationMoment(creationMoment);
		log.setLastUpdateMoment(creationMoment);
		log.setIndicator(TrackingLogStatus.PENDING);
		log.setDraftMode(true);
		log.setClaim(claim);

		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final TrackingLog log) {
		super.bindObject(log, "step", "resolutionPercentage", "resolution");
	}

	@Override
	public void validate(final TrackingLog log) {
		//validar que incrementa el %
		int claimId = log.getClaim().getId();
		Double lastPercentage = this.repository.findLastLog(claimId).getResolutionPercentage();

		if (log.getResolutionPercentage() != null && log.getResolutionPercentage() == 100.00) {
			int count = this.repository.countResolvedLogs(claimId);
			super.state(count < 2, "resolutionPercentage", "");
		}

		if (log.getResolutionPercentage() != null && lastPercentage != null) {
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

		dataset = super.unbindObject(log, "lastUpdateMoment", "resolutionPercentage", "resolution", "draftMode");
		dataset.put("indicator", choices_status);

		super.getResponse().addData(dataset);
	}

}
