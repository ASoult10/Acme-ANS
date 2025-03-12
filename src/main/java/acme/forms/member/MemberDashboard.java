
package acme.forms.member;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.flightAssignment.AssignmentStatus;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long						serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private List<String>							lastFiveDestinations;
	private String									severityranging;
	private List<Member>							memberAssignedWith;
	private Map<FlightAssignment, AssignmentStatus>	flightAssignmentGrouped;
	private Integer									monthFlightAssignmentAverage;
	private Integer									monthFlightAssignmentMax;
	private Integer									monthFlightAssignmentMin;
	private Double									monthFlightAssignmentStandardDeviation;

}
