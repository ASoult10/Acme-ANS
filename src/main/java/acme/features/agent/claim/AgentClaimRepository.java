
package acme.features.agent.claim;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.legs.Leg;

@Repository
public interface AgentClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where c.status != acme.entities.TrackingLogStatus.PENDING")
	Collection<Claim> findCompletedClaims();

	@Query("select c from Claim c where c.status != acme.entities.TrackingLogStatus.PENDING and c.assistanceAgent.id == :id")
	Collection<Claim> findCompletedClaimsByAgent(int id);

	@Query("select c from Claim c where c.status == acme.entities.TrackingLogStatus.PENDING")
	Collection<Claim> findUndergoingClaims();

	@Query("select c from Claim c where c.status != acme.entities.TrackingLogStatus.PENDING and c.assistanceAgent.id == :id")
	Collection<Claim> findUndergoingClaimsByAgent(int id);

	@Query("select c from Claim c where c.id == :id")
	Claim findClaimById(int id);

	@Query("select l from Leg l where l.id = :legId")
	Leg findLegById(int legId);

	@Query("select l from Leg l")
	Collection<Leg> findAllLegs();

	//TODO: queda comprobar si esta publicado
	@Query("select l from Leg l where l.scheduledArrival < :currentMoment")
	Collection<Leg> findAllPublishedCompletedLegs(Date currentMoment);
}
