
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
	private List<Money>			spentMoneyLastYear;
	private Integer				economyBookings;
	private Integer				businessBookings;
	private List<Money>			bookingCountCost;
	private List<Money>			bookingAverageCost;
	private List<Money>			bookingMinimumCost;
	private List<Money>			bookingMaximumCost;
	private List<Money>			bookingDeviationCost;
	private Integer				bookingCountPassengers;
	private Double				bookingAveragePassengers;
	private Integer				bookingMinimumPassengers;
	private Integer				bookingMaximumPassengers;
	private Double				bookingDeviationPassengers;
}
