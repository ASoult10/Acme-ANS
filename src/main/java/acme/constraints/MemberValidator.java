
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.principals.UserAccount;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.features.authenticated.member.AuthenticatedMemberRepository;
import acme.realms.Member;

@Validator
public class MemberValidator extends AbstractValidator<ValidMember, Member> {

	@Autowired
	private AuthenticatedMemberRepository repository;


	@Override
	protected void initialise(final ValidMember annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Member member, final ConstraintValidatorContext context) {

		String identifier = member.getEmployeeCode();

		if (identifier == null)
			return false;
		if (identifier.length() < 2) {
			super.state(context, false, "employeeCode", "{acme.validation.member.nullornotpattern.message}");
			return false;
		}
		UserAccount userAccount = member.getUserAccount();
		if (userAccount == null || userAccount.getIdentity() == null)
			return false;
		DefaultUserIdentity identity = member.getUserAccount().getIdentity();

		if (identity.getName() == null || identity.getName().isBlank() || identity.getSurname() == null || identity.getSurname().isBlank())
			return false;

		String inicialNombre = String.valueOf(identity.getName().charAt(0)).toUpperCase();
		String inicialApellido = String.valueOf(identity.getSurname().charAt(0)).toUpperCase();

		String iniciales = inicialNombre + inicialApellido;

		String employeeCodeInitials = identifier.substring(0, 2);

		if (!iniciales.equals(employeeCodeInitials)) {
			super.state(context, false, "employeeCode", "{acme.validation.member.notInitials.message}");
			return false;
		}

		Member memberWithSameCode = this.repository.findMemberByEmployeeCode(identifier);
		if (memberWithSameCode != null && memberWithSameCode.getId() != member.getId()) {
			super.state(context, false, "employeeCode", "{acme.validation.member.repeated.message}: " + identifier);
			return false;
		}

		return true;
	}

}
