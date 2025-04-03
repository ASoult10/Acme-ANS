
package acme.features.customer.customerDashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingPassenger;

@Repository
public interface CustomerCustomerDashboardRepository extends AbstractRepository {

	@Query("SELECT b FROM Booking b WHERE b.customer.id =:customerId")
	Collection<Booking> findAllBookingsOf(int customerId);

	@Query("SELECT br FROM BookingPassenger br WHERE br.booking.customer.id =:customerId")
	Collection<BookingPassenger> findAllBookingPassengerOf(int customerId);

}
