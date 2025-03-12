
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.maintenanceRecord.MaintenanceRecord;

@Validator
public class InspectionDateValidator extends AbstractValidator<ValidInspectionDate, MaintenanceRecord> {

	@Override
	protected void initialise(final ValidInspectionDate constraintAnnotation) {
		assert constraintAnnotation != null;
	}

	@Override
	public boolean isValid(final MaintenanceRecord record, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;
		boolean isNull;
		isNull = record == null || record.getMaintenanceMoment() == null || record.getNextInspectionDueDate() == null;

		if (!isNull) {
			boolean nextInspectionIsAfterMaintenance;

			Date maintenanceDate = record.getMaintenanceMoment();
			Date nextInspection = record.getNextInspectionDueDate();
			nextInspectionIsAfterMaintenance = MomentHelper.isAfter(nextInspection, maintenanceDate);

			super.state(context, nextInspectionIsAfterMaintenance, "nextInspectionDueDate", "{acme.validation.maintenance-record.next-inspection.message}");
		}

		result = !super.hasErrors(context);
		return result;
	}
}
