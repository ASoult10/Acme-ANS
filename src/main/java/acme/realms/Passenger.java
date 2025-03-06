
package acme.realms;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Moment;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidString;
import acme.entities.booking.Booking;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Passenger extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 255)
	private String				fullName;

	@Mandatory
	@Automapped
	@ManyToOne
	private Booking				booking;

	@Mandatory
	@Automapped
	@ValidEmail
	private String				email;

	@Mandatory
	@Automapped
	@ValidString(pattern = "^[A-Z0-9]{6,9}$")
	private String				passportNumber;

	@Mandatory
	private Moment				birthDate;

	@Optional
	@Automapped
	@ValidString(min = 1, max = 51)
	private String				specialNeeds;
}
