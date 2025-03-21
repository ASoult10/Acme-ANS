
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.trackinglogs.TrackingLog;
import acme.entities.trackinglogs.TrackingLogRepository;
import acme.entities.trackinglogs.TrackingLogStatus;

public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TrackingLogRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog tl, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (tl == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			result = !super.hasErrors(context);
		} else if (tl.getIndicator() == null || tl.getResolution() == null || tl.getCreationMoment() == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			result = !super.hasErrors(context);
		} else
			result = this.checkStatus(tl, context) && this.mandatoryResolution(tl, context) && this.isIncreasing(tl, context);
		return result;
	}

	//The intermediate tracking logs can keep in state “PENDING”, whereas the last one (when the resolution percentage is 100%), must set state to either “ACCEPTED” or “REJECTED”.
	//The status can be “ACCEPTED” or “REJECTED” only when the resolution percentage gets to 100%.
	public boolean checkStatus(final TrackingLog tl, final ConstraintValidatorContext context) {
		boolean result;
		if (tl.getResolutionPercentage() == 100) {
			if (tl.getIndicator() != TrackingLogStatus.ACCEPTED && tl.getIndicator() != TrackingLogStatus.REJECTED)
				super.state(context, false, "trackingLogIndicator", "acme.validation.trackingLog.incorrectIndicator.message");
		} else if (tl.getIndicator() != TrackingLogStatus.PENDING)
			super.state(context, false, "trackingLogIndicator", "acme.validation.trackingLog.mustBePending.message");

		result = !super.hasErrors(context);

		return result;

	}

	//If the status is not “PENDING”, then the resolution is mandatory; otherwise, it’s optional.
	public boolean mandatoryResolution(final TrackingLog tl, final ConstraintValidatorContext context) {
		boolean result;
		if (tl.getIndicator() == TrackingLogStatus.PENDING)
			if (tl.getResolution() == null)
				super.state(context, false, "*", "javax.validation.constraints.NotNull.message");

		result = !super.hasErrors(context);

		return result;
	}

	//the resolution percentage must be monotonically increasing.
	public boolean isIncreasing(final TrackingLog tl, final ConstraintValidatorContext context) {
		boolean result;

		List<TrackingLog> logs = this.repository.findAllLogsFromClaimSortedByCreationMoment(tl.getClaim().getId());
		if (logs.size() > 0)
			if (logs.get(0).getResolutionPercentage() >= tl.getResolutionPercentage())
				super.state(context, false, "resolutionPercentage", "acme.validation.trackingLog.increasingPercentage.message");

		result = !super.hasErrors(context);

		return result;
	}

}
