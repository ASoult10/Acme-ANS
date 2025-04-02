
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.tasks.Task;
import acme.entities.tasks.TaskStatus;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskShowService extends AbstractGuiService<Technician, Task> {

	@Autowired
	private TechnicianTaskRepository repository;


	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);
	}
	@Override
	public void load() {
		Task task;
		int id;

		id = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(id);

		super.getBuffer().addData(task);
	}

	@Override
	public void unbind(final Task task) {
		SelectChoices taskStatus;
		SelectChoices technicianChoices;

		Collection<Technician> technicians;
		Dataset dataset;

		technicians = this.repository.findAllTechnicians();
		technicianChoices = SelectChoices.from(technicians, "licenseNumber", task.getTechnician());

		taskStatus = SelectChoices.from(TaskStatus.class, task.getType());

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("taskStatus", taskStatus);
		dataset.put("technicians", technicianChoices);
		dataset.put("technician", technicianChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
