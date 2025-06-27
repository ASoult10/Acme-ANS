
package acme.entities.airlines;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AirlineRepository extends AbstractRepository {

	@Query("SELECT a FROM Airline a WHERE a.IATA =:IATA")
	Airline findAirlineByIATACode(String IATA);
}
