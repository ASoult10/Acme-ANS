
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
import acme.entities.mappings.InvolvedIn;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordPublishService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		int id;

		id = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(id);
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
		boolean status;

		status = maintenanceRecord.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		Collection<InvolvedIn> involvedInCollection;
		involvedInCollection = this.repository.findInvolvedInByMaintenanceRecordId(maintenanceRecord.getId());

		boolean hasUnpublishedTask = false;
		boolean hasAtLeastOnePublishedTask = false;

		for (InvolvedIn involvedIn : involvedInCollection)
			if (involvedIn.getTask().isDraftMode())
				hasUnpublishedTask = true;
			else
				hasAtLeastOnePublishedTask = true;

		// Si hay alguna Task no publicada o si no hay ninguna publicada, no publicar el MaintenanceRecord
		if (hasUnpublishedTask || !hasAtLeastOnePublishedTask)
			throw new IllegalArgumentException("Cannot publish this Maintenance Record. It must have at least one published task and no unpublished tasks.");

		// Si pasa las validaciones, se publica
		maintenanceRecord.setDraftMode(false);
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

		super.getResponse().addData(dataset);

	}

}
