<%@page%>  

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>  
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>  

<acme:list>  
    <acme:list-column code="assistance-agent.tracking-log.label.indicator" path="indicator"/>  
    <acme:list-column code="assistance-agent.tracking-log.label.resolutionPercentage" path="resolutionPercentage"/>  
    <acme:list-column code="assistance-agent.tracking-log.label.resolution" path="resolution"/>
    <acme:list-column code="assistance-agent.tracking-log.label.lastUpdateMoment" path="lastUpdateMoment"/>    
   	<acme:list-payload path="payload"/>
   	
</acme:list>  

<jstl:if test="${_command == 'list'}">
    <acme:button code="assistance-agent.tracking-log.button.create" action="/assistance-agent/activity-log/create?masterId=${masterId}"/>  
</jstl:if>  