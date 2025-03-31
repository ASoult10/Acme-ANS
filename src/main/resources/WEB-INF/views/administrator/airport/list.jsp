<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.airport.list.label.name" path="name" width="50%"/>
	<acme:list-column code="administrator.airport.list.label.code" path="iataCode" width="25%"/>
	<acme:list-column code="administrator.airport.list.label.country" path="country" width="25%"/>
</acme:list>

<acme:button code="administrator.airport.list.button.create" action="/administrator/airport/create"/>
