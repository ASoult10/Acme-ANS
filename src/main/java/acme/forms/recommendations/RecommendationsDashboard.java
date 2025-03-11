
package acme.forms.recommendations;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendationsDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private String					city;
	private String					country;

	private List<Recommendation>	recommendations;

}
