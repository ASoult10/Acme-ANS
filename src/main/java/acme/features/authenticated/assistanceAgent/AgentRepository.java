
package acme.features.authenticated.assistanceAgent;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.AssistanceAgent;

@Repository
public interface AgentRepository extends AbstractRepository {

	@Query("select a from AssistanceAgent a where a.employeeCode = :employeeCode")
	Optional<AssistanceAgent> findOneAgentByEmployeeCode(String employeeCode);
}
