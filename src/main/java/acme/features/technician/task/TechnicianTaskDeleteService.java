
package acme.features.technician.task;

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
public class TechnicianTaskDeleteService extends AbstractGuiService<Technician, Task> {

	@Autowired
	private TechnicianTaskRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Task task;
		Technician technician;

		masterId = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(masterId);
		technician = task == null ? null : task.getTechnician();
		status = task != null && task.isDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);

		super.getResponse().setAuthorised(status);
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
	public void bind(final Task task) {
		int technicianId;
		Technician technician;

		technicianId = super.getRequest().getData("technician", int.class);
		technician = this.repository.findTechnicianById(technicianId);
		super.bindObject(task, "type", "description", "priority", "estimatedDuration");
		task.setTechnician(technician);
	}

	@Override
	public void validate(final Task task) {
		super.state(true, "*", "technician.task.delete.task-linked");

	}

	@Override
	public void perform(final Task task) {
		Collection<InvolvedIn> involvedInCollection;

		involvedInCollection = this.repository.findInvolvedInFromTaskId(task.getId());
		this.repository.deleteAll(involvedInCollection);
		this.repository.delete(task);
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
