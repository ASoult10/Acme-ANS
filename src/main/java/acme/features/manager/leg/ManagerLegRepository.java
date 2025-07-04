
package acme.features.manager.leg;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airlines.Airline;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;

@Repository
public interface ManagerLegRepository extends AbstractRepository {

	@Query("select f from Flight f where f.id = :id")
	Flight findFlightById(int id);

	@Query("select l.flight from Leg l where l.id = :id")
	Flight findFlightByLegId(int id);

	@Query("select l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("select l from Leg l where l.flight.id = :masterId")
	Collection<Leg> findLegsByMasterId(int masterId);

	@Query("select a from Airline a")
	Collection<Airline> findAllAirlines();

	@Query("select a from Airline a where a.id = :id")
	Airline findAirlineById(int id);

	@Query("select a from Airport a")
	Collection<Airport> findAllAirports();

	@Query("select a from Airport a where a.id = :id")
	Airport findAirportById(int id);

	@Query("select count(l) from Leg l where l.draftMode = false and l.aircraft.id = :aircraftId and ((l.scheduledDeparture >= :departure and l.scheduledDeparture <= :arrival) or (l.scheduledArrival >= :departure and l.scheduledArrival <= :arrival) or (l.scheduledDeparture <= :departure and l.scheduledArrival >= :arrival))")
	Integer countNumberOfPublishedLegsInIntervalWithAircraft(int aircraftId, Date departure, Date arrival);

	@Query("select a from Aircraft a where a.status = acme.entities.aircrafts.AircraftStatus.ACTIVE")
	Collection<Aircraft> findAllActiveAircrafts();

	@Query("select a from Aircraft a where a.id = :id")
	Aircraft findAircraftById(int id);

	@Query("select l from Leg l where l.flight.id = :flightId order by l.scheduledDeparture")
	List<Leg> findLegsByFlight(Integer flightId);

}
