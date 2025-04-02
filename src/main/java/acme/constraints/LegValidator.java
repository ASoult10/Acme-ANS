
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.StringHelper;
import acme.entities.legs.Leg;
import acme.entities.legs.LegRepository;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private LegRepository repository;

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
			{
				boolean uniqueNumber;
				Leg existingLeg;

				existingLeg = this.repository.findByFlightNumber(leg.getFlightNumber());
				uniqueNumber = existingLeg == null || existingLeg.equals(leg);

				super.state(context, uniqueNumber, "flightNumber", "acme.validation.leg.duplicated-number.message");
			}
			{
				// Comprobar que el momento de llegada es posterior al de salida
				Boolean horarioCorrecto = leg.getScheduledArrival() != null && leg.getScheduledDeparture() != null && MomentHelper.isAfter(leg.getScheduledArrival(), leg.getScheduledDeparture());

				super.state(context, horarioCorrecto, "scheduledArrival", "acme.validation.leg.wrong-scheduled-arrival.message");
			}
			{
				// Comprobar que el código de vuelo comienza con el código IATA de la aerolínea
				if (leg.getAirline() != null) {
					String airlineCode = leg.getAirline().getIATA();
					Boolean numeroCorrecto = StringHelper.startsWith(leg.getFlightNumber(), airlineCode, false);

					super.state(context, numeroCorrecto, "flightNumber", "acme.validation.leg.wrong-flight-number.message");
				}
			}

		}

		result = !super.hasErrors(context);

		return result;
	}
}
