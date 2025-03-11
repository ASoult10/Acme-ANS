
package acme.entities.service;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidScore;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
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
	@Automapped
	private String				name;

	@Mandatory
	@Automapped
	@ValidUrl
	private String				imageLink;

	@Mandatory
	@ValidNumber(min = 0, max = 100, fraction = 2) // TODO: 2 numeros decimales
	@Automapped
	private Double				dWellTime;

	@Optional
	@ValidString(pattern = "^[A-Z]{4}-[0-9]{2}$") // TODO: Custom validator (servicio) para dos últimos dígitos --> año
	@Column(unique = true)
	private String				promoteCode;

	@Optional
	@ValidScore
	@Automapped
	private Double				discount;

}
