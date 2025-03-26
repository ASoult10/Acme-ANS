<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="member.flightAssignment.form.label.duty" path="duty"/>
	<acme:input-textbox code="member.flightAssignment.form.label.moment" path="moment"/>
	<acme:input-textbox code="member.flightAssignment.form.label.assignmentStatus" path="assignmentStatus"/>
	<acme:input-textbox code="member.flightAssignment.form.label.remarks" path="remarks"/>
	

	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == true}">
			<acme:submit code="member.flightAssignment.form.button.update" action="/member/flightAssignment/update"/>
			<acme:submit code="member.flightAssignment.form.button.delete" action="/member/flightAssignment/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="member.flightAssignment.form.label.confirmation" path="confirmation"/>
			<acme:submit code="member.flightAssignment.form.button.create" action="/member/flightAssignment/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>