
package acme.entities.technicians;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Technician extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2-3}\\d{6}$")
	@Automapped
	@Column(unique = true)
	private String				licenseNumber;

	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				specialisation;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				annualHealthTest;

	@Mandatory
	@ValidNumber(min = 0)
	@Automapped
	private Integer				yearsOfExperience;

	@Optional
	@ValidUrl
	@Automapped
	private String				certifications;

}
