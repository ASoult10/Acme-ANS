
package acme.constraints;

import java.util.Optional;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.aircrafts.Aircraft;
import acme.features.administrator.aircraft.AircraftRepository;

@Validator

public class RegistrationNumberAircraftValidator extends AbstractValidator<ValidRegistrationNumber, Aircraft> {

	@Autowired
	private AircraftRepository repository;


	@Override
	protected void initialise(final ValidRegistrationNumber annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Aircraft aircraft, final ConstraintValidatorContext context) {
		assert context != null;

		if (aircraft == null)
			return false;

		String registrationNumber = aircraft.getRegistrationNumber();

		if (registrationNumber == null)
			return false;

		Optional<Aircraft> aircraftWithSameRegistrationNumber = this.repository.findOneAircraftByRegistrationNumber(registrationNumber);
		if (aircraftWithSameRegistrationNumber.isPresent() && aircraftWithSameRegistrationNumber.get().getId() != aircraft.getId()) {
			super.state(context, false, "registrationNumber", "{acme.validation.registration-number.repeated.message}: " + registrationNumber);
			return false;
		}

		return true;
	}

}
