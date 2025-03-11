
package acme.forms.recommendations;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Recommendation {

	// Attributes -------------------------------------------------------------

	private String				name;

	private String				description;

	private RecommendationType	recommendationType;
}
