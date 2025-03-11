
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.Customer;

@Validator
public class CustomerValidator extends AbstractValidator<ValidCustomer, Customer> {

	@Override
	protected void initialise(final ValidCustomer annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {

		String identifier = customer.getIdentifier();

		if (identifier == null || !identifier.matches("^[A-Z]{2,3}\\d{6}$")) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.identifier.nullornotpattern.message").addConstraintViolation();
			return false;
		}

		DefaultUserIdentity identity = customer.getUserAccount().getIdentity();

		String inicialNombre = String.valueOf(identity.getName().charAt(0)).toUpperCase();
		String inicial1Apellido = String.valueOf(identity.getSurname().charAt(0)).toUpperCase();
		String inicial2Apellido = "";
		Integer initialsLenght = 2;

		if (identity.getSurname().contains(" ")) {
			inicial2Apellido = String.valueOf(identity.getSurname().split(" ")[1].charAt(0)).toUpperCase();
			initialsLenght++;
		}

		String iniciales = inicialNombre + inicial1Apellido + inicial2Apellido;

		String identifierInitials = identifier.substring(0, initialsLenght);

		if (!iniciales.equals(identifierInitials)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.identifier.notInitials.message").addConstraintViolation();
			return false;
		}

		return true;
	}

}
