
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimRepository;

public class ClaimValidator extends AbstractValidator<ValidClaim, Claim> {

	// Internal state ---------------------------------------------------------
	private ClaimRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	public boolean isValid(final Claim value, final ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		return false;
	}
}
