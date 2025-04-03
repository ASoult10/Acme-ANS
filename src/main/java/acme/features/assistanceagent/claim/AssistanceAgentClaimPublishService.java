
package acme.features.assistanceagent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimType;
import acme.entities.legs.Leg;
import acme.entities.trackinglogs.TrackingLogStatus;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimPublishService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssitanceAgentClaimRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int claimId;
		Claim selectedClaim;
		AssistanceAgent agent;

		claimId = super.getRequest().getData("id", int.class);
		selectedClaim = this.repository.findClaimById(claimId);

		agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class) && selectedClaim.getAssistanceAgent().equals(agent);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		Claim claim;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		claim.setLeg(leg);
		super.bindObject(claim, "email", "description", "type", "status");
	}

	@Override
	public void validate(final Claim claim) {
		//validar que si status no es pending, es pq tiene un log asociado de 100%
		if (claim.getStatus() != TrackingLogStatus.PENDING && this.repository.findLastLog(claim.getId()).getResolutionPercentage() < 100)
			super.state(claim.getStatus() != TrackingLogStatus.PENDING, "status", "assistanceAgent.claim.form.error.status");

	}

	@Override
	public void perform(final Claim claim) {
		claim.setDraftMode(false);
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		SelectChoices choices_type;
		SelectChoices choices_leg;
		SelectChoices choices_status;
		Dataset dataset;
		Collection<Leg> legs;

		legs = this.repository.findAllPublishedCompletedLegs(claim.getRegistrationMoment());

		choices_type = SelectChoices.from(ClaimType.class, claim.getType());
		choices_status = SelectChoices.from(TrackingLogStatus.class, claim.getStatus());
		choices_leg = SelectChoices.from(legs, "flightNumber", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "email", "description", "assistanceAgent", "draftMode");
		dataset.put("type", choices_type);
		dataset.put("status", choices_status);
		dataset.put("legs", choices_leg);
		dataset.put("legFlightNumber", claim.getLeg().getFlightNumber());

		super.getResponse().addData(dataset);
	}

}
