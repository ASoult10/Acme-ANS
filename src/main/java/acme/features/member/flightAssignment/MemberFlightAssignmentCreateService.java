
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
		super.getResponse().setAuthorised(true);
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

		if (flightAssignment.getLeg() != null) {

			System.out.println("time  " + flightAssignment.getLeg().getScheduledArrival() + "getId = " + flightAssignment.getLeg().getId());
			super.state(MomentHelper.isFuture(flightAssignment.getLeg().getScheduledArrival()), "leg", "acme.validation.FlightAssignment.notValidLeg.message");

			System.out.println("time  " + flightAssignment.getLeg().getScheduledArrival() + "getId = " + flightAssignment.getLeg().getId());
		}

		//Restricción legs incompatibles
		List<Leg> legsByMember;
		legsByMember = this.repository.findLegsByMemberId(flightAssignment.getMember().getId());

		if (flightAssignment.getLeg() != null)
			for (Leg leg : legsByMember)
				if (!this.legIsCompatible(flightAssignment.getLeg(), leg)) {
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
				if (fa.getDuty().equals(Duty.PILOT))
					hasPilot = true;
				if (fa.getDuty().equals(Duty.CO_PILOT))
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
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {

		SelectChoices assignmentStatus;
		SelectChoices duty;

		List<Leg> legs;
		SelectChoices legChoices;

		Dataset dataset;

		legs = this.repository.findAllNotCompletedLegs(MomentHelper.getCurrentMoment());
		try {
			legChoices = SelectChoices.from(legs, "flightNumber", flightAssignment.getLeg());
		} catch (Exception e) {
			legChoices = SelectChoices.from(legs, "flightNumber", legs.get(0));
			System.out.println("leg  " + legs.get(0).getScheduledArrival() + "getId = " + legs.get(0).getId());
		}

		assignmentStatus = SelectChoices.from(AssignmentStatus.class, flightAssignment.getAssignmentStatus());
		duty = SelectChoices.from(Duty.class, flightAssignment.getDuty());

		dataset = super.unbindObject(flightAssignment, "assignmentStatus", "remarks", "draftMode");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("moment", flightAssignment.getMoment());
		dataset.put("assignmentStatus", assignmentStatus);
		dataset.put("duty", duty);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("member", flightAssignment.getMember());

		super.getResponse().addData(dataset);
	}
}
