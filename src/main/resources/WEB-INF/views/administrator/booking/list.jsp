<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="administrator.booking.list.label.flight" path="flight" />
    <acme:list-column code="administrator.booking.list.label.locatorCode" path="locatorCode" />
	<acme:list-column code="administrator.booking.list.label.price" path="price" />
	<acme:list-column code="administrator.booking.list.label.purchaseMoment" path="purchaseMoment" />
    <acme:list-payload path="payload"/>	
</acme:list>