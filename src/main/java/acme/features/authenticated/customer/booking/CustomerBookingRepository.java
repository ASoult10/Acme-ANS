
package acme.features.authenticated.customer.booking;

import java.util.List;

import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;

@Repository
public interface CustomerBookingRepository extends AbstractRepository {

	List<Booking> findManyBookingsByLocatorCode(String locatorCode);

}
