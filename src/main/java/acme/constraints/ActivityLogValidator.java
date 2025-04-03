
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.activityLog.ActivityLog;

@Validator
public class ActivityLogValidator extends AbstractValidator<ValidActivityLog, ActivityLog> {

	@Override
	protected void initialise(final ValidActivityLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final ActivityLog activityLog, final ConstraintValidatorContext context) {
		if (activityLog == null)
			return false;
		if (activityLog.getRegistrationMoment() == null || activityLog.getFlightAssignment() == null || activityLog.getFlightAssignment().getLeg() == null || activityLog.getFlightAssignment().getLeg().getScheduledArrival() == null)
			return false;
		Date activityLogMoment = activityLog.getRegistrationMoment();
		Date scheduledArrival = activityLog.getFlightAssignment().getLeg().getScheduledArrival();
		Boolean activityLogMomentIsAfterscheduledArrival = MomentHelper.isAfter(activityLogMoment, scheduledArrival);
		super.state(context, activityLogMomentIsAfterscheduledArrival, "registrationMoment", "{acme.validation.activityLog.wrongMoment.message}");

		boolean result = !super.hasErrors(context);
		return result;
	}

}
