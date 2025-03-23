<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
"model", "registrationNumber", "capacity", "cargoWeight", "status", "details"
<acme:form> 
	<acme:input-textbox code="administrator.aircraft.form.label.model" path="model"/>
	<acme:input-textbox code="administrator.aircraft.form.label.registrationNumber" path="registrationNumber"/>	
	<acme:input-integer code="administrator.aircraft.form.label.capacity" path="capacity"/>
	<acme:input-integer code="administrator.aircraft.form.label.cargoWeight" path="cargoWeight"/>
	<acme:input-select code="administrator.aircraft.form.label.status" path="status" choices="${status}"/>	
	<acme:input-textbox code="administrator.aircraft.form.label.details" path="details"/>

	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|create')}">
			<acme:button code="administrator.aircraft.form.button.maintenanceRecords" action="/administrator/maintenanceRecord/list?masterId=${id}"/>		
			<acme:submit code="administrator.aircraft.form.button.update" action="/administrator/aircraft/update"/>
			<acme:submit code="administrator.aircraft.form.button.delete" action="/administrator/aircraft/delete"/>
			<acme:submit code="administrator.aircraft.form.button.create" action="/administrator/aircraft/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
