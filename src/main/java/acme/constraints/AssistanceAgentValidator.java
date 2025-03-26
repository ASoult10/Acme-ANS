
package acme.constraints;

import java.util.Optional;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.StringHelper;
import acme.features.authenticated.assistanceAgent.AgentRepository;
import acme.realms.AssistanceAgent;

public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AgentRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidAssistanceAgent annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AssistanceAgent ag, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (ag == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			result = !super.hasErrors(context);
		} else if (ag.getEmployeeCode() == null || ag.getIdentity() == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			result = !super.hasErrors(context);
		} else
			result = this.checkÍnitials(ag, context) && this.isUnique(ag, context);
		return result;
	}

	//the first two or three letters correspond to their initials
	public boolean checkÍnitials(final AssistanceAgent ag, final ConstraintValidatorContext context) {
		// HINT: assistanceAgent can be null
		assert context != null;

		boolean result;

		//acceder a la cuenta del usuario para recuperar las iniciales de su nombre y comprobar que coinciden
		DefaultUserIdentity identity = ag.getIdentity();
		String initials = identity.getName().trim().substring(0, 1) + identity.getSurname().trim().substring(0, 1);

		Boolean sameInitials = StringHelper.startsWith(ag.getEmployeeCode(), initials, true);
		super.state(context, sameInitials, "employeeCode", "acme.validation.identifier.notInitials.message");

		result = !super.hasErrors(context);

		return result;
	}

	public boolean isUnique(final AssistanceAgent ag, final ConstraintValidatorContext context) {
		boolean result;

		String identifier = ag.getEmployeeCode();

		Optional<AssistanceAgent> agentWithSameCode = this.repository.findOneAgentByEmployeeCode(identifier);
		if (agentWithSameCode.isPresent() && agentWithSameCode.get().getId() != ag.getId())
			super.state(context, false, "*", "{acme.validation.member.repeated.message}: " + identifier);

		result = !super.hasErrors(context);

		return result;
	}

}
