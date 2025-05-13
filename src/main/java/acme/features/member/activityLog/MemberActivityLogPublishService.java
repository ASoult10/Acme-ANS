
package acme.features.member.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.Member;

@GuiService
public class MemberActivityLogPublishService extends AbstractGuiService<Member, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private MemberActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		ActivityLog activityLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(id);
		boolean correctMember = activityLog.getFlightAssignment().getMember().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean flightAssignmentPublished = !activityLog.getFlightAssignment().isDraftMode();
		boolean inPast = MomentHelper.isPast(activityLog.getFlightAssignment().getLeg().getScheduledArrival());

		boolean status = correctMember && flightAssignmentPublished && inPast && activityLog.isDraftMode();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog activityLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(id);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		super.bindObject(activityLog, "typeOfIncident", "description", "severityLevel");
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		;
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		activityLog.setDraftMode(false);
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;

		final boolean showCreate;
		FlightAssignment flightAssignment = activityLog.getFlightAssignment();

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
		dataset.put("registrationMoment", activityLog.getRegistrationMoment());
		dataset.put("masterId", activityLog.getFlightAssignment().getId());
		dataset.put("draftMode", activityLog.getFlightAssignment().isDraftMode());
		boolean inPast = MomentHelper.isPast(flightAssignment.getLeg().getScheduledArrival());
		boolean correctMember = super.getRequest().getPrincipal().getActiveRealm().getId() == flightAssignment.getMember().getId();

		showCreate = !flightAssignment.isDraftMode() && activityLog.isDraftMode() && inPast && correctMember;

		dataset.put("buttonsAvaiable", showCreate);
		super.getResponse().addData(dataset);
	}
}
