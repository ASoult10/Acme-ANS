
package acme.features.customer.passenger;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.realms.Customer;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	List<Booking> findManyBookingsByLocatorCode(String locatorCode);

	@Query("SELECT c FROM Customer c WHERE c.id = :userAccountId") // TODO: Verificar
	Customer findCustomerByUserAccountId(Integer userAccountId);

}
