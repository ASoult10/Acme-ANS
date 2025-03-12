
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.Technician;

@Validator
public class LicenseNumberValidator extends AbstractValidator<ValidLicenseNumber, Technician> {

	@Override
	protected void initialise(final ValidLicenseNumber constraintAnnotation) {
		assert constraintAnnotation != null;
	}

	@Override
	public boolean isValid(final Technician technician, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;
		boolean isNull;
		isNull = technician == null || technician.getLicenseNumber() == null;

		if (!isNull) {
			String license = technician.getLicenseNumber();
			boolean matchesPattern = license.matches("^[A-Z]{2,3}\\d{6}$");

			super.state(context, matchesPattern, "licenseNumber", "{acme.validation.license-number.invalid-format.message}");

			if (matchesPattern) {
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

				boolean initialsMatch = expectedInitials.equals(licenseInitials);
				super.state(context, initialsMatch, "licenseNumber", "{acme.validation.license-number.not-matching-initials.message}");
			}
		}

		result = !super.hasErrors(context);
		return result;
	}
}
