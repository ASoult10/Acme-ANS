
package acme.entities.legs;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface LegRepository extends AbstractRepository {

	Leg findByFlightNumber(String flightNumber);

	@Query("select l.scheduledDeparture from Leg l where l.flight.id = :flightId order by l.scheduledDeparture")
	List<Date> findSheduledDeparturesByFlight(Integer flightId, PageRequest page);

	@Query("select l.scheduledArrival from Leg l where l.flight.id = :flightId order by l.scheduledDeparture desc")
	List<Date> findSheduledArrivalsByFlight(Integer flightId, PageRequest page);

	@Query("select l.departureAirport.city from Leg l where l.flight.id = :flightId order by l.scheduledDeparture")
	List<String> findOriginCitiesByFlight(Integer flightId, PageRequest page);

	@Query("select l.arrivalAirport.city from Leg l where l.flight.id = :flightId order by l.scheduledDeparture desc")
	List<String> findDestinationCitiesByFlight(Integer flightId, PageRequest page);

	@Query("select count(l) from Leg l where l.flight.id = :flightId")
	Integer countNumberOfLegsOfFlight(Integer flightId);

	@Query("select count(l) from Leg l where l.flight.id = :flightId and l.draftMode = false")
	Integer countNumberOfPublishedLegsOfFlight(Integer flightId);

	@Query("select l from Leg l where l.flight.id = :flightId order by l.scheduledDeparture")
	List<Leg> findLegsByFlight(Integer flightId);

}
