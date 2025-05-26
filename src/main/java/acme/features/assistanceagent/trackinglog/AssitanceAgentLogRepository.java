
package acme.features.assistanceagent.trackinglog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.trackinglogs.TrackingLog;

@Repository
public interface AssitanceAgentLogRepository extends AbstractRepository {

	@Query("select l from TrackingLog l")
	Collection<TrackingLog> findAllLogs();

	@Query("select l from TrackingLog l where l.claim.id = :claimId")
	Collection<TrackingLog> findLogsByClaim(int claimId);

	@Query("select l from TrackingLog l where l.id = :id")
	TrackingLog findLogById(int id);

	@Query("select c from Claim c where c.id = :claimId")
	Claim findClaimById(int claimId);

	@Query("select t from TrackingLog t where t.claim.id = :claimId order by t.creationMoment desc")
	TrackingLog findLastLog(int claimId);

	@Query("select count(t) from TrackingLog t where t.claim.id = :claimId and t.resolutionPercentage = 100.00")
	int countResolvedLogs(int claimId);

}
