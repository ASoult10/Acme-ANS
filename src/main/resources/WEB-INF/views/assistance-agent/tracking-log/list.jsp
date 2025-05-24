<%@page%>  

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>  
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>  

<acme:list>  
    <acme:list-column code="assistance-agent.claim.label.email" path="email"/>  
    <acme:list-column code="assistance-agent.claim.label.type" path="type"/>  
    <acme:list-column code="assistance-agent.claim.label.leg" path="leg"/>  
    <acme:list-column code="assistance-agent.claim.label.status" path="status"/>  
   	<acme:list-payload path="payload"/>
</acme:list>  

<jstl:if test="${acme:anyOf(_command, 'list||pending')}">  
    <acme:button code="assistance-agent.claim.button.create" action="/assistance-agent/claim/create"/>  
</jstl:if>  