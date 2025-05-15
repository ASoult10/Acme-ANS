<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-select code="member.flight-assignment.form.label.duty" path="duty" choices="${duty}"/>
	<acme:input-select code="member.flight-assignment.form.label.leg" path="leg" choices="${legs}"/>
	<acme:input-textbox code="member.flight-assignment.form.label.member" path="member" readonly="true"/>
	<acme:input-moment code="member.flight-assignment.form.label.moment" path="moment" readonly="true"/>
	<acme:input-select code="member.flight-assignment.form.label.assignmentStatus" path="assignmentStatus" choices="${assignmentStatus}"/>
	<acme:input-textbox code="member.flight-assignment.form.label.remarks" path="remarks"/>
	

	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="member.flight-assignment.form.button.activity-log" action="/member/activity-log/list?masterId=${id}"/>			
		</jstl:when> 
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && draftMode == true && legNotCompleted == true}">
			<acme:button code="member.flight-assignment.form.button.activity-log" action="/member/activity-log/list?masterId=${id}"/>
			<acme:submit code="member.flight-assignment.form.button.publish" action="/member/flight-assignment/publish"/>
			<acme:submit code="member.flight-assignment.form.button.update" action="/member/flight-assignment/update"/>
			<acme:submit code="member.flight-assignment.form.button.delete" action="/member/flight-assignment/delete"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && draftMode == true}">
			<acme:button code="member.flight-assignment.form.button.activity-log" action="/member/activity-log/list?masterId=${id}"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="member.flight-assignment.form.label.confirmation" path="confirmation"/>
			<acme:submit code="member.flight-assignment.form.button.create" action="/member/flight-assignment/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>