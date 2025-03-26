
package acme.features.administrator.aircraft;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircrafts.Aircraft;

@Repository
public interface AircraftRepository extends AbstractRepository {

	Optional<Aircraft> findOneAircraftByRegistrationNumber(String registrationNumber);

	@Query("select a from Aircraft a where a.registrationNumber = :registrationNumber")
	List<Aircraft> findAircraftsByRegistrationNumber(String registrationNumber);

}
