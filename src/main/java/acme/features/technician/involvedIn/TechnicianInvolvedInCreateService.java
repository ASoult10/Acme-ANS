
package acme.features.technician.involvedIn;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.mappings.InvolvedIn;
import acme.entities.tasks.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianInvolvedInCreateService extends AbstractGuiService<Technician, InvolvedIn> {

	@Autowired
	private TechnicianInvolvedInRepository repository;


	@Override
	public void authorise() {
		boolean status = true;
		int masterId;
		MaintenanceRecord maintenanceRecord;
		Technician technician;
		boolean correctTask = true;
		Collection<Task> tasks;
		boolean taskNotMR = false;

		if (super.getRequest().hasData("id")) {
			masterId = super.getRequest().getData("masterId", int.class);
			maintenanceRecord = this.repository.findMaintenanceRecordById(masterId);
			technician = maintenanceRecord == null ? null : maintenanceRecord.getTechnician();
			Integer taskId = super.getRequest().getData("task", int.class);
			tasks = this.repository.findTasksNotInMaintenanceRecord(masterId);
			if (taskId != 0) {
				Task task = this.repository.findTaskById(taskId);
				correctTask = task != null;
				if (correctTask)
					for (Task t : tasks)
						if (t.getId() == taskId) {
							taskNotMR = true;
							break;
						}
			}
			status = maintenanceRecord != null && maintenanceRecord.isDraftMode() && super.getRequest().getPrincipal().hasRealm(technician) && correctTask && taskNotMR;
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		InvolvedIn involvedIn;
		int masterId;

		MaintenanceRecord maintenanceRecord;

		masterId = super.getRequest().getData("masterId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(masterId);

		involvedIn = new InvolvedIn();
		involvedIn.setMaintenanceRecord(maintenanceRecord);

		super.getBuffer().addData(involvedIn);
	}

	@Override
	public void bind(final InvolvedIn involvedIn) {
		int taskId;
		Task task;

		taskId = super.getRequest().getData("task", int.class);
		task = this.repository.findTaskById(taskId);

		super.bindObject(involvedIn);
		involvedIn.setTask(task);
	}

	@Override
	public void validate(final InvolvedIn involvedIn) {
		boolean confirmation;
		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final InvolvedIn involvedIn) {
		this.repository.save(involvedIn);
	}

	@Override
	public void unbind(final InvolvedIn involvedIn) {

		SelectChoices taskChoices;

		Collection<Task> tasks;
		Dataset dataset;

		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);

		tasks = this.repository.findTasksNotInMaintenanceRecord(masterId);

		taskChoices = SelectChoices.from(tasks, "description", involvedIn.getTask());

		dataset = super.unbindObject(involvedIn);
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("tasks", taskChoices);
		dataset.put("task", taskChoices.getSelected().getKey());
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);

	}

}
