<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="authenticated.member.form.label.employeeCode" path="employeeCode"/>
	<acme:input-textbox code="authenticated.member.form.label.phoneNumber" path="phoneNumber"/>
	<acme:input-textarea code="authenticated.member.form.label.languageSkills" path="languageSkills"/>
	<acme:input-select code="authenticated.member.form.label.availabilityStatus" path="availabilityStatus" choices="${availabilityStatus}"/>
	<acme:input-money code="authenticated.member.form.label.salary" path="salary"/>
	<acme:input-integer code="authenticated.member.form.label.yearsOfExperience" path="yearsOfExperience"/>
	<acme:input-select code="authenticated.member.form.label.airline" path="airline" choices="${airlines}"/>
	
	<jstl:if test="${_command == 'create'}">
		<acme:submit code="authenticated.member.form.button.create" action="/authenticated/member/create"/>
	</jstl:if>
	
	<jstl:if test="${_command != 'create'}">
		<acme:submit code="authenticated.member.form.button.update" action="/authenticated/member/update"/>
	</jstl:if>

</acme:form>
