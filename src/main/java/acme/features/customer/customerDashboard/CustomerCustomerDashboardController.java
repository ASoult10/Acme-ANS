
package acme.features.customer.customerDashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.forms.customers.CustomerDashboard;
import acme.realms.Customer;

@GuiController
public class CustomerCustomerDashboardController extends AbstractGuiController<Customer, CustomerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerCustomerDashboardShowService customerCustomerDashboardService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.customerCustomerDashboardService);
	}

}
