
package acme.features.member.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
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
		int flightAssignmentId;
		FlightAssignment flightAssignment;

		Integer memberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		flightAssignmentId = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);

		boolean correctMember = true;
		String employeeCode = super.getRequest().getData("member", String.class);
		Member member = this.repository.findMemberByEmployeeCode(employeeCode);
		correctMember = member != null && memberId == member.getId();

		status = flightAssignment.isDraftMode() && correctMember;
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

	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		Collection<ActivityLog> activityLogs = this.repository.getActivityLogByFlightAssignmentId(flightAssignment.getId());

		this.activityLogRepository.deleteAll(activityLogs);
		this.repository.delete(flightAssignment);
	}

}
