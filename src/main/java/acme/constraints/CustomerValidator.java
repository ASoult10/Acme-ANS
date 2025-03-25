
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.features.authenticated.customer.CustomerRepository;
import acme.realms.Customer;

@Validator
public class CustomerValidator extends AbstractValidator<ValidCustomer, Customer> {

	@Autowired
	private CustomerRepository repository;


	@Override
	protected void initialise(final ValidCustomer annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {

		String identifier = customer.getCustomerIdentifier();

		if (identifier == null) {
			super.state(context, false, "*", "{acme.validation.identifier.nullornotpattern.message}");
			return false;
		}

		DefaultUserIdentity identity = customer.getUserAccount().getIdentity();

		String inicialNombre = String.valueOf(identity.getName().charAt(0)).toUpperCase();
		String inicial1Apellido = String.valueOf(identity.getSurname().charAt(0)).toUpperCase();
		Integer initialsLenght = 2;

		String iniciales = inicialNombre + inicial1Apellido;

		String identifierInitials = identifier.substring(0, initialsLenght);

		if (!iniciales.equals(identifierInitials)) {
			super.state(context, false, "*", "{acme.validation.identifier.notInitials.message}");
			return false;
		}

		Customer customerWithSameIdentifier = this.repository.findCustomerByIdentifier(identifier);
		if (customerWithSameIdentifier != null && customerWithSameIdentifier.getId() != customer.getId()) {
			super.state(context, false, "*", "{acme.validation.identifier.repeated.message}: " + identifier);
			return false;
		}
		return true;
	}

}
