
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.Member;

@Validator
public class MemberValidator extends AbstractValidator<ValidMember, Member> {

	@Override
	protected void initialise(final ValidMember annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Member member, final ConstraintValidatorContext context) {

		String identifier = member.getEmployeeCode();

		if (identifier == null || !identifier.matches("^[A-Z]{2,3}\\d{6}$")) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.identifier.nullornotpattern.message").addConstraintViolation();
			return false;
		}

		DefaultUserIdentity identity = member.getUserAccount().getIdentity();

		String inicialNombre = String.valueOf(identity.getName().charAt(0)).toUpperCase();
		String inicialApellido = String.valueOf(identity.getSurname().charAt(0)).toUpperCase();

		String iniciales = inicialNombre + inicialApellido;

		String employeeCodeInitials = identifier.substring(0, 2);

		if (!iniciales.equals(employeeCodeInitials)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.identifier.notInitials.message").addConstraintViolation();
			return false;
		}

		return true;
	}

}
