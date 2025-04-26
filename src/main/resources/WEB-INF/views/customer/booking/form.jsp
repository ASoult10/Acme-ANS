<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${isPublished}"> 
	<acme:input-select code="customer.booking.form.label.flight" path="flight" choices="${flights}" readonly="${isPublished}"/>
	<acme:input-textbox code="customer.booking.form.label.locatorCode" path="locatorCode"/>
	<acme:input-textbox code="customer.booking.form.label.purchaseMoment" path="purchaseMoment" readonly="true"/>
	<acme:input-select code="customer.booking.form.label.travelClass" path="travelClass" choices="${travelClass}"/>	
	<acme:input-money code="customer.booking.form.label.price" path="price" readonly="true"/>
	<acme:input-integer code="customer.booking.form.label.lastNibble" path="lastNibble"/>

	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update') && isPublished == false}">
			<acme:submit code="customer.booking.form.button.update" action="/customer/booking/update"/>
			<acme:submit code="customer.booking.form.button.publish" action="/customer/booking/publish"/>
			<acme:button code="customer.booking.form.button.addPassenger" action="/customer/booking-passenger/create?bookingId=${id}"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.booking.form.button.create" action="/customer/booking/create"/>
		</jstl:when>		
	</jstl:choose>
	
	<jstl:if test="${_command != 'create'}">
		<acme:button code="customer.booking.form.button.listPassenger" action="/customer/passenger/list?bookingId=${id}"/>
		<acme:button code="customer.booking.form.show.recommendations" action="/customer/recommendation-dashboard/list?city=${city}&country=${country}"/>
	</jstl:if>
</acme:form>
