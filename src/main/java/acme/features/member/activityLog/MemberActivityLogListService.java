
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
		boolean correctMember = true;
		Integer masterId;
		FlightAssignment flightAssignment;

		masterId = super.getRequest().getData("masterId", Integer.class);
		if (masterId == null)
			status = false;
		else {

			flightAssignment = this.repository.findFlightAssignmentById(masterId);
			correctMember = flightAssignment != null && //
				(flightAssignment.getMember().getId() == super.getRequest().getPrincipal().getActiveRealm().getId() ||//
					!flightAssignment.isDraftMode());

		}
		status = status && correctMember;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		Collection<ActivityLog> activityLog;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		activityLog = this.repository.findPublishedActivityLogsByMasterId(masterId);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel");
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
		boolean inPast = MomentHelper.isPast(flightAssignment.getLeg().getScheduledArrival());
		boolean correctMember = super.getRequest().getPrincipal().getActiveRealm().getId() == flightAssignment.getMember().getId();
		showCreate = !flightAssignment.isDraftMode() && inPast && correctMember;

		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addGlobal("showCreate", showCreate);

	}

}
