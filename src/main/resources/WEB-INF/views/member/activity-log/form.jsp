<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-moment code="member.activity-log.form.label.registrationMoment" path="registrationMoment"/>
	<acme:input-textbox code="member.activity-log.form.label.typeOfIncident" path="typeOfIncident"/>	
	<acme:input-textbox code="member.activity-log.form.label.description" path="description"/>	
	<acme:input-integer code="member.activity-log.form.label.severityLevel" path="severityLevel"/>
	

	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && draftMode == true && not masterDraftMode}">
			<acme:submit code="member.activity-log.form.button.update" action="/member/activity-log/update"/>
			<acme:submit code="member.activity-log.form.button.publish" action="/member/activity-log/publish"/>
			<acme:submit code="member.activity-log.form.button.delete" action="/member/activity-log/delete"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && draftMode == true}">
			<acme:submit code="member.activity-log.form.button.update" action="/member/activity-log/update"/>
			<acme:submit code="member.activity-log.form.button.delete" action="/member/activity-log/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="member.activity-log.form.button.create" action="/member/activity-log/create?masterId=${masterId}"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>