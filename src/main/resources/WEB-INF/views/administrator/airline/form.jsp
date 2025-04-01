<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="administrator.airline.label.name" path="name"/>
	<acme:input-textbox code="administrator.airline.label.iata" path="IATA"/>	
	<acme:input-url code="administrator.airline.label.website" path="website"/>
	<acme:input-select code="administrator.airline.label.type" path="type" choices="${airlineType}"/>
	<acme:input-moment code="administrator.airline.label.foundationMoment" path="foundationMoment"/>
	<acme:input-email code="administrator.airline.label.email" path="email"/>
	<acme:input-textbox code="administrator.airline.label.phoneNumber" path="phoneNumber"/>

	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update')}">			
			<acme:input-checkbox code="administrator.airline.form.label.confirmation" path="confirmation"/>
			<acme:submit code="administrator.airline.button.update" action="/administrator/airline/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="administrator.airline.form.label.confirmation" path="confirmation"/>
			<acme:submit code="administrator.airline.button.create" action="/administrator/airline/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>