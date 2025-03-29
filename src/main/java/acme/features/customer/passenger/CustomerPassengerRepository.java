
package acme.features.customer.passenger;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.passenger.Passenger;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("SELECT b.passenger FROM BookingPassenger b WHERE b.booking.id = :bookingId")
	List<Passenger> findPassengerByBookingId(Integer bookingId);

}
