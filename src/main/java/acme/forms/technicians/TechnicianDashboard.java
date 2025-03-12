
package acme.forms.technicians;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import acme.entities.maintenancerecord.MaintenanceRecordStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long						serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private Map<MaintenanceRecordStatus, Integer>	maintenanceRecordsByStatus;
	private String									nearestInspectionRecord;
	private List<String>							topFiveAircraftByTasks;
	private Money									maintenanceAverageCost;
	private Money									maintenanceMinimumCost;
	private Money									maintenanceMaximumCost;
	private Money									maintenanceDeviationCost;
	private Double									taskAverageDuration;
	private Integer									taskMinimumDuration;
	private Integer									taskMaximumDuration;
	private Double									taskDeviationDuration;

}
