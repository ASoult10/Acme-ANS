
package acme.features.flightAssignment;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.FlightAssignment;

@Repository
public interface FlightAssignmentRepository extends AbstractRepository {

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.leg.scheduledArrival < CURRENT_TIMESTAMP")
	List<FlightAssignment> findPastFlightAssignments();

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.leg.scheduledArrival > CURRENT_TIMESTAMP")
	List<FlightAssignment> findNotPastFlightAssignments();

}
