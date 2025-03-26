<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="member.flight-assignment.list.label.duty" path="duty" width="25%"/>
	<acme:list-column code="member.flight-assignment.list.label.moment" path="moment" width="25%"/>
	<acme:list-column code="member.flight-assignment.list.label.assignmentStatus" path="assignmentStatus" width="25%"/>
	<acme:list-column code="member.flight-assignment.list.label.remarks" path="remarks" width="25%"/>
	
	<acme:list-payload path="payload"/>
</acme:list>	
	
<jstl:if test="${_command == 'list'}">
	<acme:button code="member.flight-assignment.list.button.create" action="/member/flight-assignment/create"/>
</jstl:if>	