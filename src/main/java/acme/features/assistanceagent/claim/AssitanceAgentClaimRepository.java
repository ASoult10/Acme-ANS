
package acme.features.assistanceagent.claim;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.legs.Leg;
import acme.entities.trackinglogs.TrackingLog;
import acme.entities.trackinglogs.TrackingLogStatus;

@Repository
public interface AssitanceAgentClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where c.status != :status")
	Collection<Claim> findCompletedClaims(TrackingLogStatus status);

	@Query("select c from Claim c where c.status != :status and c.assistanceAgent.id = :id")
	Collection<Claim> findCompletedClaimsByAgent(int id, TrackingLogStatus status);

	@Query("select c from Claim c where c.status = :status")
	Collection<Claim> findUndergoingClaims(TrackingLogStatus status);

	@Query("select c from Claim c where c.status = :status and c.assistanceAgent.id = :id")
	Collection<Claim> findUndergoingClaimsByAgent(int id, TrackingLogStatus status);

	@Query("select c from Claim c where c.id = :id")
	Claim findClaimById(int id);

	@Query("select l from Leg l where l.id = :legId")
	Leg findLegById(int legId);

	@Query("select l from Leg l")
	Collection<Leg> findAllLegs();

	@Query("select l from Leg l where l.scheduledArrival < :currentMoment and l.draftMode is false")
	Collection<Leg> findAllPublishedCompletedLegs(Date currentMoment);

	@Query("select t from TrackingLog t where t.claim.id = :claimId order by t.creationMoment desc")
	TrackingLog findLastLog(int claimId);
}
