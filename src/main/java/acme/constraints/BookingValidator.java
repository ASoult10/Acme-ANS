
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.booking.Booking;
import acme.features.customer.booking.CustomerBookingRepository;

@Validator
public class BookingValidator extends AbstractValidator<ValidBooking, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	protected void initialise(final ValidBooking annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Booking booking, final ConstraintValidatorContext context) {

		String locatorCode = booking.getLocatorCode();

		List<Booking> bookingsWithSameLocatorCode = this.repository.findManyBookingsByLocatorCode(locatorCode);
		for (Integer i = 0; i < bookingsWithSameLocatorCode.size(); i++)
			if (bookingsWithSameLocatorCode.get(i).getId() != booking.getId()) {
				super.state(context, false, "*", "{acme.validation.identifier.repeated.message}: " + locatorCode);
				return false;
			}

		return true;
	}

}
