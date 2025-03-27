<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-select code="member.flight-assignment.form.label.duty" path="duty" choices="${duty}"/>
	<acme:input-select code="member.flight-assignment.form.label.leg" path="leg" choices="${legs}"/>	
	<acme:input-select code="member.flight-assignment.form.label.member" path="member" choices="${members}"/>	
	<acme:input-moment code="member.flight-assignment.form.label.moment" path="moment"/>
	<acme:input-select code="member.flight-assignment.form.label.assignmentStatus" path="assignmentStatus" choices="${assignmentStatus}"/>
	<acme:input-textbox code="member.flight-assignment.form.label.remarks" path="remarks"/>
	

	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update')}">
			<acme:submit code="member.flight-assignment.form.button.update" action="/member/flight-assignment/update"/>
			
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="member.flight-assignment.form.label.confirmation" path="confirmation"/>
			<acme:submit code="member.flight-assignment.form.button.create" action="/member/flight-assignment/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>