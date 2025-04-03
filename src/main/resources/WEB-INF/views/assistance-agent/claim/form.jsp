<%@page%>  

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>  
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>  

<acme:form>  
    <acme:input-moment code="assistance-agent.claim.label.registrationMoment" path="registrationMoment" readonly="true"/>  
    <acme:input-textbox code="assistance-agent.claim.label.email" path="email"/>  
    <acme:input-textarea code="assistance-agent.claim.label.description" path="description"/>  
    <acme:input-select code="assistance-agent.claim.label.type" path="type" choices="${type}"/>  
    <acme:input-select code="assistance-agent.claim.label.status" path="status" choices="${status}"/> 
    <acme:input-checkbox code="assistance-agent.claim.label.draftMode" path="draftMode"/> 
    <acme:input-select code="assistance-agent.claim.label.leg" path="leg" choices="${legs}"/>
    <acme:input-moment code="assistance-agent.claim.label.agent" path="assistanceAgent" readonly="true"/>  

    <jstl:choose>    
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == true}">  
            <acme:submit code="assistance-agent.claim.button.update" action="/assistance-agent/claim/update"/>  
            <acme:submit code="assistance-agent.claim.button.delete" action="/assistance-agent/claim/delete"/>  
            <acme:submit code="assistance-agent.claim.button.publish" action="/assistance-agent/claim/publish"/>  
        </jstl:when>  
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == false}">  
            <acme:submit code="assistance-agent.claim.button.publish" action="/assistance-agent/claim/publish"/>  
        </jstl:when> 
        <jstl:when test="${_command == 'create'}">  
        	<acme:input-checkbox code="assistance-agent.claim.form.label.confirmation" path="confirmation"/>
            <acme:submit code="assistance-agent.claim.button.create" action="/assistance-agent/claim/create"/>  
        </jstl:when>    
    </jstl:choose>  
</acme:form>  