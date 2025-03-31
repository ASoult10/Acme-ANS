
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.airports.Airport;
import acme.entities.airports.AirportRepository;

@Validator
public class AirportValidator extends AbstractValidator<ValidAirport, Airport> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirportRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidAirport annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Airport airport, final ConstraintValidatorContext context) {
		// HINT: manager can be null
		assert context != null;

		boolean result;

		if (airport == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean uniqueAirport;
			Airport existingAirport;

			existingAirport = this.repository.findByIataCode(airport.getIataCode());
			uniqueAirport = existingAirport == null || existingAirport.equals(airport);

			super.state(context, uniqueAirport, "iataCode", "acme.validation.airport.duplicated-code.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
