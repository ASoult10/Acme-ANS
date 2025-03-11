
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.StringHelper;
import acme.realms.AssistanceAgent;

public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	// Internal state ---------------------------------------------------------

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidAssistanceAgent annotation) {
		assert annotation != null;
	}

	@Override
	//the first two or three letters correspond to their initials
	public boolean isValid(final AssistanceAgent ag, final ConstraintValidatorContext context) {
		// HINT: assistanceAgent can be null
		assert context != null;

		boolean result;

		if (ag == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			//acceder a la cuenta del usuario para recuperar las iniciales de su nombre y comprobar que coinciden
			DefaultUserIdentity identity = ag.getIdentity();
			String initials = identity.getName().trim().substring(0, 1) + identity.getSurname().trim().substring(0, 1);

			Boolean sameInitials = StringHelper.startsWith(ag.getEmployeeCode(), initials, true);
			super.state(context, sameInitials, "employeeCode", "acme.validation.assistanceagent.initials-dont-match.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
