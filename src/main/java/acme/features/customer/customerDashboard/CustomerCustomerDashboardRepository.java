
package acme.features.customer.customerDashboard;

import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface CustomerCustomerDashboardRepository extends AbstractRepository {

	//	@Query("SELECT DISTINCT f.destinationCity FROM Booking b JOIN b.flight f WHERE b.customer.id = :customerId ORDER BY b.purchaseMoment DESC")
	//	Collection<String> lastFiveDestinations(Integer customerId);
	//
	//	@Query("SELECT COALESCE(SUM(f.cost.amount * (SELECT COUNT(bp) FROM BookingPassenger bp WHERE bp.booking.id = b.id)), 0) FROM Booking b JOIN b.flight f WHERE b.customer.id = :customerId AND FUNCTION('YEAR', b.purchaseMoment) = FUNCTION('YEAR', CURRENT_DATE)")
	//	Money spentMoney(Integer customerId);
	//
	//	@Query("SELECT COUNT(b) FROM Booking b WHERE b.customer.id = :customerId AND b.travelClass = ECONOMY")
	//	Integer economyBookings(Integer customerId);
	//
	//	@Query("SELECT COUNT(b) FROM Booking b WHERE b.customer.id = :customerId AND b.travelClass = BUSINESS")
	//	Integer businessBookings(Integer customerId);
	//
	//	@Query("SELECT COALESCE(SUM(f.cost.amount * (SELECT COUNT(bp) FROM BookingPassenger bp WHERE bp.booking.id = b.id)), 0) FROM Booking b JOIN b.flight f WHERE b.customer.id = :customerId AND b.purchaseMoment >= CURRENT_DATE - 5 * 365")
	//	Money bookingTotalCost(Integer customerId);
	//
	//	@Query("SELECT COALESCE(AVG(f.cost.amount * (SELECT COUNT(bp) FROM BookingPassenger bp WHERE bp.booking.id = b.id)), 0) FROM Booking b JOIN b.flight f WHERE b.customer.id = :customerId AND b.purchaseMoment >= CURRENT_DATE - 5 * 365")
	//	Money bookingAverageCost(Integer customerId);
	//
	//	@Query("SELECT COALESCE(MIN(f.cost.amount * (SELECT COUNT(bp) FROM BookingPassenger bp WHERE bp.booking.id = b.id)), 0) FROM Booking b JOIN b.flight f WHERE b.customer.id = :customerId AND b.purchaseMoment >= CURRENT_DATE - 5 * 365")
	//	Money bookingMinimumCost(Integer customerId);
	//
	//	@Query("SELECT COALESCE(MAX(f.cost.amount * (SELECT COUNT(bp) FROM BookingPassenger bp WHERE bp.booking.id = b.id)), 0) FROM Booking b JOIN b.flight f WHERE b.customer.id = :customerId AND b.purchaseMoment >= CURRENT_DATE - 5 * 365")
	//	Money bookingMaximumCost(Integer customerId);
	//
	//	@Query("SELECT COALESCE(STDDEV(f.cost.amount * (SELECT COUNT(bp) FROM BookingPassenger bp WHERE bp.booking.id = b.id)), 0) FROM Booking b JOIN b.flight f WHERE b.customer.id = :customerId AND b.purchaseMoment >= CURRENT_DATE - 5 * 365")
	//	Money bookingDeviationCost(Integer customerId);
	//
	//	@Query("SELECT COALESCE(SUM((SELECT COUNT(bp) FROM BookingPassenger bp WHERE bp.booking.id = b.id)), 0) FROM Booking b WHERE b.customer.id = :customerId")
	//	Integer bookingTotalPassengers(Integer customerId);
	//
	//	@Query("SELECT COALESCE(AVG((SELECT COUNT(bp) FROM BookingPassenger bp WHERE bp.booking.id = b.id)), 0) FROM Booking b WHERE b.customer.id = :customerId")
	//	Double bookingAveragePassengers(Integer customerId);
	//
	//	@Query("SELECT COALESCE(MIN((SELECT COUNT(bp) FROM BookingPassenger bp WHERE bp.booking.id = b.id)), 0) FROM Booking b WHERE b.customer.id = :customerId")
	//	Integer bookingMinimumPassengers(Integer customerId);
	//
	//	@Query("SELECT COALESCE(MAX((SELECT COUNT(bp) FROM BookingPassenger bp WHERE bp.booking.id = b.id)), 0) FROM Booking b WHERE b.customer.id = :customerId")
	//	Integer bookingMaximumPassengers(Integer customerId);
	//
	//	@Query("SELECT COALESCE(STDDEV((SELECT COUNT(bp) FROM BookingPassenger bp WHERE bp.booking.id = b.id)), 0) FROM Booking b WHERE b.customer.id = :customerId")
	//	Double bookingDeviationPassengers(Integer customerId);

}
