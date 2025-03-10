
package acme.entities.service;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.entities.airports.Airport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Service extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	private String				name;

	@Mandatory
	@Automapped
	@ManyToOne
	private Airport				airport;

	@Mandatory
	@Automapped
	@ValidUrl
	private String				imageLink;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0)
	private Integer				dWellTime;

	@Optional
	@ValidString(pattern = "^[A-Z]{4}-[0-9]{2}$")
	@Column(unique = true)
	private String				promoteCode;

	@Mandatory
	@Automapped
	@ValidMoney
	private Money				moneyDiscounted;

}
