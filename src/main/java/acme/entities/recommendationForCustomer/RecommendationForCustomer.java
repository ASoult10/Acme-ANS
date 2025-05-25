
package acme.entities.recommendationForCustomer;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RecommendationForCustomer extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString
	@Automapped
	private String				city;

	@Mandatory
	@ValidString
	@Automapped
	private String				country;

	@Mandatory
	@ValidString
	@Automapped
	private String				name;

	@Mandatory
	@ValidString
	@Automapped
	private String				recommendationType;

}
