<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="manager.flight.list.label.tag" path="tag" width="60%"/>
	<acme:list-column code="manager.flight.list.label.cost" path="cost" width="20%"/>
	<acme:list-column code="manager.flight.list.label.requires-self-transfer" path="requiresSelfTransfer" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="manager.flight.list.button.create" action="/manager/flight/create"/>
</jstl:if>
