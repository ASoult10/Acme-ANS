
package acme.features.authenticated.customer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.Customer;

@Repository
public interface CustomerRepository extends AbstractRepository {

	@Query("SELECT c FROM Customer c WHERE c.customerIdentifier = :customerIdentifier")
	Customer findCustomerByIdentifier(String customerIdentifier);

	@Query("SELECT c FROM Customer c WHERE c.id = :userAccountId") // TODO: Verificar
	Customer findCustomerByUserAccountId(Integer userAccountId);
}
