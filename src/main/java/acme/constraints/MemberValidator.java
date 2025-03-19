
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.principals.UserAccount;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.flightCrewMembers.MemberRepository;
import acme.realms.Member;

@Validator
public class MemberValidator extends AbstractValidator<ValidMember, Member> {

	@Autowired
	private MemberRepository repository;


	@Override
	protected void initialise(final ValidMember annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Member member, final ConstraintValidatorContext context) {

		String identifier = member.getEmployeeCode();

		if (identifier == null || identifier.length() < 2) {
			super.state(context, false, "*", "{acme.validation.identifier.nullornotpattern.message}");
			return false;
		}

		UserAccount userAccount = member.getUserAccount();
		if (userAccount == null || userAccount.getIdentity() == null) {
			super.state(context, false, "*", "{javax.validation.constraints.NotNull.message}");
			return false;
		}
		DefaultUserIdentity identity = member.getUserAccount().getIdentity();

		if (identity.getName() == null || identity.getName().isBlank() || identity.getSurname() == null || identity.getSurname().isBlank()) {
			super.state(context, false, "*", "{javax.validation.constraints.NotNull.message}");
			return false;
		}
		String inicialNombre = String.valueOf(identity.getName().charAt(0)).toUpperCase();
		String inicialApellido = String.valueOf(identity.getSurname().charAt(0)).toUpperCase();

		String iniciales = inicialNombre + inicialApellido;

		String employeeCodeInitials = identifier.substring(0, 2);

		if (!iniciales.equals(employeeCodeInitials)) {
			super.state(context, false, "*", "{acme.validation.identifier.notInitials.message}");
			return false;
		}

		List<Member> memberWithSameCode = this.repository.findManyMembersByEmployeeCode(identifier);
		for (Integer i = 0; i < memberWithSameCode.size(); i++)
			if (memberWithSameCode.get(i).getId() != member.getId()) {
				super.state(context, false, "*", "Estaba ya" + identifier);
				return false;
			}

		return true;
	}

}
