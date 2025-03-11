
package acme.entities.legs;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface LegRepository extends AbstractRepository {

	@Query("select l from Leg l where l.flight.id = :flightId order by l.scheduledDeparture")
	Leg findFirstLegByFlight(Integer flightId);

	@Query("select l from Leg l where l.flight.id = :flightId order by l.scheduledDeparture desc")
	Leg findLastLegByFlight(Integer flightId);

	@Query("select count(l) from Leg l where l.flight.id = :flightId")
	Integer countNumberOfLegsOfFlight(Integer flightId);

	@Query("select l from Leg l where l.flight.id = :flightId order by l.scheduledDeparture")
	List<Leg> findLegsByFlight(Integer flightId);

}
