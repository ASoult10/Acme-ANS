
package acme.features.authenticated.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airline;
import acme.entities.flightCrewMembers.AvailabilityStatus;
import acme.realms.Member;

@GuiService
public class AuthenticatedMemberUpdateService extends AbstractGuiService<Authenticated, Member> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedMemberRepository repository;

	// AbstractService interface ----------------------------------------------ç


	@Override
	public void authorise() {
		boolean status = true;
		if (super.getRequest().getMethod().equals("POST"))
			try {

				List<Airline> airlines = this.repository.findAllAirlines();
				Integer id = super.getRequest().getData("airline", Integer.class);
				if (id != 0) {

					Airline airline = this.repository.findAirlineById(id);
					if (!airlines.contains(airline))
						status = false;
				}
			} catch (Throwable e) {
				status = false;
			}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Member object;
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		object = this.repository.findMemberByUserAccountId(userAccountId);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Member member) {
		assert member != null;

		super.bindObject(member, "employeeCode", "phoneNumber", "languageSkills", "availabilityStatus", "salary", "yearsOfExperience", "airline");
	}

	@Override
	public void validate(final Member object) {
	}

	@Override
	public void perform(final Member modifiedMember) {
		this.repository.save(modifiedMember);
	}

	@Override
	public void unbind(final Member member) {

		List<Airline> airlines = this.repository.findAllAirlines();
		SelectChoices airlineChoices = SelectChoices.from(airlines, "IATA", member.getAirline());

		SelectChoices availabilityStatus = SelectChoices.from(AvailabilityStatus.class, member.getAvailabilityStatus());

		Dataset dataset = super.unbindObject(member, "employeeCode", "phoneNumber", "languageSkills", "availabilityStatus", "salary", "yearsOfExperience", "airline");

		dataset.put("availabilityStatus", availabilityStatus);
		dataset.put("airline", airlineChoices.getSelected().getKey());
		dataset.put("airlines", airlineChoices);
		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
