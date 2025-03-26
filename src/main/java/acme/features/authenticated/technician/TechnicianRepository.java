
package acme.features.authenticated.technician;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.Technician;

@Repository
public interface TechnicianRepository extends AbstractRepository {

	Optional<Technician> findOneTechnicianByLicenseNumber(String licenseNumber);

	@Query("select t from Technician t where t.licenseNumber = :licenseNumber")
	List<Technician> findTechniciansByLicenseNumber(String licenseNumber);
}
