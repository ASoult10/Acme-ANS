<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-select code="customer.bookingPassenger.list.label.passenger" path="passenger" choices="${passengers}"/>
    <acme:input-textbox code="customer.bookingPassenger.list.label.booking" path="booking" readonly="true"/>
    
	<acme:button code="customer.passenger.form.link.create" action="/customer/passenger/create"/>
	
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.bookingPassenger.form.button.create" action="/customer/booking-passenger/create?bookingId=${booking.id}"/>
		</jstl:when>		
	</jstl:choose>	
</acme:form>
