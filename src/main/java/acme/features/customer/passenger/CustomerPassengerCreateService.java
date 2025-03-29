
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerCreateService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository customerPassengerRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		// TODO: Hacer
	}

	@Override
	public void bind(final Passenger passenger) {
		int contratorId; // TODO: Hacerlo

		contratorId = super.getRequest().getData("contractor", int.class);
		super.bindObject(passenger, "fullName", "email", "passportNumber", "birthDate", "specialNeeds");

	}

	@Override
	public void validate(final Passenger passenger) {
		// TODO: Published solo cuando se guarda el last nibble
	}

	@Override
	public void perform(final Passenger passenger) {
		this.customerPassengerRepository.save(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		assert passenger != null;

		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "birthDate", "specialNeeds");
		super.getResponse().addData(dataset);
	}

}
