
package acme.entities.booking;

import java.beans.Transient;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidBooking;
import acme.entities.flights.Flight;
import acme.features.customer.passenger.CustomerPassengerRepository;
import acme.realms.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidBooking
//@Table(indexes = {
//	@Index(columnList = "customer_id"), @Index(columnList = "locator_code")
//})
public class Booking extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight				flight;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Customer			customer;

	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,8}$", message = "{acme.validation.locatorCode.notPattern.message}")
	@Column(unique = true)
	private String				locatorCode;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				purchaseMoment;

	@Mandatory
	@Valid
	@Automapped
	private TravelClass			travelClass;

	@Optional
	@ValidString(min = 4, max = 4, pattern = "[0-9]{4}", message = "{acme.validation.lastNibble.notPattern.message}")
	@Automapped
	private String				lastNibble;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				isPublished;


	@Transient
	public Money getPrice() {
		Money price = new Money();

		CustomerPassengerRepository customerPassengerRepository = SpringHelper.getBean(CustomerPassengerRepository.class);

		if (this.getFlight() == null) {
			price.setAmount(0.0);
			price.setCurrency("EUR");
		} else {
			Flight flight = this.getFlight();
			Integer numberOfPassenger = customerPassengerRepository.findAllPassengerByBookingId(this.getId()).size();
			price.setAmount(flight.getCost().getAmount() * numberOfPassenger);
			price.setCurrency(flight.getCost().getCurrency());
		}

		return price;
	}
}
