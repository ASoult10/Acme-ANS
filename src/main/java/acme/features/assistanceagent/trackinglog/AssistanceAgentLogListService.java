
package acme.features.assistanceagent.trackinglog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.trackinglogs.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentLogListService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssitanceAgentLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int logId;
		TrackingLog selectedLog;
		boolean correctMember;

		logId = super.getRequest().getData("masterId", int.class);
		selectedLog = this.repository.findLogById(logId);
		correctMember = selectedLog.getClaim().getAssistanceAgent().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();
		status = correctMember && (selectedLog != null || selectedLog.isDraftMode());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<TrackingLog> logs;
		int id;

		id = super.getRequest().getData("masterId", int.class);
		logs = this.repository.findLogsByClaim(id);

		super.getBuffer().addData(logs);
	}

	@Override
	public void unbind(final TrackingLog log) {
		Dataset dataset;

		dataset = super.unbindObject(log, "lastUpdateMoment", "indicator", "resolutionPercentage", "creationMoment", "draftMode");
		dataset.put("masterId", log.getClaim().getId());

		super.addPayload(dataset, log, "step", "resolution");
		super.getResponse().addData(dataset);

	}

	@Override
	public void unbind(final Collection<TrackingLog> logs) {
		int id;
		Claim claim;
		final boolean showCreate;

		id = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimById(id);
		boolean correctMember = super.getRequest().getPrincipal().getActiveRealm().getId() == claim.getAssistanceAgent().getId();
		showCreate = !claim.isDraftMode() && correctMember;

		super.getResponse().addGlobal("masterId", id);
		super.getResponse().addGlobal("showCreate", showCreate);
	}

}
