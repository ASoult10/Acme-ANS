
package acme.features.administrator.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;

@Repository
public interface AdministratorPassengerRepository extends AbstractRepository {

	@Query("SELECT br.passenger FROM BookingPassenger br WHERE br.booking.id=:bookingId")
	Collection<Passenger> getPassengersFromBookingId(int bookingId);

	@Query("SELECT b FROM Booking b WHERE b.id=:bookingId")
	Booking getBookingById(int bookingId);

}
