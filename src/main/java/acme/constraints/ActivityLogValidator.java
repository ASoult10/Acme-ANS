
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.activityLog.ActivityLogs;

@Validator
public class ActivityLogValidator extends AbstractValidator<ValidActivityLog, ActivityLogs> {

	@Override
	protected void initialise(final ValidActivityLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final ActivityLogs activityLog, final ConstraintValidatorContext context) {

		Date activityLogMoment = activityLog.getRegistrationMoment();
		Date scheduledArrival = activityLog.getFlightAssignment().getLeg().getScheduledArrival();
		Boolean activityLogMomentIsAfterscheduledArrival = MomentHelper.isAfter(activityLogMoment, scheduledArrival);
		super.state(context, activityLogMomentIsAfterscheduledArrival, "WrongActivityLogDate", "{acme.validation.activityLogMoment.wrong.message}");

		boolean result = !super.hasErrors(context);
		return result;
	}

}
