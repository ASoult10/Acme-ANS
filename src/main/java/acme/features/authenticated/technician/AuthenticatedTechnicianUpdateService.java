
package acme.features.authenticated.technician;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.Technician;

@GuiService
public class AuthenticatedTechnicianUpdateService extends AbstractGuiService<Authenticated, Technician> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedTechnicianRepository authenticatedTechnicianRepository;

	// AbstractService interface ----------------------------------------------ç


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Technician.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Technician object;
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		object = this.authenticatedTechnicianRepository.findTechnicianByUserAccountId(userAccountId);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Technician object) {
		assert object != null;

		super.bindObject(object, "licenseNumber", "phoneNumber", "specialisation", "annualHealthTest", "yearsOfExperience", "certifications");
	}

	@Override
	public void validate(final Technician object) {
		//		assert object != null;
		//		Customer existing = this.authenticatedCustomerRepository.findCustomerByCustomerIdentifier(object.getCustomerIdentifier());
		//		boolean valid = existing == null || existing.getId() == object.getId();
		//		super.state(valid, "identifier", "authenticated.customer.form.error.duplicateIdentifier");
	}

	@Override
	public void perform(final Technician modifiedTechnician) {
		assert modifiedTechnician != null;

		Technician newTechnician = (Technician) this.authenticatedTechnicianRepository.findById(modifiedTechnician.getId()).get();
		newTechnician.setLicenseNumber(modifiedTechnician.getLicenseNumber());
		newTechnician.setPhoneNumber(modifiedTechnician.getPhoneNumber());
		newTechnician.setAnnualHealthTest(modifiedTechnician.getAnnualHealthTest());
		newTechnician.setCertifications(modifiedTechnician.getCertifications());
		newTechnician.setSpecialisation(modifiedTechnician.getSpecialisation());
		newTechnician.setYearsOfExperience(modifiedTechnician.getYearsOfExperience());

		this.authenticatedTechnicianRepository.save(newTechnician);
	}

	@Override
	public void unbind(final Technician object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbindObject(object, "licenseNumber", "phoneNumber", "specialisation", "annualHealthTest", "yearsOfExperience", "certifications");
		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
