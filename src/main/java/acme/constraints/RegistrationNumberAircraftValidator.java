
package acme.constraints;

import java.util.List;

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

		if (aircraft == null) {
			super.state(context, false, "*", "{acme.validation.aircraft.null.message}");
			return false;
		}

		String registrationNumber = aircraft.getRegistrationNumber();

		if (registrationNumber == null) {
			super.state(context, false, "*", "{acme.validation.registration-number.null.message");
			return false;
		}

		List<Aircraft> aircraftsWithSameRegistrationNumber = this.repository.findAircraftsByRegistrationNumber(registrationNumber);
		for (Integer i = 0; i < aircraftsWithSameRegistrationNumber.size(); i++)
			if (aircraftsWithSameRegistrationNumber.get(i).getId() != aircraft.getId()) {
				super.state(context, false, "registrationNumber", "{acme.validation.registration-number.repeated.message}: " + registrationNumber);
				return false;
			}

		return true;
	}

}
