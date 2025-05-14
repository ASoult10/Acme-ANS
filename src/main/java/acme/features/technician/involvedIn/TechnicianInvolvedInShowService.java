
package acme.features.technician.involvedIn;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.mappings.InvolvedIn;
import acme.entities.tasks.Task;
import acme.entities.tasks.TaskStatus;
import acme.realms.Technician;

@GuiService
public class TechnicianInvolvedInShowService extends AbstractGuiService<Technician, InvolvedIn> {

	@Autowired
	private TechnicianInvolvedInRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Technician technician;
		InvolvedIn involvedIn;

		id = super.getRequest().getData("id", int.class);
		involvedIn = this.repository.findInvolvedInById(id);
		technician = involvedIn == null ? null : involvedIn.getMaintenanceRecord().getTechnician();
		status = involvedIn != null && (!involvedIn.getMaintenanceRecord().isDraftMode() || super.getRequest().getPrincipal().hasRealm(technician));

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		InvolvedIn involvedIn;
		int id;

		id = super.getRequest().getData("id", int.class);
		involvedIn = this.repository.findInvolvedInById(id);

		super.getBuffer().addData(involvedIn);
	}

	@Override
	public void unbind(final InvolvedIn involvedIn) {

		SelectChoices taskChoices;

		Collection<Task> tasks;
		Dataset dataset;

		tasks = this.repository.findAllDisponibleTasks();

		taskChoices = SelectChoices.from(tasks, "description", involvedIn.getTask());

		SelectChoices taskStatus;
		SelectChoices technicianChoices;

		Collection<Technician> technicians;

		technicians = this.repository.findAllTechnicians();
		technicianChoices = SelectChoices.from(technicians, "licenseNumber", involvedIn.getTask().getTechnician());

		taskStatus = SelectChoices.from(TaskStatus.class, involvedIn.getTask().getType());

		dataset = super.unbindObject(involvedIn, "task.type", "task.description", "task.priority", "task.estimatedDuration", "task.draftMode");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("tasks", taskChoices);
		dataset.put("task", taskChoices.getSelected().getKey());
		dataset.put("draftMode", involvedIn.getMaintenanceRecord().isDraftMode());
		dataset.put("taskStatus", taskStatus);
		dataset.put("technicians", technicianChoices);
		dataset.put("technician", technicianChoices.getSelected().getKey());

		super.getResponse().addData(dataset);

	}

}
