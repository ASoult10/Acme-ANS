
package acme.entities.claims;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.trackinglogs.TrackingLog;

@Repository
public interface ClaimRepository extends AbstractRepository {

	@Query("select t from TrackingLog t where t.claim.id = :claimId order by t.creationMoment desc")
	Collection<TrackingLog> findAllLogsFromClaimSortedByCreationMoment(@Param("claimId") Integer claimId);

}
