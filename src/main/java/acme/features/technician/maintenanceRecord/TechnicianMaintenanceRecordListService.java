
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordListService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<MaintenanceRecord> maintenanceRecords;

		maintenanceRecords = this.repository.findAllPublishedMaintenanceRecord();

		super.getBuffer().addData(maintenanceRecords);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset = super.unbindObject(maintenanceRecord, "maintenanceMoment", "status", "nextInspectionDueDate", "estimatedCost", "draftMode");
		super.addPayload(dataset, maintenanceRecord, "notes");

		super.getResponse().addData(dataset);
	}
}
