
package acme.realms;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidAssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidAssistanceAgent
public class AssistanceAgent extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2-3}\\d{6}$")
	@Column(unique = true)
	private String				employeeCode;

	@Mandatory
	@ValidString(min = 1, max = 255, pattern = "^([A-Za-z ]+)(,[A-Za-z ]+)*$")
	@Automapped
	private String				spokenLanguages;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@ManyToOne(optional = false)
	private String				airline;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				startingMoment;

	@Optional
	@ValidString(min = 0, max = 255)
	@Automapped
	private String				bio;

	@Optional
	@ValidMoney
	@Automapped
	private Money				salary;

	@Optional
	@ValidUrl
	@Automapped
	private String				photoLink;

}
