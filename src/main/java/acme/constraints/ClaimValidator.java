
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimRepository;

public class ClaimValidator extends AbstractValidator<ValidClaim, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClaimRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidClaim annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Claim claim, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (claim == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");

		result = !super.hasErrors(context);
		return result;

	}
}
