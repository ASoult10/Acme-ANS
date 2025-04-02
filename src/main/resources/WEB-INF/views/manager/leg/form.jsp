<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="employer.duty.form.label.title" path="title"/>
	<acme:input-textarea code="employer.duty.form.label.description" path="description"/>
	<acme:input-double code="employer.duty.form.label.workLoad" path="workLoad" placeholder="employer.duty.form.placeholder.workLoad"/>
	<acme:input-url code="employer.duty.form.label.moreInfo" path="moreInfo"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == true}">
			<acme:submit code="employer.duty.form.button.update" action="/employer/duty/update"/>
			<acme:submit code="employer.duty.form.button.delete" action="/employer/duty/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="employer.duty.form.button.create" action="/employer/duty/create?masterId=${masterId}"/>
		</jstl:when>		
	</jstl:choose>		
</acme:form>

