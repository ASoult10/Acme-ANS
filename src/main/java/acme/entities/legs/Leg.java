
package acme.entities.legs;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.MomentHelper;
import acme.constraints.ValidLeg;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airlines.Airline;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidLeg
@Table(indexes = {
	@Index(columnList = "flightNumber"), //
	@Index(columnList = "draftMode"), //
	@Index(columnList = "scheduledDeparture"), //
	@Index(columnList = "draftMode, scheduledDeparture, scheduledArrival")
})
public class Leg extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(min = 7, max = 7, pattern = "^[A-Z]{3}\\d{4}$", message = "{acme.validation.leg.flight-number-format.message}")
	@Column(unique = true)
	private String				flightNumber;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@Valid
	@Automapped
	private LegStatus			status;

	@Mandatory
	// HINT: @Valid by default.
	@Automapped
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Double getDuration() {
		Double duration = null;

		if (this.getScheduledDeparture() != null && this.getScheduledArrival() != null && MomentHelper.isAfterOrEqual(this.getScheduledArrival(), this.getScheduledDeparture()))
			duration = MomentHelper.computeDuration(this.getScheduledDeparture(), this.getScheduledArrival()).getSeconds() / 3600.;

		return duration;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline		airline;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight		flight;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport		departureAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport		arrivalAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft	aircraft;

}
