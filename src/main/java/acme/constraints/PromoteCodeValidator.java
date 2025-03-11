
package acme.constraints;

import java.time.LocalDate;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class PromoteCodeValidator extends AbstractValidator<ValidPromoteCode, String> {

	@Override
	protected void initialise(final ValidPromoteCode annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final String promoteCode, final ConstraintValidatorContext context) {

		if (promoteCode == null || promoteCode == "")
			return true;

		if (!promoteCode.matches("^[A-Z]{4}-[0-9]{2}$")) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.promoteCode.notPattern.message  " + promoteCode).addConstraintViolation();
			return false;
		}

		String promoteCodeYear = promoteCode.substring(promoteCode.length() - 2);

		String actualCurrentYear = String.valueOf(LocalDate.now().getYear()).substring(2);

		if (!promoteCodeYear.equals(actualCurrentYear)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.promoteCode.notCurrentYear.message").addConstraintViolation();
			return false;
		}

		return true;
	}

}
