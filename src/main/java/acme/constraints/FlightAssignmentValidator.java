
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightCrewMembers.AvailabilityStatus;
import acme.entities.legs.Leg;
import acme.features.member.flightAssignment.MemberFlightAssignmentRepository;

@Validator
public class FlightAssignmentValidator extends AbstractValidator<ValidFlightAssignment, FlightAssignment> {

	@Autowired
	private MemberFlightAssignmentRepository repository;


	@Override
	protected void initialise(final ValidFlightAssignment annotation) {
		//assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightAssignment flightAssignment, final ConstraintValidatorContext context) {
		//assert context != null;

		//if (flightAssignment == null)
		//	return false;

		//if (flightAssignment.getMember() != null) {
		boolean memberAvailable = flightAssignment.getMember().getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE);
		boolean pastFlightAssignment = MomentHelper.isPast(flightAssignment.getMoment());
		super.state(context, memberAvailable || pastFlightAssignment, "member", "{acme.validation.FlightAssignment.memberNotAvailable.message}");
		//}

		//Restricción legs incompatibles
		if (flightAssignment.getLeg() != null) {

			List<FlightAssignment> flightAssignmentByMember;
			flightAssignmentByMember = this.repository.findFlightAssignmentByMemberId(flightAssignment.getMember().getId());

			if (!flightAssignment.getLeg().isDraftMode())
				for (FlightAssignment fa : flightAssignmentByMember)
					if (!fa.getLeg().isDraftMode() && !this.legIsCompatible(flightAssignment.getLeg(), fa.getLeg()) && fa.getId() != flightAssignment.getId()) {
						super.state(context, false, "member", "acme.validation.FlightAssignment.memberHasIncompatibleLegs.message");
						break;
					}
		}
		//===========================
		//Restricción piloto copiloto

		if (flightAssignment.getDuty() != null && flightAssignment.getLeg() != null && !flightAssignment.getLeg().isDraftMode()) {

			List<FlightAssignment> flightAssignmentsByLeg;
			flightAssignmentsByLeg = this.repository.findFlightAssignmentByLegId(flightAssignment.getLeg().getId());
			boolean hasPilot = false;
			boolean hasCopilot = false;
			for (FlightAssignment fa : flightAssignmentsByLeg) {
				if (fa.getDuty().equals(Duty.PILOT) && flightAssignment.getId() != fa.getId())
					hasPilot = true;
				if (fa.getDuty().equals(Duty.CO_PILOT) && flightAssignment.getId() != fa.getId())
					hasCopilot = true;
			}

			super.state(context, !(flightAssignment.getDuty().equals(Duty.PILOT) && hasPilot), "duty", "{acme.validation.FlightAssignment.hasPilot.message}");
			super.state(context, !(flightAssignment.getDuty().equals(Duty.CO_PILOT) && hasCopilot), "duty", "{acme.validation.FlightAssignment.hasCopilot.message}");
		}
		//==============================

		boolean result = !super.hasErrors(context);
		return result;
	}
	private boolean legIsCompatible(final Leg legToIntroduce, final Leg legInTheDB) {
		boolean departureIncompatible = MomentHelper.isInRange(legToIntroduce.getScheduledDeparture(), legInTheDB.getScheduledDeparture(), legInTheDB.getScheduledArrival());
		boolean arrivalIncompatible = MomentHelper.isInRange(legToIntroduce.getScheduledArrival(), legInTheDB.getScheduledDeparture(), legInTheDB.getScheduledArrival());
		return !departureIncompatible && !arrivalIncompatible;
	}

}
