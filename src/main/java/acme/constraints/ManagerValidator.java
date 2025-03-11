
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;
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
			// Comprobar que las dos primeras letras del número de identificación coinciden con las iniciales del gestor
			DefaultUserIdentity identity = manager.getIdentity();
			String iniciales = identity.getName().trim().substring(0, 1) + identity.getSurname().trim().substring(0, 1);

			Boolean identificadorCorrecto = StringHelper.startsWith(manager.getIdentifierNumber(), iniciales, true);

			super.state(context, identificadorCorrecto, "identifierNumber", "acme.validation.identifier.notInitials.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
