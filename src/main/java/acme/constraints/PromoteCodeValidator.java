
package acme.constraints;

import java.util.Calendar;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;

@Validator
public class PromoteCodeValidator extends AbstractValidator<ValidPromoteCode, String> {

	@Override
	protected void initialise(final ValidPromoteCode annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final String promoteCode, final ConstraintValidatorContext context) {

		if (promoteCode == null || promoteCode.equals(""))
			return true;

		if (!promoteCode.matches("^[A-Z]{4}-[0-9]{2}$")) {
			super.state(context, false, "*", "{acme.validation.promoteCode.notPattern.message}" + promoteCode);
			return false;
		}

		String promoteCodeYear = promoteCode.substring(promoteCode.length() - 2);

		Date moment = MomentHelper.getCurrentMoment();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(moment);
		String actualCurrentYear = String.valueOf(calendar.get(Calendar.YEAR)).substring(2);

		if (!promoteCodeYear.equals(actualCurrentYear)) {
			super.state(context, false, "*", "{acme.validation.promoteCode.notCurrentYear.message} CY: " + actualCurrentYear + ", PY: " + promoteCodeYear);
			return false;
		}

		return true;
	}

}
