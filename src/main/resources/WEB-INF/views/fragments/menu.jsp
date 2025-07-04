<%--
- menu.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:menu-bar>
	
	<acme:menu-left>
	
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.anonymous.manuel-link" action="https://google.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.alejandro-link" action="https://github.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.enrique-link" action="https://www.twitch.tv/"/>
			<acme:menu-suboption code="master.menu.anonymous.mario-link" action="https://colorhunt.co/"/>
			<acme:menu-suboption code="master.menu.anonymous.marta-link" action="https://open.spotify.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.administrator" access="hasRealm('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.list-user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-airports" action="/administrator/airport/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-aircrafts" action="/administrator/aircraft/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-airlines" action="/administrator/airline/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-bookings" action="/administrator/booking/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-initial" action="/administrator/system/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-sample" action="/administrator/system/populate-sample"/>
			<acme:menu-suboption code="master.menu.administrator.populate-recommendations" action="/administrator/recommendation-for-customer/populate"/>			
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-system-down" action="/administrator/system/shut-down"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.manager" access="hasRealm('Manager')">
			<acme:menu-suboption code="master.menu.manager.list-flights" action="/manager/flight/list"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.provider" access="hasRealm('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.consumer" access="hasRealm('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.customer" access="hasRealm('Customer')">
			<acme:menu-suboption code="master.menu.customer.dashboard" action="/customer/customer-dashboard/show"/>
			<acme:menu-suboption code="master.menu.customer.list-bookings" action="/customer/booking/list"/>
			<acme:menu-suboption code="master.menu.customer.list-passengers" action="/customer/passenger/list"/>
		</acme:menu-option>
		
		
		<acme:menu-option code="master.menu.member" access="hasRealm('Member')">
			<acme:menu-suboption code="master.menu.member.completedlist-flightAssignment" action="/member/flight-assignment/completedlist"/>
			<acme:menu-suboption code="master.menu.member.notCompletedlist-flightAssignment" action="/member/flight-assignment/notCompletedlist"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.member.myCompletedList-flightAssignment" action="/member/flight-assignment/myCompletedList"/>
			<acme:menu-suboption code="master.menu.member.myNotCompletedList-flightAssignment" action="/member/flight-assignment/myNotCompletedList"/>		
		</acme:menu-option>
			
		<acme:menu-option code="master.menu.technician" access="hasRealm('Technician')">
			<acme:menu-suboption code="master.menu.technician.list-maintenanceRecord" action="/technician/maintenance-record/list"/>
			<acme:menu-suboption code="master.menu.technician.list-mine-maintenanceRecord" action="/technician/maintenance-record/list-mine"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.technician.list-task" action="/technician/task/list"/>
			<acme:menu-suboption code="master.menu.technician.list-mine-task" action="/technician/task/list-mine"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.agent" access="hasRealm('AssistanceAgent')">
			<acme:menu-suboption code="master.menu.agent.list-claim" action="/assistance-agent/claim/list"/>
			<acme:menu-suboption code="master.menu.agent.pending-claim" action="/assistance-agent/claim/pending"/>
		</acme:menu-option>
		
	</acme:menu-left>

	<acme:menu-right>		
	
		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-profile" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.become-customer" action="/authenticated/customer/create" access="!hasRealm('Customer')"/>
			<acme:menu-suboption code="master.menu.user-account.customer-profile" action="/authenticated/customer/update" access="hasRealm('Customer')"/>
			<acme:menu-suboption code="master.menu.user-account.become-technician" action="/authenticated/technician/create" access="!hasRealm('Technician')"/>
			<acme:menu-suboption code="master.menu.user-account.technician-profile" action="/authenticated/technician/update" access="hasRealm('Technician')"/>
			<acme:menu-suboption code="master.menu.user-account.become-member" action="/authenticated/member/create" access="!hasRealm('Member')"/>
			<acme:menu-suboption code="master.menu.user-account.member-profile" action="/authenticated/member/update" access="hasRealm('Member')"/>
			
		</acme:menu-option>
	
	</acme:menu-right>
</acme:menu-bar>


