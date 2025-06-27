
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.airlines.Airline;
import acme.entities.airlines.AirlineRepository;

@Validator
public class AirlineValidator extends AbstractValidator<ValidAirline, Airline> {

	@Autowired
	private AirlineRepository repository;


	@Override
	protected void initialise(final ValidAirline annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Airline airline, final ConstraintValidatorContext context) {

		String identifier = airline.getIATA();

		if (identifier == null)
			return false;

		Airline airlineWithSameCode = this.repository.findAirlineByIATACode(identifier);

		if (airlineWithSameCode != null && airlineWithSameCode.getId() != airline.getId()) {
			super.state(context, false, "IATA", "{acme.validation.airline.repeated.message}: " + identifier);
			return false;
		}
		return true;
	}

}
