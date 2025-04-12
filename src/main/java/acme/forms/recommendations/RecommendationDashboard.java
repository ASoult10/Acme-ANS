
package acme.forms.recommendations;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendationDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private String				city;

	private String				country;

	private String				name;

	private String				description;

	private String				recommendationType;


	public static RecommendationDashboard of(final String city, final String country, final String name, final String description, final String recommendationType) {
		return new RecommendationDashboard(city, country, name, description, recommendationType);
	}

	public RecommendationDashboard() {

	}

	public RecommendationDashboard(final String city, final String country, final String name, final String description, final String recommendationType) {
		super();
		this.city = city;
		this.country = country;
		this.name = name;
		this.description = description;
		this.recommendationType = recommendationType;
	}
}
