
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
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
public class Aircraft extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Valid
	@Automapped
	@Size(max = 50)
	private String				model;

	@Mandatory
	@Valid
	@Automapped
	@Column(unique = true)
	@ValidString(min = 1, max = 255)
	private String				registrationNumber;

	@Mandatory
	@ValidNumber
	@Automapped
	private Integer				capacity;

	@Mandatory
	@ValidNumber
	@Automapped
	@Min(2000)
	@Max(50000)
	private Integer				cargoWeight;

	@Mandatory
	@Valid
	@Automapped
	private AircraftStatus		status;

	@Optional
	@ValidUrl
	@Automapped
	private String				details;

}
