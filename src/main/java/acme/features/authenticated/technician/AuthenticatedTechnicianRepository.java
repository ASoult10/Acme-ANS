
package acme.features.authenticated.technician;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.realms.Technician;

@Repository
public interface AuthenticatedTechnicianRepository extends AbstractRepository {

	@Query("SELECT u FROM UserAccount u WHERE u.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("SELECT t FROM Technician t WHERE t.userAccount.id = :id")
	Technician findTechnicianByUserAccountId(int id);

	@Query("SELECT t FROM Technician t WHERE t.licenseNumber = :licenseNumber")
	Technician findTechnicianByTechnicianLicenseNumber(String licenseNumber);
}
