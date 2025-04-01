
package acme.features.authenticated.customer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.realms.Customer;

@Repository
public interface AuthenticatedCustomerRepository extends AbstractRepository {

	@Query("SELECT ua FROM UserAccount ua WHERE ua.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("SELECT c FROM Customer c WHERE c.userAccount.id = :id")
	Customer findCustomerByUserAccountId(int id);

	@Query("SELECT c FROM Customer c WHERE c.customerIdentifier =:customerIdentifier")
	Customer findCustomerByCustomerIdentifier(String customerIdentifier);
}
