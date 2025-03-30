
package acme.features.technician.involvedIn;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.mappings.InvolvedIn;
import acme.realms.Technician;

@GuiService
public class TechnicianInvolvedInListService extends AbstractGuiService<Technician, InvolvedIn> {

	@Autowired
	private TechnicianInvolvedInRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<InvolvedIn> involvedInCollection;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		involvedInCollection = this.repository.findInvolvedInByMaintenanceRecordId(masterId);

		super.getBuffer().addData(involvedInCollection);
	}

	@Override
	public void unbind(final Collection<InvolvedIn> involvedInCollection) {
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);

		super.getResponse().addGlobal("masterId", masterId);

	}

	@Override
	public void unbind(final InvolvedIn involvedIn) {

		Dataset dataset = super.unbindObject(involvedIn);
		dataset.put("task", involvedIn.getTask().getDescription());

		super.getResponse().addData(dataset);

	}

}
