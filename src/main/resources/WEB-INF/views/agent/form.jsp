<%@page%>  

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>  
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>  

<acme:form>  
    <acme:input-moment code="agent.claim.form.label.registrationMoment" path="registrationMoment" readonly="true"/>  
    <acme:input-textbox code="agent.claim.form.label.email" path="email"/>  
    <acme:input-textarea code="agent.claim.form.label.description" path="description"/>  
    <acme:input-select code="agent.claim.form.label.type" path="type" choices="${type}"/>  
    <acme:input-select code="agent.claim.form.label.status" path="status" choices="${status}"/> 
    <acme:input-checkbox code="agent.claim.form.label.draftMode" path="draftMode"/> 
    <acme:input-select code="agent.claim.form.label.leg" path="leg" choices="${legs}"/>
    <acme:input-moment code="agent.claim.form.label.agent" path="assistanceAgent" readonly="true"/>  

    <jstl:choose>    
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == false}">  
            <acme:submit code="agent.claim.button.update" action="/agent/claim/update"/>  
            <acme:submit code="agent.claim.button.delete" action="/agent/claim/delete"/>  
            <acme:submit code="agent.claim.button.publish" action="/agent/claim/publish"/>  
        </jstl:when>  
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == true}">  
            <acme:submit code="agent.claim.button.publish" action="/agent/claim/publish"/>  
        </jstl:when> 
        <jstl:when test="${_command == 'create'}">  
        	<acme:input-checkbox code="agent.claim.form.label.confirmation" path="confirmation"/>
            <acme:submit code="agent.claim.button.create" action="/agent/claim/create"/>  
        </jstl:when>    
    </jstl:choose>  
</acme:form>  