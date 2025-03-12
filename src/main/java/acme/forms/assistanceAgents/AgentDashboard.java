
package acme.forms.assistanceAgents;

import acme.client.components.basis.AbstractForm;

public class AgentDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private Double				ratioSuccess;
	private Double				ratioRejected;
	private String				topThreeMonths;
	private Double				averageLog;
	private Integer				minimumLog;
	private Integer				maximumLog;
	private Double				sdLog;
	private Double				averageClaim;
	private Integer				minimumClaim;
	private Integer				maximumClaim;
	private Double				sdClaim;

}
