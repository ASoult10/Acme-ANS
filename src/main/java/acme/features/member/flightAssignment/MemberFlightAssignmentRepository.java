
package acme.features.member.flightAssignment;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.Member;

@Repository
public interface MemberFlightAssignmentRepository extends AbstractRepository {

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.leg.scheduledArrival < CURRENT_TIMESTAMP")
	List<FlightAssignment> findPastFlightAssignments();

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.leg.scheduledArrival > CURRENT_TIMESTAMP")
	List<FlightAssignment> findNotPastFlightAssignments();

	@Query("SELECT DISTINCT fa.leg FROM FlightAssignment fa WHERE fa.member.id = :memberId")
	Collection<Leg> findLegsByMemberId(int memberId);

	@Query("select l from Leg l where l.id = :legId")
	Leg findLegById(int legId);

	@Query("select m from Member m")
	List<Member> findAllMembers();

	@Query("select l from Leg l")
	List<Leg> findAllLegs();

	@Query("select m from Member m where m.id = :memberId")
	Member findMemberById(int memberId);

	@Query("select f from FlightAssignment f where f.id = :flightAssignmentId")
	FlightAssignment findFlightAssignmentById(int flightAssignmentId);

}
