
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightCrewMembers.AvailabilityStatus;
import acme.features.member.flightAssignment.MemberFlightAssignmentRepository;

@Validator
public class FlightAssignmentValidator extends AbstractValidator<ValidFlightAssignment, FlightAssignment> {

	@Autowired
	private MemberFlightAssignmentRepository repository;


	@Override
	protected void initialise(final ValidFlightAssignment annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightAssignment flightAssignment, final ConstraintValidatorContext context) {
		if (flightAssignment == null)
			return false;

		boolean memberAvailable;
		memberAvailable = flightAssignment.getMember().getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE);
		super.state(context, memberAvailable, "member", "{acme.validation.FlightAssignment.memberNotAvailable.message}");

		boolean result = !super.hasErrors(context);
		return result;
	}

}
