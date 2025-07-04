
package acme.features.member.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.Member;

@GuiService
public class MemberActivityLogListService extends AbstractGuiService<Member, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private MemberActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;
		boolean flightAssignmentPublished = true;
		boolean legNotFuture = false;
		Integer masterId;
		FlightAssignment flightAssignment;

		try {

			masterId = super.getRequest().getData("masterId", Integer.class);
			if (masterId == null)
				status = false;
			else {

				flightAssignment = this.repository.findFlightAssignmentById(masterId);
				legNotFuture = flightAssignment != null && !MomentHelper.isFuture(flightAssignment.getLeg().getScheduledArrival());
				flightAssignmentPublished = flightAssignment != null && !flightAssignment.isDraftMode();
			}
			status = status && flightAssignmentPublished && legNotFuture;
		} catch (Throwable e) {
			status = false;
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		Collection<ActivityLog> activityLog;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);

		if (this.repository.findFlightAssignmentById(masterId).getMember().getId() != super.getRequest().getPrincipal().getActiveRealm().getId())
			activityLog = this.repository.findPublishedActivityLogsByMasterId(masterId);
		else
			activityLog = this.repository.findActivityLogsByMasterId(masterId);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
		super.addPayload(dataset, activityLog, "registrationMoment", "typeOfIncident");

		super.getResponse().addData(dataset);

	}

	@Override
	public void unbind(final Collection<ActivityLog> activityLog) {
		int masterId;
		FlightAssignment flightAssignment;
		final boolean showCreate;

		masterId = super.getRequest().getData("masterId", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(masterId);
		boolean correctMember = super.getRequest().getPrincipal().getActiveRealm().getId() == flightAssignment.getMember().getId();
		showCreate = correctMember;

		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addGlobal("showCreate", showCreate);

	}

}
