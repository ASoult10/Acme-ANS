
package acme.constraints;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.Manager;

@Validator
public class ManagerValidator extends AbstractValidator<ValidManager, Manager> {

	// Internal state ---------------------------------------------------------

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidManager annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Manager manager, final ConstraintValidatorContext context) {
		// HINT: manager can be null
		assert context != null;

		boolean result;

		if (manager == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			DefaultUserIdentity identity = manager.getIdentity();
			String iniciales = identity.getName().substring(0, 1) + Arrays.stream(identity.getSurname().split("\\s")).map(w -> w.substring(0, 1)).limit(2).collect(Collectors.joining()).toUpperCase();
			String identifier = manager.getIdentifierNumber();

			Boolean identificadorCorrecto = identifier.startsWith(iniciales) && identifier.length() == iniciales.length() + 6;

			super.state(context, identificadorCorrecto, "identifierNumber", "acme.validation.manager.wrong-initials.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
