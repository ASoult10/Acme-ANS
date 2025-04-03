
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
public class MemberFlightAssignmentUpdateService extends AbstractGuiService<Member, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private MemberFlightAssignmentRepository repository;

	// AbstractService<Member, FlightAssignment> -------------------------------------


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
		boolean futureLeg = !MomentHelper.isPast(flightAssignment.getLeg().getScheduledArrival());
		boolean legPublished = !flightAssignment.getLeg().isDraftMode();
		status = flightAssignment.isDraftMode() && correctMember && futureLeg && legPublished;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		int id;
		id = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(id);
		flightAssignment.setMoment(MomentHelper.getCurrentMoment());
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

		super.bindObject(flightAssignment, "duty", "assignmentStatus", "remarks");
		flightAssignment.setLeg(leg);
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		if (flightAssignment.getLeg() != null) {
			boolean futureLeg = !MomentHelper.isPast(flightAssignment.getLeg().getScheduledArrival());
			super.state(futureLeg, "leg", "acme.validation.FlightAssignment.pastLeg.message");

			boolean legPublished = !flightAssignment.getLeg().isDraftMode();
			super.state(legPublished, "leg", "acme.validation.FlightAssignment.notPublishedLeg.message");
		}
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

		legs = this.repository.findAllNotCompletedPublishedLegs(MomentHelper.getCurrentMoment());
		try {
			legChoices = SelectChoices.from(legs, "flightNumber", flightAssignment.getLeg());
		} catch (Exception e) {
			legChoices = SelectChoices.from(legs, "flightNumber", legs.get(0));
		}

		assignmentStatus = SelectChoices.from(AssignmentStatus.class, flightAssignment.getAssignmentStatus());
		duty = SelectChoices.from(Duty.class, flightAssignment.getDuty());

		dataset = super.unbindObject(flightAssignment, "duty", "assignmentStatus", "remarks", "draftMode");

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
