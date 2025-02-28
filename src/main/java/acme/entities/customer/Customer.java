
package acme.entities.customer;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends AbstractEntity { // TODO: AbstractEntity o AbstractRole?

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory // TODO: Automapped se pone?
	@ValidString(min = 1, max = 50)
	private String				name;

	@Mandatory
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z]{2-3}\b{6}$") // TODO: Cambiar b por d; TODO: Comprobar que los tres primeros caracteres son del nombre?
	private String				identifier;

	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	private String				phoneNumber;

	@Mandatory
	@ValidString(min = 0, max = 255)
	private String				physicalAddress;

	@Mandatory
	@ValidString(min = 0, max = 50)
	private String				city;

	@Mandatory
	@ValidString(min = 0, max = 50)
	private String				country;

	@Optional
	@ValidNumber(min = 0, max = 500000)
	private Integer				points;
}
