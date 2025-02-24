
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.legs.Leg;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	// Internal state ---------------------------------------------------------

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		// HINT: manager can be null
		assert context != null;

		boolean result;

		if (leg == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			// Comprobar que el momento de llegada es posterior al de salida
			Boolean horarioCorrecto = MomentHelper.isAfter(leg.getScheduledDeparture(), leg.getScheduledArrival());

			super.state(context, horarioCorrecto, "scheduledArrival", "acme.validation.leg.wrong-scheduled-arrival.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
