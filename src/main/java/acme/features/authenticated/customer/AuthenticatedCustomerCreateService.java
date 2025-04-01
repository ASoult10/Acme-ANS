
package acme.features.authenticated.customer;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.Customer;

@GuiService
public class AuthenticatedCustomerCreateService extends AbstractGuiService<Authenticated, Customer> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedCustomerRepository repository;

	// AbstractService<Authenticated, Consumer> ---------------------------


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		Integer userAccountId = super.getRequest().getPrincipal().getAccountId();
		UserAccount userAccount = this.repository.findUserAccountById(userAccountId);

		Customer customer = new Customer();
		customer.setUserAccount(userAccount);
		customer.setPoints(0);

		super.getBuffer().addData(customer);
	}

	@Override
	public void bind(final Customer customer) {
		assert customer != null;

		super.bindObject(customer, "customerIdentifier", "customerPhoneNumber", "physicalAddress", "city", "country");
	}

	@Override
	public void validate(final Customer customer) {
		Customer existing = this.repository.findCustomerByCustomerIdentifier(customer.getCustomerIdentifier());
		boolean valid = existing == null || existing.getId() == customer.getId();
		super.state(valid, "customerIdentifier", "authenticated.customer.form.error.duplicateIdentifier");

	}

	@Override
	public void perform(final Customer customer) {
		assert customer != null;

		this.repository.save(customer);
	}

	@Override
	public void unbind(final Customer customer) {
		Dataset dataset = super.unbindObject(customer, "customerIdentifier", "customerPhoneNumber", "physicalAddress", "city", "country", "points");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
