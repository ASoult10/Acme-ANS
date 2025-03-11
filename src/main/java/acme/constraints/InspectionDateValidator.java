
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.maintenancerecord.MaintenanceRecord;

@Validator
public class InspectionDateValidator extends AbstractValidator<ValidInspectionDate, MaintenanceRecord> {

	@Override
	protected void initialise(final ValidInspectionDate constraintAnnotation) {
		assert constraintAnnotation != null;
	}

	@Override
	public boolean isValid(final MaintenanceRecord record, final ConstraintValidatorContext context) {
		assert context != null;

		if (record == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			return false;
		}

		boolean valid = true;

		if (record.getMoment() == null) {
			super.state(context, false, "moment", "acme.validation.maintenance-record.null-moment.message");
			valid = false;
		}

		if (record.getNextInspectionDueDate() == null) {
			super.state(context, false, "nextInspectionDueDate", "acme.validation.maintenance-record.null-next-inspection.message");
			valid = false;
		}

		if (valid) {
			// Validar que nextInspectionDueDate es posterior a moment
			boolean isAfter = MomentHelper.isAfter(record.getNextInspectionDueDate(), record.getMoment());
			super.state(context, isAfter, "nextInspectionDueDate", "acme.validation.maintenance-record.invalid-inspection-date.message");

			valid = isAfter;
		}

		return !super.hasErrors(context);
	}
}
