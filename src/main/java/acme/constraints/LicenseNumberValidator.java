
package acme.constraints;

import java.util.Optional;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.features.authenticated.technician.TechnicianRepository;
import acme.realms.Technician;

@Validator
public class LicenseNumberValidator extends AbstractValidator<ValidLicenseNumber, Technician> {

	@Autowired
	private TechnicianRepository repository;


	@Override
	protected void initialise(final ValidLicenseNumber annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Technician technician, final ConstraintValidatorContext context) {
		assert context != null;

		if (technician == null) {
			super.state(context, false, "licenseNumber", "{acme.validation.technician.null.message}");
			return false;
		}

		String license = technician.getLicenseNumber();
		if (license == null || license.isBlank())
			return true;

		DefaultUserIdentity identity = technician.getUserAccount().getIdentity();
		String name = identity.getName().trim();
		String surname = identity.getSurname().trim();

		String initials = "" + name.charAt(0) + surname.charAt(0);

		// Comprobamos que las iniciales están al principio del licenseNumber
		boolean matchesInitials = license.toUpperCase().startsWith(initials);

		super.state(context, matchesInitials, "licenseNumber", "{acme.validation.license-number.not-matching-initials.message}");

		// Validar que el número de licencia no se repite
		Optional<Technician> technicianWithSameLicense = this.repository.findOneTechnicianByLicenseNumber(license);
		boolean unique = technicianWithSameLicense.isEmpty() || technicianWithSameLicense.get().getId() == technician.getId();

		super.state(context, unique, "licenseNumber", "{acme.validation.license-number.repeated.message}: " + license);

		return !super.hasErrors(context);
	}

}
