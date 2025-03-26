
package acme.features.authenticated.member;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.Member;

@Repository
public interface MemberRepository extends AbstractRepository {

	Optional<Member> findOneMemberByEmployeeCode(String employeeCode);
	List<Member> findManyMembersByEmployeeCode(String employeeCode);
}
