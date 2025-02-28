
package acme.entities.service;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
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
	@ValidString(min = 1, max = 50)
	private String				name;

	@Mandatory
	@ManyToOne
	private Airport				airport;

	@Mandatory
	@ValidUrl // TODO: Url or URI?
	// TODO: Store where?
	private String				link;

	@Mandatory
	@ValidNumber(min = 0)
	private Integer				dWellTime;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{4}-[0-9]{2}$")
	@Column(unique = true)
	// TODO: Two lasts digits -> Current year?
	private String				promoteCode;

	@Mandatory
	@ValidMoney
	private Money				moneyDiscounted;

}
