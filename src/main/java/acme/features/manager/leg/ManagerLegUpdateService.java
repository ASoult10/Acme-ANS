
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.aircrafts.AircraftStatus;
import acme.entities.airlines.Airline;
import acme.entities.airports.Airport;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.realms.Manager;

@GuiService
public class ManagerLegUpdateService extends AbstractGuiService<Manager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerLegRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int legId;
		Leg leg;

		legId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(legId);
		status = leg != null && leg.isDraftMode() && super.getRequest().getPrincipal().hasRealm(leg.getFlight().getManager());

		if (status) {
			String method;
			int airlineId, departureAirportId, arrivalAirportId, aircraftId;
			Airline airline;
			Airport departureAirport, arrivalAirport;
			Aircraft aircraft;

			method = super.getRequest().getMethod();

			if (method.equals("GET"))
				status = true;
			else {
				airlineId = super.getRequest().getData("airline", int.class);
				airline = this.repository.findAirlineById(airlineId);
				status = airlineId == 0 || airline != null;

				departureAirportId = super.getRequest().getData("departureAirport", int.class);
				departureAirport = this.repository.findAirportById(departureAirportId);
				status = status && (departureAirportId == 0 || departureAirport != null);

				arrivalAirportId = super.getRequest().getData("arrivalAirport", int.class);
				arrivalAirport = this.repository.findAirportById(arrivalAirportId);
				status = status && (arrivalAirportId == 0 || arrivalAirport != null);

				aircraftId = super.getRequest().getData("aircraft", int.class);
				aircraft = this.repository.findAircraftById(aircraftId);
				status = status && (aircraftId == 0 || aircraft != null && aircraft.getStatus().equals(AircraftStatus.ACTIVE));
			}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		int airlineId;
		int departureAirportId;
		int arrivalAirportId;
		int aircraftId;
		Airline airline;
		Airport departureAirport;
		Airport arrivalAirport;
		Aircraft aircraft;

		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.repository.findAirlineById(airlineId);

		departureAirportId = super.getRequest().getData("departureAirport", int.class);
		departureAirport = this.repository.findAirportById(departureAirportId);

		arrivalAirportId = super.getRequest().getData("arrivalAirport", int.class);
		arrivalAirport = this.repository.findAirportById(arrivalAirportId);

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);

		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status");
		leg.setAirline(airline);
		leg.setDepartureAirport(departureAirport);
		leg.setArrivalAirport(arrivalAirport);
		leg.setAircraft(aircraft);
	}

	@Override
	public void validate(final Leg leg) {
		boolean correctScheduledDeparture;

		correctScheduledDeparture = leg.getScheduledDeparture() != null && MomentHelper.isFuture(leg.getScheduledDeparture());
		super.state(correctScheduledDeparture, "scheduledDeparture", "acme.validation.leg.scheduled-departure-future.message");
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		SelectChoices statusChoices;
		SelectChoices airlineChoices;
		SelectChoices departureAirportChoices;
		SelectChoices arrivalAirportChoices;
		SelectChoices aircraftChoices;
		Collection<Airline> airlines;
		Collection<Airport> airports;
		Collection<Aircraft> aircrafts;
		Dataset dataset;

		airlines = this.repository.findAllAirlines();
		airports = this.repository.findAllAirports();
		aircrafts = this.repository.findAllActiveAircrafts();

		statusChoices = SelectChoices.from(LegStatus.class, leg.getStatus());
		airlineChoices = SelectChoices.from(airlines, "IATA", leg.getAirline());
		departureAirportChoices = SelectChoices.from(airports, "iataCode", leg.getDepartureAirport());
		arrivalAirportChoices = SelectChoices.from(airports, "iataCode", leg.getArrivalAirport());
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", leg.getAircraft());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status");
		dataset.put("masterId", leg.getFlight().getId());
		dataset.put("draftMode", leg.isDraftMode());
		dataset.put("statuses", statusChoices);

		dataset.put("airline", airlineChoices.getSelected().getKey());
		dataset.put("airlines", airlineChoices);

		dataset.put("departureAirport", departureAirportChoices.getSelected().getKey());
		dataset.put("departureAirports", departureAirportChoices);
		dataset.put("arrivalAirport", arrivalAirportChoices.getSelected().getKey());
		dataset.put("arrivalAirports", arrivalAirportChoices);

		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);

		super.getResponse().addData(dataset);
	}

}
