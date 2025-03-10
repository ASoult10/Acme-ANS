
package acme.realms;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Passenger extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 255)
	private String				fullName;

	@Mandatory
	@Automapped
	@ValidEmail
	private String				email;

	@Mandatory
	@Automapped
	@ValidString(pattern = "^[A-Z0-9]{6,9}$")
	private String				passportNumber;

	@Mandatory
	@Automapped
	@Temporal(TemporalType.TIMESTAMP)
	//@ValidMoment(past = true)
	private Date				birthDate;

	@Optional
	@Automapped
	@ValidString(min = 1, max = 51)
	private String				specialNeeds;
}
