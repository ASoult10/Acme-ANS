
package acme.features.authenticated.member;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.realms.Member;

@GuiController
public class AuthenticatedMemberController extends AbstractGuiController<Authenticated, Member> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedMemberCreateService	authenticatedMemberCreateService;

	@Autowired
	private AuthenticatedMemberUpdateService	authenticatedMemberUpdateService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.authenticatedMemberCreateService);
		super.addBasicCommand("update", this.authenticatedMemberUpdateService);
	}

}
