
package acme.features.customer.bookingPassenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;

@Repository
public interface CustomerBookingPassengerRepository extends AbstractRepository {

	@Query("SELECT b FROM Booking b WHERE b.id=:bookingId")
	Booking getBookingById(Integer bookingId);

	@Query("SELECT p FROM Passenger p WHERE p.id=:passengerId")
	Passenger getPassengerById(Integer passengerId);

	@Query("SELECT p FROM Passenger p WHERE p.customer.id=:customerId AND p.isPublished = true")
	Collection<Passenger> getAllPassengersByCustomerId(Integer customerId);

	@Query("SELECT br.passenger FROM BookingPassenger br WHERE br.booking.id=:bookingId")
	Collection<Passenger> getPassengersInBooking(Integer bookingId);

}
