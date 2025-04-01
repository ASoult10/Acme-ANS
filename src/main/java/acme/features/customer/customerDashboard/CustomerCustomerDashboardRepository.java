
package acme.features.customer.customerDashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.datatypes.Money;
import acme.client.repositories.AbstractRepository;

@Repository
public interface CustomerCustomerDashboardRepository extends AbstractRepository {

	@Query("")
	Collection<String> lastFiveDestinations(Integer customerId);

	@Query("")
	Money spentMoney(Integer customerId);

	@Query("")
	Integer economyBookings(Integer customerId);

	@Query("")
	Integer businessBookings(Integer customerId);

	@Query("")
	Money bookingTotalCost(Integer customerId);

	@Query("")
	Money bookingAverageCost(Integer customerId);

	@Query("")
	Money bookingMinimumCost(Integer customerId);

	@Query("")
	Money bookingMaximumCost(Integer customerId);

	@Query("")
	Money bookingDeviationCost(Integer customerId);

	@Query("")
	Integer bookingTotalPassengers(Integer customerId);

	@Query("")
	Double bookingAveragePassengers(Integer customerId);

	@Query("")
	Integer bookingMinimumPassengers(Integer customerId);

	@Query("")
	Integer bookingMaximumPassengers(Integer customerId);

	@Query("")
	Double bookingDeviationPassengers(Integer customerId);

}
