
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

		if (technician == null || technician.getLicenseNumber() == null)
			return true; // Se asume que la validación de campo obligatorio ya se hace en otra parte

		String license = technician.getLicenseNumber();

		// 1. Validar el formato del LicenseNumber
		if (!license.matches("^[A-Z]{2,3}\\d{6}$")) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.license-number.invalid-format.message").addConstraintViolation();
			return false;
		}

		// 2. Obtener identidad del técnico
		DefaultUserIdentity identity = technician.getUserAccount().getIdentity();

		// 3. Extraer iniciales
		String inicialNombre = String.valueOf(identity.getName().charAt(0)).toUpperCase();
		String inicial1Apellido = String.valueOf(identity.getSurname().charAt(0)).toUpperCase();
		String inicial2Apellido = "";
		Integer initialsLength = 2;

		// Si el apellido tiene dos palabras, se toma la primera letra de la segunda
		if (identity.getSurname().contains(" ")) {
			inicial2Apellido = String.valueOf(identity.getSurname().split(" ")[1].charAt(0)).toUpperCase();
			initialsLength++;
		}

		String expectedInitials = inicialNombre + inicial1Apellido + inicial2Apellido;

		// 4. Comparar iniciales con el LicenseNumber
		String licenseInitials = license.substring(0, initialsLength);

		if (!expectedInitials.equals(licenseInitials)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.license-number.not-matching-initials.message").addConstraintViolation();
			return false;
		}

		return true;
	}
}
