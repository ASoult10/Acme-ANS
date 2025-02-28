
package acme.entities.passenger;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import acme.client.components.datatypes.Moment;
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
public class Passenger {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 255)
	private String				fullName;

	@Mandatory
	@ManyToOne
	private Booking				booking;

	@Mandatory
	@ValidEmail
	private String				email;

	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,9}$")
	private String				passportNumber;

	@Mandatory // TODO: Como dice la mujer, in the past?
	private Moment				birthDate;

	@Optional
	@ValidString(min = 1, max = 51)
	private String				specialNeeds;
}
