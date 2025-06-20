
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

		if (license == null || license.isBlank() || license.strip().length() < 2)
			return true;

		DefaultUserIdentity identity = technician.getUserAccount().getIdentity();

		String inicialNombre = String.valueOf(identity.getName().charAt(0)).toUpperCase();
		String inicial1Apellido = String.valueOf(identity.getSurname().charAt(0)).toUpperCase();
		String inicial2Apellido = "";
		Integer initialsLength = 2;

		if (identity.getSurname().contains(" ")) {
			inicial2Apellido = String.valueOf(identity.getSurname().split(" ")[1].charAt(0)).toUpperCase();
			initialsLength++;
		}

		String expectedInitials = inicialNombre + inicial1Apellido + inicial2Apellido;
		String licenseInitials = license.substring(0, initialsLength);

		if (!expectedInitials.equals(licenseInitials)) {
			super.state(context, false, "licenseNumber", "{acme.validation.license-number.not-matching-initials.message}");
			return false;
		}

		Optional<Technician> technicianWithSameLicense = this.repository.findOneTechnicianByLicenseNumber(license);
		if (technicianWithSameLicense.isPresent() && technicianWithSameLicense.get().getId() != technician.getId()) {
			super.state(context, false, "licenseNumber", "{acme.validation.license-number.repeated.message}: " + license);
			return false;
		}

		return true;
	}
}
