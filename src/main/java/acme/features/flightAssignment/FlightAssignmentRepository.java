
package acme.features.flightAssignment;

import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface FlightAssignmentRepository extends AbstractRepository {

	//@Query("select a from FlightAssignment a where a.registrationNumber = :registrationNumber")
	//List<FlightAssignment> findFlightAssignmentByRegistrationNumber(String registrationNumber);

}
