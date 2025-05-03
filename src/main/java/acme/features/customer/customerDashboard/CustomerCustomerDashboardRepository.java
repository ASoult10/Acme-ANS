
package acme.features.customer.customerDashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingPassenger;

@Repository
public interface CustomerCustomerDashboardRepository extends AbstractRepository {

	@Query("SELECT b FROM Booking b WHERE b.customer.id =:customerId AND b.isPublished=true")
	Collection<Booking> findAllBookingsOf(int customerId);

	@Query("SELECT bp FROM BookingPassenger bp WHERE bp.booking.customer.id =:customerId AND bp.booking.isPublished=true")
	Collection<BookingPassenger> findAllBookingPassengerOf(int customerId);

}
