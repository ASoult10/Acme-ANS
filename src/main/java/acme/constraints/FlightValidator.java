
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.entities.legs.LegRepository;

@Validator
public class FlightValidator extends AbstractValidator<ValidFlight, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private LegRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidFlight annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Flight flight, final ConstraintValidatorContext context) {
		// HINT: manager can be null
		assert context != null;

		boolean result;

		if (flight == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				// Comprobar que los tramos de vuelos no se solapan
				boolean tramosSeparados = true;
				List<Leg> legs = this.repository.findLegsByFlight(flight.getId());

				for (Integer i = 0; i < legs.size() - 1; i++) {
					Leg primerTramo = legs.get(i);
					Leg segundoTramo = legs.get(i + 1);

					tramosSeparados &= MomentHelper.isBefore(primerTramo.getScheduledArrival(), segundoTramo.getScheduledDeparture());
				}

				super.state(context, tramosSeparados, "*", "acme.validation.flight.overlapping-legs.message");
			}
			{
				// Comprobar que si el vuelo está publicado tenga, al menos, un tramo de vuelo asociado
				boolean correctLegs;
				Integer numberOfLegs = this.repository.countNumberOfLegsOfFlight(flight.getId());

				correctLegs = flight.isDraftMode() || numberOfLegs > 0;

				super.state(context, correctLegs, "*", "acme.validation.flight.no-legs.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}
}
