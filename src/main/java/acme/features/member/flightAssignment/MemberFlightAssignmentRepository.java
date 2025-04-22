
package acme.features.member.flightAssignment;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.Member;

@Repository
public interface MemberFlightAssignmentRepository extends AbstractRepository {

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.leg.scheduledArrival < :currentMoment AND fa.draftMode = false")
	Collection<FlightAssignment> findCompletedPublishedFlightAssignments(Date currentMoment);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.leg.scheduledArrival > :currentMoment AND fa.draftMode = false")
	Collection<FlightAssignment> findNotCompletedPublishedFlightAssignments(Date currentMoment);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.leg.scheduledArrival < :currentMoment AND fa.member.id = :memberId")
	Collection<FlightAssignment> findMyCompletedFlightAssignments(Date currentMoment, int memberId);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.leg.scheduledArrival > :currentMoment AND fa.member.id = :memberId")
	Collection<FlightAssignment> findMyNotCompletedFlightAssignments(Date currentMoment, int memberId);

	@Query("select a from ActivityLog a where a.flightAssignment.id = :flightAssignmentId")
	Collection<ActivityLog> getActivityLogByFlightAssignmentId(int flightAssignmentId);

	@Query("SELECT DISTINCT fa.leg FROM FlightAssignment fa WHERE fa.member.id = :memberId")
	List<Leg> findLegsByMemberId(int memberId);

	@Query("SELECT DISTINCT fa.member FROM FlightAssignment fa WHERE fa.leg.id = :legId")
	List<Member> findMembersByLegId(int legId);

	@Query("select l from Leg l where l.id = :legId")
	Leg findLegById(int legId);

	@Query("SELECT m FROM Member m WHERE m.availabilityStatus = 'AVAILABLE'")
	List<Member> findAllAvailableMembers();

	@Query("select fa from FlightAssignment fa WHERE fa.leg.id = :legId")
	List<FlightAssignment> findFlightAssignmentByLegId(int legId);

	@Query("select fa from FlightAssignment fa WHERE fa.member.id = :memberId")
	List<FlightAssignment> findFlightAssignmentByMemberId(int memberId);

	@Query("select l from Leg l")
	List<Leg> findAllLegs();

	@Query("select l from Leg l WHERE l.scheduledArrival > :currentMoment AND l.draftMode = false")
	List<Leg> findAllNotCompletedPublishedLegs(Date currentMoment);

	@Query("select m from Member m where m.id = :memberId")
	Member findMemberById(int memberId);

	@Query("select m from Member m where m.employeeCode = :employeeCode")
	Member findMemberByEmployeeCode(String employeeCode);

	@Query("select f from FlightAssignment f where f.id = :flightAssignmentId")
	FlightAssignment findFlightAssignmentById(int flightAssignmentId);

}
