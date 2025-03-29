<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.airline.label.name" path="name"/>
	<acme:list-column code="administrator.airline.label.iata" path="IATA"/>
	<acme:list-column code="administrator.airline.label.website" path="website"/>
	<acme:list-column code="administrator.airline.label.type" path="type"/>
	<acme:list-column code="administrator.airline.label.foundationMoment" path="foundationMoment"/>
	<acme:list-column code="administrator.airline.label.email" path="email"/>
	<acme:list-column code="administrator.airline.label.phoneNumber" path="phoneNumber"/>
	
	<acme:list-payload path="payload"/>
</acme:list>	

<jstl:if test="${_command == 'list'}">
	<acme:button code="administrator.airline.button.create" action="/administrator/airline/create"/>
</jstl:if>