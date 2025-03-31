
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
		//Member member;

		//member = (Member) super.getRequest().getPrincipal().getActiveRealm();

		flightAssignment = new FlightAssignment();

		flightAssignment.setDraftMode(true);
		//flightAssignment.setMember(member);

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

		memberId = super.getRequest().getData("member", int.class);
		member = this.repository.findMemberById(memberId);

		super.bindObject(flightAssignment, "duty", "moment", "assignmentStatus", "remarks");
		flightAssignment.setLeg(leg);
		flightAssignment.setMember(member);
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {

		List<Leg> legsByMember;
		legsByMember = this.repository.findLegsByMemberId(flightAssignment.getMember().getId());

		for (Leg leg : legsByMember)
			if (!this.legIsCompatible(flightAssignment.getLeg(), leg)) {
				System.out.println(flightAssignment.getLeg().getFlightNumber() + "  " + leg.getFlightNumber());
				super.state(false, "member", "acme.validation.FlightAssignment.memberHasIncompatibleLegs.message");
				break;
			}

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

		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}
	private boolean legIsCompatible(final Leg legToIntroduce, final Leg legInTheDB) {
		boolean departureIncompatible = MomentHelper.isInRange(legToIntroduce.getScheduledDeparture(), legInTheDB.getScheduledDeparture(), legInTheDB.getScheduledArrival());
		boolean arrivalIncompatible = MomentHelper.isInRange(legToIntroduce.getScheduledArrival(), legInTheDB.getScheduledDeparture(), legInTheDB.getScheduledArrival());
		System.out.println("departureIncompatible " + departureIncompatible);
		System.out.println("arrivalIncompatible " + arrivalIncompatible);
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

		int memberId;
		Collection<Leg> legs;
		SelectChoices legChoices;

		Collection<Member> members;
		SelectChoices memberChoices;
		Dataset dataset;

		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		//legs = this.repository.findLegsByMemberId(memberId);
		legs = this.repository.findAllLegs();
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

		super.getResponse().addData(dataset);
	}
}
