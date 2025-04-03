<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-select code="technician.involved-in.form.label.task" path="task" choices="${tasks}"/>	


	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|delete')&& draftMode == true}">
			<acme:submit code="technician.involved-in.form.button.delete" action="/technician/involved-in/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="technician.involved-in.form.label.confirmation" path="confirmation"/>
			<acme:submit code="technician.involved-in.form.button.create" action="/technician/involved-in/create?masterId=${masterId}"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
