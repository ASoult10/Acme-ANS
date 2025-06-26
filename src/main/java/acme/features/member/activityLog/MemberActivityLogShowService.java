
package acme.features.member.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.Member;

@GuiService
public class MemberActivityLogShowService extends AbstractGuiService<Member, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private MemberActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		Boolean status = true;
		boolean correctMember = true;
		ActivityLog activityLog;
		Integer id;

		try {

			id = super.getRequest().getData("id", Integer.class);
			if (id == null)
				status = false;
			else {

				activityLog = this.repository.findActivityLogById(id);
				correctMember = activityLog != null && //
					(activityLog.getFlightAssignment().getMember().getId() == super.getRequest().getPrincipal().getActiveRealm().getId() //
						|| !activityLog.isDraftMode());

			}
			status = status && correctMember;
		} catch (Throwable e) {
			status = false;
		}
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
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;

		FlightAssignment flightAssignment;
		boolean showCreate = false;

		flightAssignment = activityLog.getFlightAssignment();

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");

		boolean correctMember = super.getRequest().getPrincipal().getActiveRealm().getId() == flightAssignment.getMember().getId();
		boolean draftMode = activityLog.isDraftMode();
		showCreate = correctMember && draftMode;
		dataset.put("masterId", activityLog.getFlightAssignment().getId());
		dataset.put("buttonsAvaiable", showCreate);
		super.getResponse().addData(dataset);

	}

}
