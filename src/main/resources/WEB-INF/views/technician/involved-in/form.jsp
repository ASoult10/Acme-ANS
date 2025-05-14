<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-select code="technician.involved-in.form.label.task" path="task" choices="${tasks}"/>	
	<acme:input-select code="technician.involved-in.form.label.type" path="task.type"  choices="${taskStatus}"/>
	<acme:input-textbox code="technician.involved-in.form.label.description" path="task.description"/>	
	<acme:input-integer code="technician.involved-in.form.label.priority" path="task.priority"/>
	<acme:input-integer code="technician.involved-in.form.label.estimatedDuration" path="task.estimatedDuration"/>
	<acme:input-select code="technician.involved-in.form.label.technician" path="task.technician" choices="${technicians}" readonly="true"/>


	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|delete')&& draftMode == true}">
			<acme:submit code="technician.involved-in.form.button.delete" action="/technician/involved-in/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="technician.involved-in.form.button.create" action="/technician/involved-in/create?masterId=${masterId}"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
