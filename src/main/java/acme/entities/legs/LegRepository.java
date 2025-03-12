
package acme.entities.legs;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface LegRepository extends AbstractRepository {

	@Query("select l.scheduledDeparture from Leg l where l.flight.id = :flightId order by l.scheduledDeparture")
	Date findSheduledDepartureByFlight(Integer flightId);

	@Query("select l.scheduledArrival from Leg l where l.flight.id = :flightId order by l.scheduledDeparture desc")
	Date findSheduledArrivalByFlight(Integer flightId);

	@Query("select l.departureAirport.city from Leg l where l.flight.id = :flightId order by l.scheduledDeparture")
	String findOriginCityByFlight(Integer flightId);

	@Query("select l.arrivalAirport.city from Leg l where l.flight.id = :flightId order by l.scheduledDeparture desc")
	String findDestinationCityByFlight(Integer flightId);

	@Query("select count(l) from Leg l where l.flight.id = :flightId")
	Integer countNumberOfLegsOfFlight(Integer flightId);

	@Query("select l from Leg l where l.flight.id = :flightId order by l.scheduledDeparture")
	List<Leg> findLegsByFlight(Integer flightId);

}
