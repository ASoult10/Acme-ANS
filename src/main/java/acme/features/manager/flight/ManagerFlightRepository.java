
package acme.features.manager.flight;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingPassenger;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;

@Repository
public interface ManagerFlightRepository extends AbstractRepository {

	@Query("select f from Flight f where f.id = :id")
	Flight findFlightById(int id);

	@Query("select f from Flight f where f.manager.id = :managerId")
	Collection<Flight> findFlightByManagerId(int managerId);

	@Query("select l from Leg l where l.flight.id = :flightId")
	Collection<Leg> findLegsByFlightId(int flightId);

	@Query("select b from Booking b where b.flight.id = :flightId")
	Collection<Booking> findBookingsByFlightId(Integer flightId);

	@Query("select b from BookingPassenger b where b.booking.id in :bookingIds")
	Collection<BookingPassenger> findBookingPassengersByBookingIds(Collection<Integer> bookingIds);

	@Query("select count(l) from Leg l where l.flight.id = :flightId")
	Integer countNumberOfLegsOfFlight(Integer flightId);

	@Query("select count(l) from Leg l where l.flight.id = :flightId and l.draftMode = false")
	Integer countNumberOfPublishedLegsOfFlight(Integer flightId);

	@Query("select l from Leg l where l.flight.id = :flightId order by l.scheduledDeparture")
	List<Leg> findLegsByFlight(Integer flightId);

}
