<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.passenger.list.label.fullName" path="fullName" width="20%"/>
	<acme:list-column code="customer.passenger.list.label.passportNumber" path="passportNumber" width="20%"/>
	<acme:list-column code="customer.passenger.list.label.birthDate" path="birthDate" width="20%"/>
	<acme:list-column code="customer.passenger.list.label.isPublished" path="isPublished" width="20%"/>
    <acme:list-payload path="payload"/>	
</acme:list>	
	

<jstl:if test="${_command == 'list'}">
    <jstl:if test="${empty param.bookingId}">
        <acme:button code="customer.passenger.list.button.create" action="/customer/passenger/create"/>
    </jstl:if>
</jstl:if>
