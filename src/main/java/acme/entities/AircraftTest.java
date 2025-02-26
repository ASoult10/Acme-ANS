
package acme.entities;

import java.util.Set;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AircraftTest {

	private Validator validator;


	@BeforeEach
	void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		this.validator = factory.getValidator();
	}

	@Test
	void testModelTooLong() {
		Aircraft aircraft = new Aircraft();
		aircraft.setModel("A".repeat(51));  // Modelo de 51 caracteres (inválido)

		Set<javax.validation.ConstraintViolation<Aircraft>> violations = this.validator.validate(aircraft);
		Assertions.assertFalse(violations.isEmpty(), "Se esperaba una violación de la restricción de tamaño.");
	}

	@Test
	void testRegistrationNumberTooLong() {
		Aircraft aircraft = new Aircraft();
		aircraft.setRegistrationNumber("AB-123456789012345678901234567890123456789012345");  // 51 caracteres

		Set<javax.validation.ConstraintViolation<Aircraft>> violations = this.validator.validate(aircraft);
		Assertions.assertFalse(violations.isEmpty(), "Se esperaba una violación de la restricción de tamaño.");
	}

	@Test
	void testCargoWeightTooLow() {
		Aircraft aircraft = new Aircraft();
		aircraft.setCargoWeight(1500);  // Peso menor a 2000 kg (inválido)

		Set<javax.validation.ConstraintViolation<Aircraft>> violations = this.validator.validate(aircraft);
		Assertions.assertFalse(violations.isEmpty(), "Se esperaba una violación de la restricción de peso mínimo.");
	}

	@Test
	void testCargoWeightTooHigh() {
		Aircraft aircraft = new Aircraft();
		aircraft.setCargoWeight(60000);  // Peso mayor a 50000 kg (inválido)

		Set<javax.validation.ConstraintViolation<Aircraft>> violations = this.validator.validate(aircraft);
		Assertions.assertFalse(violations.isEmpty(), "Se esperaba una violación de la restricción de peso máximo.");
	}

	@Test
	void testDetailsTooLong() {
		Aircraft aircraft = new Aircraft();
		aircraft.setDetails("A".repeat(256));  // Detalles con 256 caracteres (inválido)

		Set<javax.validation.ConstraintViolation<Aircraft>> violations = this.validator.validate(aircraft);
		Assertions.assertFalse(violations.isEmpty(), "Se esperaba una violación de la restricción de tamaño en 'details'.");
	}
}
