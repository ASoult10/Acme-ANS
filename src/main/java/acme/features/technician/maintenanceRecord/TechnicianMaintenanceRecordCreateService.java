
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
		boolean status = true;

		if (super.getRequest().hasData("id")) {

			boolean correctStatus = true;
			boolean correctAircraft = true;
			boolean correctTechnician = true;
			MaintenanceRecordStatus mrStatus = super.getRequest().getData("status", MaintenanceRecordStatus.class);
			Integer aircraftId = super.getRequest().getData("aircraft", int.class);
			Integer technicianId = super.getRequest().getData("technician", int.class);
			Integer techId = super.getRequest().getPrincipal().getActiveRealm().getId();
			if (technicianId != 0) {
				Technician technicianCorrect = this.repository.findTechnicianById(technicianId);
				correctTechnician = technicianCorrect != null && technicianId.equals(techId);
			}

			if (mrStatus != null)
				if (!mrStatus.equals(0))
					correctStatus = !mrStatus.equals(MaintenanceRecordStatus.COMPLETED);
			if (aircraftId != 0) {
				Aircraft aircraft = this.repository.findAircraftById(aircraftId);
				correctAircraft = aircraft != null;
			}
			status = correctAircraft && correctStatus && correctTechnician;
		}

		super.getResponse().setAuthorised(status);
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
		Integer aircraftId;
		Aircraft aircraft;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);

		super.bindObject(maintenanceRecord, "maintenanceMoment", "status", "nextInspectionDueDate", "estimatedCost", "notes");
		maintenanceRecord.setAircraft(aircraft);
	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {
		;

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

		maintenanceRecordstatus = new SelectChoices();
		maintenanceRecordstatus.add("0", "----", maintenanceRecord.getStatus() == null);
		maintenanceRecordstatus.add("PENDING", "PENDING", maintenanceRecord.getStatus() == MaintenanceRecordStatus.PENDING);
		maintenanceRecordstatus.add("IN_PROGRESS", "IN_PROGRESS", maintenanceRecord.getStatus() == MaintenanceRecordStatus.IN_PROGRESS);

		dataset = super.unbindObject(maintenanceRecord, "maintenanceMoment", "status", "nextInspectionDueDate", "estimatedCost", "notes", "draftMode");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("maintenanceRecordstatus", maintenanceRecordstatus);
		dataset.put("technicians", technicianChoices);
		dataset.put("technician", technicianChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
