
package acme.features.customer.booking;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.realms.Customer;

@Repository
public interface CustomerBookingRepository extends AbstractRepository {

	@Query("SELECT c FROM Customer c WHERE c.id = :userAccountId") // TODO: Verificar
	Customer findCustomerByUserAccountId(Integer userAccountId);

	@Query("SELECT b FROM Booking b WHERE b.locatorCode = :locatorCode")
	Booking findBookingByLocatorCode(String locatorCode);

}
