
package acme.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AircraftTest {

	@Test
	void testAircraftAttributes() {
		Aircraft aircraft = new Aircraft();

		aircraft.setModel("Boeing 737");
		aircraft.setRegistrationNumber("AB-12345");
		aircraft.setCapacity(180);
		aircraft.setCargoWeight(3000);
		aircraft.setStatus(AircraftStatus.ACTIVE);
		aircraft.setDetails("https://example.com/details");

		Assertions.assertNotNull(aircraft);
		Assertions.assertEquals("Boeing 737", aircraft.getModel());
		Assertions.assertEquals("AB-12345", aircraft.getRegistrationNumber());
		Assertions.assertEquals(180, aircraft.getCapacity());
		Assertions.assertEquals(3000, aircraft.getCargoWeight());
		Assertions.assertEquals(AircraftStatus.ACTIVE, aircraft.getStatus());
		Assertions.assertEquals("https://example.com/details", aircraft.getDetails());
	}
	@Test
	void testModelTooLong() {
		Aircraft aircraft = new Aircraft();
		String tooLongModel = "A".repeat(51);  // Modelo de 51 caracteres (debería fallar)
		aircraft.setModel(tooLongModel);

		// Verificamos que el modelo no se asigna correctamente
		Assertions.assertNotEquals(tooLongModel, aircraft.getModel());
	}

	@Test
	void testRegistrationNumberTooLong() {
		Aircraft aircraft = new Aircraft();
		String tooLongRegNumber = "AB-123456789012345678901234567890123456789012345";  // 51 caracteres
		aircraft.setRegistrationNumber(tooLongRegNumber);

		// Verificamos que el número de registro no se asigna correctamente
		Assertions.assertNotEquals(tooLongRegNumber, aircraft.getRegistrationNumber());
	}

	@Test
	void testCargoWeightTooLow() {
		Aircraft aircraft = new Aircraft();
		aircraft.setCargoWeight(1500);  // Peso de carga menor a 2000 kg (debería fallar)

		// Verificamos que el peso de carga no es válido
		Assertions.assertNotEquals(1500, aircraft.getCargoWeight());
	}

	@Test
	void testCargoWeightTooHigh() {
		Aircraft aircraft = new Aircraft();
		aircraft.setCargoWeight(60000);  // Peso de carga mayor a 50000 kg (debería fallar)

		// Verificamos que el peso de carga no es válido
		Assertions.assertNotEquals(60000, aircraft.getCargoWeight());
	}

	@Test
	void testDetailsTooLong() {
		Aircraft aircraft = new Aircraft();
		String tooLongDetails = "A".repeat(256);  // Detalles de 256 caracteres (debería fallar)
		aircraft.setDetails(tooLongDetails);

		// Verificamos que los detalles no se asignan correctamente
		Assertions.assertNotEquals(tooLongDetails, aircraft.getDetails());
	}
}
