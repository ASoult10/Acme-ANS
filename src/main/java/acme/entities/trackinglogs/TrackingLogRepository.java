
package acme.entities.trackinglogs;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("select t from TrackingLog t")
	List<TrackingLog> findAllLogs();

	@Query("select t from TrackingLog t where t.claim.id = :claimId")
	List<TrackingLog> findAllLogsFromClaim(@Param("claimId") Integer claimId);

	@Query("select t from TrackingLog t where t.claim.id = :claimId order by t.creationMoment")
	List<TrackingLog> findAllLogsFromClaimSortedByCreationMoment(@Param("claimId") Integer claimId);

}
