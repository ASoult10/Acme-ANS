
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
public class MemberFlightAssignmentShowService extends AbstractGuiService<Member, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private MemberFlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		Boolean status = true;
		boolean correctMember = true;
		Integer activeMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		Integer id = super.getRequest().getData("id", Integer.class);
		if (id == null)
			status = false;
		else {

			FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(id);
			correctMember = flightAssignment != null &&//
				(activeMemberId == flightAssignment.getMember().getId() || //
					!flightAssignment.isDraftMode());
		}
		status = status && correctMember;
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
	public void unbind(final FlightAssignment flightAssignment) {
		SelectChoices assignmentStatus;
		SelectChoices duty;

		List<Leg> legs;
		SelectChoices legChoices = null;

		Dataset dataset;

		legs = this.repository.findAllNotCompletedPublishedLegs(MomentHelper.getCurrentMoment());
		Integer memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
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

		String identificador = legChoices.getSelected().getKey(); //== null ? "" : legChoices.getSelected().getKey();

		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("moment", flightAssignment.getMoment());
		dataset.put("assignmentStatus", assignmentStatus);
		dataset.put("duty", duty);
		dataset.put("leg", identificador);
		dataset.put("legs", legChoices);
		dataset.put("member", flightAssignment.getMember().getEmployeeCode());

		Boolean legNotCompleted = MomentHelper.isFuture(flightAssignment.getLeg().getScheduledArrival());
		dataset.put("legNotCompleted", legNotCompleted);

		super.getResponse().addData(dataset);

	}

}
