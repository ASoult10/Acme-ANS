
package acme.features.member.flightAssignment;

import java.util.Collection;
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
public class MemberFlightAssignmentPublishService extends AbstractGuiService<Member, FlightAssignment> {

	@Autowired
	private MemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		int memberId;
		FlightAssignment flightAssignment;
		Member member;

		id = super.getRequest().getData("id", int.class);
		memberId = super.getRequest().getData("member", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(id);
		member = this.repository.findMemberById(memberId);

		boolean correctMember = super.getRequest().getPrincipal().getActiveRealm().getId() == member.getId();
		System.out.println(super.getRequest().getPrincipal().getActiveRealm().getId() + " == " + member.getId());
		boolean futureLeg = !MomentHelper.isPast(flightAssignment.getLeg().getScheduledArrival());
		System.out.println(flightAssignment.getLeg().getScheduledArrival());

		status = flightAssignment.isDraftMode() && correctMember && futureLeg;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		int id;
		id = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(id);
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

		if (flightAssignment.getLeg() != null)
			super.state(MomentHelper.isFuture(flightAssignment.getLeg().getScheduledArrival()), "leg", "acme.validation.FlightAssignment.notValidLeg.message");

		//Restricción legs incompatibles
		List<Leg> legsByMember;
		legsByMember = this.repository.findLegsByMemberId(flightAssignment.getMember().getId());

		if (flightAssignment.getLeg() != null)
			for (Leg leg : legsByMember)
				if (!this.legIsCompatible(flightAssignment.getLeg(), leg) && flightAssignment.getLeg().getId() != leg.getId()) {
					super.state(false, "member", "acme.validation.FlightAssignment.memberHasIncompatibleLegs.message");
					break;
				}
		//===========================
		//Restricción piloto copiloto

		if (flightAssignment.getLeg() != null && flightAssignment.getDuty() != null) {

			List<FlightAssignment> flightAssignmentsByLeg;
			flightAssignmentsByLeg = this.repository.findFlightAssignmentByLegId(flightAssignment.getLeg().getId());
			boolean hasPilot = false;
			boolean hasCopilot = false;
			for (FlightAssignment fa : flightAssignmentsByLeg) {
				if (fa.getDuty().equals(Duty.PILOT) && fa.getId() != flightAssignment.getId())
					hasPilot = true;
				if (fa.getDuty().equals(Duty.CO_PILOT) && fa.getId() != flightAssignment.getId())
					hasCopilot = true;
			}

			super.state(!(flightAssignment.getDuty().equals(Duty.PILOT) && hasPilot), "duty", "acme.validation.FlightAssignment.hasPilot.message");
			super.state(!(flightAssignment.getDuty().equals(Duty.CO_PILOT) && hasCopilot), "duty", "acme.validation.FlightAssignment.hasCopilot.message");
		}
		//==============================
	}
	private boolean legIsCompatible(final Leg legToIntroduce, final Leg legInTheDB) {
		boolean departureIncompatible = MomentHelper.isInRange(legToIntroduce.getScheduledDeparture(), legInTheDB.getScheduledDeparture(), legInTheDB.getScheduledArrival());
		boolean arrivalIncompatible = MomentHelper.isInRange(legToIntroduce.getScheduledArrival(), legInTheDB.getScheduledDeparture(), legInTheDB.getScheduledArrival());
		return !departureIncompatible && !arrivalIncompatible;
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		flightAssignment.setDraftMode(false);
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {

		SelectChoices assignmentStatus;
		SelectChoices duty;

		Collection<Leg> legs;
		SelectChoices legChoices;

		Dataset dataset;

		legs = this.repository.findAllLegs();

		legChoices = SelectChoices.from(legs, "flightNumber", flightAssignment.getLeg());

		assignmentStatus = SelectChoices.from(AssignmentStatus.class, flightAssignment.getAssignmentStatus());
		duty = SelectChoices.from(Duty.class, flightAssignment.getDuty());

		dataset = super.unbindObject(flightAssignment, "duty", "assignmentStatus", "remarks", "draftMode");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("moment", flightAssignment.getMoment());
		dataset.put("assignmentStatus", assignmentStatus);
		dataset.put("member", flightAssignment.getMember());
		dataset.put("duty", duty);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);

		super.getResponse().addData(dataset);
	}
}
