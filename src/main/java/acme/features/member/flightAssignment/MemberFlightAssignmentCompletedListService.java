
package acme.features.member.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.Member;

@GuiService
public class MemberFlightAssignmentCompletedListService extends AbstractGuiService<Member, FlightAssignment> {

	@Autowired
	private MemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		Boolean correctRealm = super.getRequest().getPrincipal().hasRealmOfType(Member.class);
		super.getResponse().setAuthorised(correctRealm);
	}

	@Override
	public void load() {
		Collection<FlightAssignment> flightAssignments;

		Date currentMoment;
		currentMoment = MomentHelper.getCurrentMoment();
		flightAssignments = this.repository.findCompletedPublishedFlightAssignments(currentMoment);

		super.getBuffer().addData(flightAssignments);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset = super.unbindObject(flightAssignment, "duty", "moment", "assignmentStatus", "remarks", "draftMode");

		super.getResponse().addData(dataset);
	}
}
