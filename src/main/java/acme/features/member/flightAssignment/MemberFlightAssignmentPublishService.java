
package acme.features.member.flightAssignment;

import java.util.Collection;

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
		int masterId;
		FlightAssignment flightAssignment;

		masterId = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(masterId);

		status = flightAssignment.isDraftMode() && MomentHelper.isFuture(flightAssignment.getLeg().getScheduledArrival());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;

		flightAssignment = new FlightAssignment();

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

		int memberId;
		Collection<Leg> legs;
		SelectChoices legChoices;

		Collection<Member> members;
		SelectChoices memberChoices;
		Dataset dataset;

		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
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
