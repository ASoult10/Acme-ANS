
package acme.features.member.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.AssignmentStatus;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.legs.Leg;
import acme.features.member.activityLog.MemberActivityLogRepository;
import acme.realms.Member;

@GuiService
public class MemberFlightAssignmentDeleteService extends AbstractGuiService<Member, FlightAssignment> {

	@Autowired
	private MemberFlightAssignmentRepository	repository;

	@Autowired
	private MemberActivityLogRepository			activityLogRepository;


	@Override
	public void authorise() {
		boolean status;
		Integer flightAssignmentId = null;
		FlightAssignment flightAssignment;

		Integer memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		try {
			if (super.getRequest().hasData("id")) {

				flightAssignmentId = super.getRequest().getData("id", Integer.class);
				if (flightAssignmentId == null)
					status = false;
				else {

					flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);

					boolean correctMember = true;
					String employeeCode = super.getRequest().getData("member", String.class);
					Member member = this.repository.findMemberByEmployeeCode(employeeCode);
					correctMember = member != null && memberId == member.getId() && member.getId() == flightAssignment.getMember().getId();

					status = flightAssignment.isDraftMode() && correctMember;
				}
			} else
				status = false;
		} catch (Throwable e) {
			status = false;
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;

		flightAssignment = new FlightAssignment();

		flightAssignment.setDraftMode(true);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		Integer legId;
		Leg leg;

		Integer memberId;
		Member member;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		String employeeCode = super.getRequest().getData("member", String.class);
		member = this.repository.findMemberByEmployeeCode(employeeCode);

		super.bindObject(flightAssignment, "duty", "moment", "assignmentStatus", "remarks");
		flightAssignment.setLeg(leg);
		flightAssignment.setMember(member);
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		;
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		Collection<ActivityLog> activityLogs = this.repository.getActivityLogByFlightAssignmentId(flightAssignment.getId());

		this.activityLogRepository.deleteAll(activityLogs);
		this.repository.delete(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		SelectChoices assignmentStatus;
		SelectChoices duty;
		int memberId;
		Collection<Leg> legs;
		SelectChoices legChoices;
		Collection<Member> members;
		SelectChoices memberChoices;
		Dataset dataset;
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		//legs = this.repository.findLegsByMemberId(memberId);
		legs = this.repository.findAllLegs();
		FlightAssignment oldFlightAssignment = this.repository.findFlightAssignmentById(flightAssignment.getId());

		members = this.repository.findAllAvailableMembers();
		legChoices = SelectChoices.from(legs, "flightNumber", flightAssignment.getLeg());
		memberChoices = SelectChoices.from(members, "employeeCode", flightAssignment.getMember());
		assignmentStatus = SelectChoices.from(AssignmentStatus.class, flightAssignment.getAssignmentStatus());
		duty = SelectChoices.from(Duty.class, flightAssignment.getDuty());

		dataset = super.unbindObject(flightAssignment, "duty", "moment", "assignmentStatus", "remarks", "draftMode");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("moment", MomentHelper.getBaseMoment());
		dataset.put("assignmentStatus", assignmentStatus);
		dataset.put("duty", duty);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("member", memberChoices.getSelected().getKey());
		dataset.put("members", memberChoices);

		Boolean legNotCompleted = MomentHelper.isFuture(oldFlightAssignment.getLeg().getScheduledArrival());
		dataset.put("legNotCompleted", legNotCompleted);
		super.getResponse().addData(dataset);
	}
}
