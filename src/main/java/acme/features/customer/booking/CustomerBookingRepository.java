
package acme.features.customer.booking;

import java.util.Collection;

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

	@Query("SELECT b FROM Booking b") // TODO:  WHERE b.customer.id = :customerId
	Collection<Booking> findBookingsByCustomer(Integer customerId);

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingById(int id);

}
