
package acme.features.administrator.airline;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.airlines.Airline;

public interface AdministratorAirlineRepository extends AbstractRepository {

	@Query("select a from  airline a where a.id = :id")
	Airline findAirlineById(int id);

}
