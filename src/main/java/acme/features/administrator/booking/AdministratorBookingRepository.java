
package acme.features.administrator.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.flights.Flight;

@Repository
public interface AdministratorBookingRepository extends AbstractRepository {

	@Query("SELECT b FROM Booking b WHERE b.id = :bookingId")
	Booking findBookingById(int bookingId);

	@Query("SELECT f FROM Flight f")
	Collection<Flight> findAllFlights();

	@Query("SELECT b FROM Booking b WHERE b.isPublished = true")
	Collection<Booking> findAllPublishedBookings();

}
