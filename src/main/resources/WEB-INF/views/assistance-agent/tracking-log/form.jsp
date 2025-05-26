<%@page%>  

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>  
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>  

<acme:form>  
    <acme:input-moment code="assistance-agent.tracking-log.label.creationMoment" path="creationMoment" readonly="true"/>  
    <acme:input-double code="assistance-agent.tracking-log.label.resolutionPercentage" path="resolutionPercentage"/>  
    <acme:input-textbox code="assistance-agent.tracking-log.label.step" path="step" />
    <acme:input-select code="assistance-agent.tracking-log.label.indicator" path="indicator" choices="${indicator}"/>
    <acme:input-moment code="assistance-agent.tracking-log.label.lastUpdateMoment" path="lastUpdateMoment" readonly="true"/>   
    <acme:input-checkbox code="assistance-agent.tracking-log.label.draftMode" path="draftMode"/> 

    <jstl:choose>    
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == true}">  
            <acme:submit code="assistance-agent.tracking-log.button.update" action="/assistance-agent/tracking-log/update"/>  
            <acme:submit code="assistance-agent.tracking-log.button.delete" action="/assistance-agent/tracking-log/delete"/>  
            <acme:submit code="assistance-agent.tracking-log.button.publish" action="/assistance-agent/tracking-log/publish"/>  
        </jstl:when>   
        <jstl:when test="${_command == 'create'}">  
    		<acme:button code="assistance-agent.tracking-log.button.create" action="/assistance-agent/activity-log/create?masterId=${masterId}"/>  
        </jstl:when>    
    </jstl:choose>  
</acme:form>  