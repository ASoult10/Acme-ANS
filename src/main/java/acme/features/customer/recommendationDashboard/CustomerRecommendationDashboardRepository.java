
package acme.features.customer.recommendationDashboard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.recommendationForCustomer.RecommendationForCustomer;

@Repository
public interface CustomerRecommendationDashboardRepository extends AbstractRepository {

	@Query("SELECT r FROM RecommendationForCustomer r WHERE r.city = :city AND r.country = :country")
	List<RecommendationForCustomer> findRecommendationsByCityAndCountry(String city, String country);

}
