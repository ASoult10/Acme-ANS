<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.recommendation-dashboard.list.label.city" path="city" width="20%"/>
	<acme:list-column code="customer.recommendation-dashboard.list.label.country" path="country" width="20%"/>
	<acme:list-column code="customer.recommendation-dashboard.list.label.name" path="name" width="20%"/>
	<acme:list-column code="customer.recommendation-dashboard.list.label.recommendationType" path="recommendationType" width="20%"/>
</acme:list>