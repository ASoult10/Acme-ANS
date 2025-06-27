
package acme.features.member.flightAssignment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.AssignmentStatus;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.Member;

@GuiService
public class MemberFlightAssignmentCreateService extends AbstractGuiService<Member, FlightAssignment> {

	@Autowired
	private MemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status = true;

		try {
			if (super.getRequest().hasData("id")) {

				boolean futureLeg = true;
				boolean legPublished = true;
				boolean legNotOwned = true;
				Integer memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
				Integer legId = super.getRequest().getData("leg", Integer.class);
				if (legId == null)
					status = false;
				else {

				}
				if (legId != null && legId != 0) {
					Leg leg = null;
					leg = this.repository.findLegById(legId);

					futureLeg = leg != null && !MomentHelper.isPast(leg.getScheduledArrival());
					legPublished = leg != null && !leg.isDraftMode();
					legNotOwned = !this.repository.findLegsByMemberId(memberId).contains(leg);
				}

				boolean correctMember = true;
				String employeeCode = super.getRequest().getData("member", String.class);

				Member member = this.repository.findMemberByEmployeeCode(employeeCode);
				correctMember = member != null && memberId == member.getId();

				status = status && correctMember && futureLeg && legPublished && legNotOwned;
			}
		} catch (Throwable e) {
			status = false;
		}
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		Member member;

		member = (Member) super.getRequest().getPrincipal().getActiveRealm();

		flightAssignment = new FlightAssignment();

		flightAssignment.setDraftMode(true);
		flightAssignment.setMoment(MomentHelper.getCurrentMoment());
		flightAssignment.setMember(member);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		Integer legId;
		Leg leg;

		legId = super.getRequest().getData("leg", Integer.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(flightAssignment, "duty", "assignmentStatus", "remarks");
		flightAssignment.setLeg(leg);
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		;
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {

		SelectChoices assignmentStatus;
		SelectChoices duty;

		List<Leg> legs;
		SelectChoices legChoices = null;

		Dataset dataset;

		legs = this.repository.findAllNotCompletedPublishedLegs(MomentHelper.getCurrentMoment());
		Integer memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		legs.removeAll(this.repository.findLegsByMemberId(memberId));
		//try {
		legChoices = SelectChoices.from(legs, "flightNumber", flightAssignment.getLeg());
		//} catch (Throwable e) {

		//}

		assignmentStatus = SelectChoices.from(AssignmentStatus.class, flightAssignment.getAssignmentStatus());
		duty = SelectChoices.from(Duty.class, flightAssignment.getDuty());
		String identificador = legChoices.getSelected().getKey();// == null ? "" : legChoices.getSelected().getKey();
		dataset = super.unbindObject(flightAssignment, "assignmentStatus", "remarks");
		dataset.put("readonly", false);
		dataset.put("moment", flightAssignment.getMoment());
		dataset.put("assignmentStatus", assignmentStatus);
		dataset.put("duty", duty);
		dataset.put("leg", identificador);
		dataset.put("legs", legChoices);
		dataset.put("member", flightAssignment.getMember().getEmployeeCode());
		dataset.put("draftMode", true);

		super.getResponse().addData(dataset);
	}
}
