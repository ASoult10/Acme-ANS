
package acme.features.member.flightAssignment;

import java.util.Collection;
import java.util.List;

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
		boolean status = true;

		Integer memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		try {
			Integer id = super.getRequest().getData("id", Integer.class);

			boolean futureLeg = true;
			boolean legPublished = true;
			boolean legNotOwned = true;
			Integer legId = null;
			if (id == null)
				status = false;
			else {
				FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(id);
				boolean flightAssignmentIsDraftMode = flightAssignment.isDraftMode();
				legId = super.getRequest().getData("leg", Integer.class);

				if (legId == null)
					status = false;
				else if (legId != 0) {
					Leg oldLeg = flightAssignment.getLeg();
					Leg leg = this.repository.findLegById(legId);
					futureLeg = leg != null && !MomentHelper.isPast(leg.getScheduledArrival()) && !MomentHelper.isPast(oldLeg.getScheduledArrival());
					legPublished = leg != null && !leg.isDraftMode();
					legNotOwned = !this.repository.findLegsByMemberId(memberId).contains(leg) || leg == flightAssignment.getLeg();
				}

				boolean correctMember = true;
				String employeeCode = super.getRequest().getData("member", String.class);
				Member member = this.repository.findMemberByEmployeeCode(employeeCode);
				correctMember = member != null && memberId == member.getId() && member.getId() == flightAssignment.getMember().getId();

				status = status && flightAssignmentIsDraftMode && correctMember && futureLeg && legPublished && legNotOwned;
			}
		} catch (Throwable e) {
			status = false;
		}
		status = super.getRequest().getMethod().equals("POST") && status;
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

		List<Leg> legs;
		SelectChoices legChoices = null;

		Dataset dataset;

		legs = this.repository.findAllNotCompletedPublishedLegs(MomentHelper.getCurrentMoment());
		Integer memberId = flightAssignment.getMember().getId();
		legs.removeAll(this.repository.findLegsByMemberId(memberId));
		FlightAssignment oldFlightAssignment = this.repository.findFlightAssignmentById(flightAssignment.getId());
		legs.add(oldFlightAssignment.getLeg());

		//try {
		Leg leg = flightAssignment.getLeg();
		legChoices = SelectChoices.from(legs, "flightNumber", leg);
		//} catch (NullPointerException e) {
		//}

		assignmentStatus = SelectChoices.from(AssignmentStatus.class, flightAssignment.getAssignmentStatus());
		duty = SelectChoices.from(Duty.class, flightAssignment.getDuty());

		dataset = super.unbindObject(flightAssignment, "duty", "assignmentStatus", "remarks", "draftMode");

		String identificador = legChoices.getSelected().getKey();

		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("moment", flightAssignment.getMoment());
		dataset.put("assignmentStatus", assignmentStatus);
		dataset.put("duty", duty);
		dataset.put("leg", identificador);
		dataset.put("legs", legChoices);
		dataset.put("member", flightAssignment.getMember().getEmployeeCode());

		Boolean legNotCompleted = MomentHelper.isFuture(oldFlightAssignment.getLeg().getScheduledArrival());

		dataset.put("legNotCompleted", legNotCompleted);

		super.getResponse().addData(dataset);

	}
}
