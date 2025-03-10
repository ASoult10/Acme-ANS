
package acme.forms.customers;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private List<String>		lastFiveDestinations;
	private Money				spentMoney;
	private Integer				economyBookings;
	private Integer				businessBookings;
	private Money				bookingTotalCost;
	private Money				bookingAverageCost;
	private Money				bookingMinimumCost;
	private Money				bookingMaximumCost;
	private Money				bookingDeviationCost;
	private Integer				bookingTotalPassengers;
	private Double				bookingAveragePassengers;
	private Integer				bookingMinimumPassengers;
	private Integer				bookingMaximumPassengers;
	private Double				bookingDeviationPassengers;
}
