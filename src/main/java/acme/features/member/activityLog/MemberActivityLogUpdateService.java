
package acme.features.member.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.realms.Member;

@GuiService
public class MemberActivityLogUpdateService extends AbstractGuiService<Member, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private MemberActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		ActivityLog activityLog;
		int id;
		boolean correctMember = false;
		boolean status = super.getRequest().getMethod().equals("POST");
		try {

			id = super.getRequest().getData("id", int.class);
			activityLog = this.repository.findActivityLogById(id);
			correctMember = activityLog.getFlightAssignment().getMember().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();
			status = status && correctMember && activityLog.isDraftMode();
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
	public void bind(final ActivityLog activityLog) {
		super.bindObject(activityLog, "typeOfIncident", "description", "severityLevel");
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		;
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
		dataset.put("registrationMoment", activityLog.getRegistrationMoment());
		dataset.put("masterId", activityLog.getFlightAssignment().getId());
		dataset.put("draftMode", activityLog.getFlightAssignment().isDraftMode());

		dataset.put("buttonsAvaiable", true);
		super.getResponse().addData(dataset);
	}

}
