
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.maintenanceRecord.MaintenanceRecordStatus;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordCreateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		Technician technician;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		maintenanceRecord = new MaintenanceRecord();

		maintenanceRecord.setTechnician(technician);
		maintenanceRecord.setDraftMode(true);

		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {
		int aircraftId;
		Aircraft aircraft;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);

		super.bindObject(maintenanceRecord, "maintenanceMoment", "status", "nextInspectionDueDate", "estimatedCost", "notes");
		maintenanceRecord.setAircraft(aircraft);
	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {
		boolean confirmation;
		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		this.repository.save(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		SelectChoices maintenanceRecordstatus;
		SelectChoices aircraftChoices;
		SelectChoices technicianChoices;

		Collection<Technician> technicians;
		Dataset dataset;

		technicians = this.repository.findAllTechnicians();

		technicianChoices = SelectChoices.from(technicians, "licenseNumber", maintenanceRecord.getTechnician());

		Collection<Aircraft> aircrafts;

		aircrafts = this.repository.findAllAircrafts();

		aircraftChoices = SelectChoices.from(aircrafts, "model", maintenanceRecord.getAircraft());

		maintenanceRecordstatus = SelectChoices.from(MaintenanceRecordStatus.class, maintenanceRecord.getStatus());

		dataset = super.unbindObject(maintenanceRecord, "maintenanceMoment", "status", "nextInspectionDueDate", "estimatedCost", "notes", "draftMode");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("maintenanceRecordstatus", maintenanceRecordstatus);
		dataset.put("technicians", technicianChoices);
		dataset.put("technician", technicianChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
	}
}
