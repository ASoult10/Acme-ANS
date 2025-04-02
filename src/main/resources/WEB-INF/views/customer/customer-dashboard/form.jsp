<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="true">
    <acme:input-textbox code="customer.customer-dashboard.form.label.last-five-destinations-last-year" path="lastFiveDestinations"/>
    <acme:input-double code="customer.customer-dashboard.form.label.spent-money-last-year" path="spentMoneyLastYear"/>
    <acme:input-integer code="customer.customer-dashboard.form.label.economy-bookings" path="economyBookings"/>
    <acme:input-integer code="customer.customer-dashboard.form.label.business-bookings" path="businessBookings"/>
    <acme:input-integer code="customer.customer-dashboard.form.label.booking-count-cost" path="bookingCountCost"/>
    <acme:input-double code="customer.customer-dashboard.form.label.booking-average-cost" path="bookingAverageCost"/>
    <acme:input-double code="customer.customer-dashboard.form.label.booking-minimum-cost" path="bookingMinimumCost"/>
    <acme:input-double code="customer.customer-dashboard.form.label.booking-maximum-cost" path="bookingMaximumCost"/>
    <acme:input-double code="customer.customer-dashboard.form.label.booking-deviation-cost" path="bookingDeviationCost"/>
    <acme:input-integer code="customer.customer-dashboard.form.label.booking-count-passengers" path="bookingCountPassengers"/>
    <acme:input-double code="customer.customer-dashboard.form.label.booking-average-passengers" path="bookingAveragePassengers"/>
    <acme:input-integer code="customer.customer-dashboard.form.label.booking-minimum-passengers" path="bookingMinimumPassengers"/>
    <acme:input-integer code="customer.customer-dashboard.form.label.booking-maximum-passengers" path="bookingMaximumPassengers"/>
    <acme:input-double code="customer.customer-dashboard.form.label.booking-deviation-passengers" path="bookingDeviationPassengers"/>
</acme:form>