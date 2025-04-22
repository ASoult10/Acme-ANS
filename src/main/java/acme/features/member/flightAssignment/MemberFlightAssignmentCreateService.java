
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

		if (super.getRequest().hasData("id")) {

			boolean futureLeg = true;
			boolean legPublished = true;
			Integer legId = super.getRequest().getData("leg", int.class);
			if (legId != 0) {
				Leg leg = this.repository.findLegById(legId);
				futureLeg = leg != null && !MomentHelper.isPast(leg.getScheduledArrival());
				legPublished = leg != null && !leg.isDraftMode();
			}

			boolean correctMember = true;
			String employeeCode = super.getRequest().getData("member", String.class);

			Member member = this.repository.findMemberByEmployeeCode(employeeCode);
			correctMember = member != null && super.getRequest().getPrincipal().getActiveRealm().getId() == member.getId();

			status = correctMember && futureLeg && legPublished;
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

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(flightAssignment, "duty", "assignmentStatus", "remarks");
		flightAssignment.setLeg(leg);
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
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
		SelectChoices legChoices;

		Dataset dataset;

		legs = this.repository.findAllNotCompletedPublishedLegs(MomentHelper.getCurrentMoment());
		try {
			legChoices = SelectChoices.from(legs, "flightNumber", flightAssignment.getLeg());
		} catch (Exception e) {
			Leg leg = new Leg();
			legChoices = SelectChoices.from(legs, "flightNumber", leg);
		}

		assignmentStatus = SelectChoices.from(AssignmentStatus.class, flightAssignment.getAssignmentStatus());
		duty = SelectChoices.from(Duty.class, flightAssignment.getDuty());

		dataset = super.unbindObject(flightAssignment, "assignmentStatus", "remarks");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("moment", flightAssignment.getMoment());
		dataset.put("assignmentStatus", assignmentStatus);
		dataset.put("duty", duty);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("member", flightAssignment.getMember().getEmployeeCode());
		dataset.put("draftMode", true);

		super.getResponse().addData(dataset);
	}
}
