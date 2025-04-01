<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="manager.flight.form.label.tag" path="tag"/>
	<acme:input-checkbox code="manager.flight.form.label.requires-self-transfer" path="requiresSelfTransfer"/>
	<acme:input-money code="manager.flight.form.label.cost" path="cost"/>
	<acme:input-textarea code="manager.flight.form.label.description" path="description"/>

	<acme:input-moment code="manager.flight.form.label.scheduled-departure" path="scheduledDeparture" readonly="true"/>
	<acme:input-moment code="manager.flight.form.label.scheduled-arrival" path="scheduledArrival" readonly="true"/>
	<acme:input-textbox code="manager.flight.form.label.origin-city" path="originCity" readonly="true"/>
	<acme:input-textbox code="manager.flight.form.label.destination-city" path="destinationCity" readonly="true"/>

	<jstl:choose>
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="manager.flight.form.button.legs" action="/manager/leg/list?masterId=${id}"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:button code="manager.flight.form.button.legs" action="/manager/leg/list?masterId=${id}"/>
			<acme:submit code="manager.flight.form.button.update" action="/manager/flight/update"/>
			<acme:submit code="manager.flight.form.button.delete" action="/manager/flight/delete"/>
			<jstl:if test="${hasLegs}">
				<acme:submit code="manager.flight.form.button.publish" action="/manager/flight/publish"/>
			</jstl:if>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="manager.flight.form.button.create" action="/manager/flight/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>
