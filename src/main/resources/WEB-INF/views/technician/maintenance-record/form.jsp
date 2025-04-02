<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-moment code="technician.maintenance-record.form.label.maintenanceMoment" path="maintenanceMoment"/>
	<acme:input-select code="technician.maintenance-record.form.label.status" path="status" choices="${maintenanceRecordstatus}"/>	
	<acme:input-moment code="technician.maintenance-record.form.label.nextInspectionDueDate" path="nextInspectionDueDate"/>
	<acme:input-money code="technician.maintenance-record.form.label.estimatedCost" path="estimatedCost"/>
	<acme:input-select code="technician.maintenance-record.form.label.technician" path="technician" choices="${technicians}" readonly="true"/>	
	<acme:input-select code="technician.maintenance-record.form.label.aircraft" path="aircraft" choices="${aircrafts}"/>
	<acme:input-textarea code="technician.maintenance-record.form.label.notes" path="notes"/>

	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="technician.maintenance-record.form.button.tasks" action="/technician/involved-in/list?masterId=${id}"/>			
		</jstl:when> 
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete')&& draftMode == true}">
			<acme:submit code="technician.maintenance-record.form.button.update" action="/technician/maintenance-record/update"/>
			<acme:submit code="technician.maintenance-record.form.button.publish" action="/technician/maintenance-record/publish"/>
			<acme:button code="technician.maintenance-record.form.button.tasks" action="/technician/involved-in/list?masterId=${id}"/>
			<acme:submit code="technician.maintenance-record.form.button.delete" action="/technician/maintenance-record/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="technician.maintenance-record.form.label.confirmation" path="confirmation"/>
			<acme:submit code="technician.maintenance-record.form.button.create" action="/technician/maintenance-record/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
