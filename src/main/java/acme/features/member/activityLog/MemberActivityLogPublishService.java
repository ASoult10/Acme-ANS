
package acme.features.member.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
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
		boolean activityLogDraftMode = false;
		Integer id;
		boolean correctMember = false;
		boolean status = true;
		try {

			id = super.getRequest().getData("id", Integer.class);
			if (id == null)
				status = false;
			else {
				activityLog = this.repository.findActivityLogById(id);
				activityLogDraftMode = activityLog.isDraftMode();
				correctMember = activityLog.getFlightAssignment().getMember().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();
			}
			status = status && correctMember && activityLogDraftMode;
		} catch (Throwable e) {
			status = false;
		}
		status = super.getRequest().getMethod().equals("POST") && status;
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

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
		dataset.put("registrationMoment", activityLog.getRegistrationMoment());
		dataset.put("masterId", activityLog.getFlightAssignment().getId());
		dataset.put("draftMode", activityLog.getFlightAssignment().isDraftMode());

		dataset.put("buttonsAvaiable", true);
		super.getResponse().addData(dataset);
	}
}
