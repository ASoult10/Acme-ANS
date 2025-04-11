/*
 * CustomerRecommendationDashboardController.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.customer.recommendationDashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.forms.recommendations.RecommendationDashboard;
import acme.realms.Customer;

@GuiController
public class CustomerRecommendationDashboardController extends AbstractGuiController<Customer, RecommendationDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerRecommendationDashboardListService customerRecommendationDashboardListService;

	//	@Autowired
	//	private CustomerRecommendationDashboardShowService	customerRecommendationDashboardShowService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.customerRecommendationDashboardListService);
		//		super.addBasicCommand("show", this.customerRecommendationDashboardShowService);
	}

}
