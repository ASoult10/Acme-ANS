
package acme.entities.booking;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import acme.client.components.datatypes.Moment;
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.entities.customer.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Booking {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50)
	private String				name;

	@Mandatory
	@ManyToOne
	private Customer			customer;

	@Mandatory // TODO: Esto cómo va?
	private Integer				seatNumber;

	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,8}$")
	@Column(unique = true)
	private String				locatorCode;

	@Mandatory
	@ValidMoment(past = true)
	private Moment				purchaseMoment;

	@Mandatory
	private TravelClass			travelClass;

	@Mandatory
	@ValidMoney // TODO: Cómo dice la mujer, precio máximo?
	private Money				price;

	@Optional // TODO: ???
	private String				lastNibble;
}
