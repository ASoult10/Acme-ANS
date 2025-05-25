
package acme.features.authenticated.member;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.airlines.Airline;
import acme.realms.Member;

@Repository
public interface AuthenticatedMemberRepository extends AbstractRepository {

	@Query("SELECT u FROM UserAccount u WHERE u.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("SELECT a FROM Airline a")
	List<Airline> findAllAirlines();

	@Query("SELECT a FROM Airline a WHERE a.id = :id")
	Airline findAirlineById(Integer id);

	@Query("SELECT c FROM Member c WHERE c.userAccount.id = :id")
	Member findMemberByUserAccountId(int id);

	@Query("SELECT c FROM Member c WHERE c.employeeCode =:EmployeeCode")
	Member findMemberByEmployeeCode(String EmployeeCode);
}
