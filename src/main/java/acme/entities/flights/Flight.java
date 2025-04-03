
package acme.entities.flights;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidFlight;
import acme.entities.legs.LegRepository;
import acme.realms.Manager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidFlight
public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	// HINT: @Valid por defecto.
	@Automapped
	private Boolean				requiresSelfTransfer;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				cost;

	@Optional
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				description;

	@Mandatory
	// HINT: @Valid by default.
	@Automapped
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Date getScheduledDeparture() {
		List<Date> result;
		LegRepository repository;

		repository = SpringHelper.getBean(LegRepository.class);
		result = repository.findSheduledDeparturesByFlight(this.getId(), PageRequest.of(0, 1));

		return result.isEmpty() ? null : result.get(0);
	}

	@Transient
	public Date getScheduledArrival() {
		List<Date> result;
		LegRepository repository;

		repository = SpringHelper.getBean(LegRepository.class);
		result = repository.findSheduledArrivalsByFlight(this.getId(), PageRequest.of(0, 1));

		return result.isEmpty() ? null : result.get(0);
	}

	@Transient
	public String getOriginCity() {
		List<String> result;
		LegRepository repository;

		repository = SpringHelper.getBean(LegRepository.class);
		result = repository.findOriginCitiesByFlight(this.getId(), PageRequest.of(0, 1));

		return result.isEmpty() ? null : result.get(0);
	}

	@Transient
	public String getDestinationCity() {
		List<String> result;
		LegRepository repository;

		repository = SpringHelper.getBean(LegRepository.class);
		result = repository.findDestinationCitiesByFlight(this.getId(), PageRequest.of(0, 1));

		return result.isEmpty() ? null : result.get(0);
	}

	@Transient
	public Integer getNumberOfLayovers() {
		Integer result;
		LegRepository repository;

		repository = SpringHelper.getBean(LegRepository.class);
		result = repository.countNumberOfLegsOfFlight(this.getId());

		return result;
	}

	@Transient
	public boolean getHasLegs() {
		Integer layovers;

		layovers = this.getNumberOfLayovers();

		return layovers > 0;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Manager manager;

}
