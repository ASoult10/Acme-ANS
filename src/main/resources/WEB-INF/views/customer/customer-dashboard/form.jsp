<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>  
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>  

<table class="table table-sm">  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.customer-dashboard.form.label.last-five-destinations-last-year"/>  
        </th>  
        <td>  
            <acme:print value="lastFiveDestinations"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.customer-dashboard.form.label.spent-money-last-year"/>  
        </th>  
        <td>  
            <acme:print value="spentMoneyLastYear"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.customer-dashboard.form.label.economy-bookings"/>  
        </th>  
        <td>  
            <acme:print value="economyBookings"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.customer-dashboard.form.label.business-bookings"/>  
        </th>  
        <td>  
            <acme:print value="businessBookings"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.customer-dashboard.form.label.booking-count-cost"/>  
        </th>  
        <td>  
            <acme:print value="bookingCountCost"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.customer-dashboard.form.label.booking-average-cost"/>  
        </th>  
        <td>  
            <acme:print value="bookingAverageCost"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.customer-dashboard.form.label.booking-minimum-cost"/>  
        </th>  
        <td>  
            <acme:print value="bookingMinimumCost"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.customer-dashboard.form.label.booking-maximum-cost"/>  
        </th>  
        <td>  
            <acme:print value="bookingMaximumCost"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.customer-dashboard.form.label.booking-deviation-cost"/>  
        </th>  
        <td>  
            <acme:print value="bookingDeviationCost"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.customer-dashboard.form.label.booking-count-passengers"/>  
        </th>  
        <td>  
            <acme:print value="bookingCountPassengers"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.customer-dashboard.form.label.booking-average-passengers"/>  
        </th>  
        <td>  
            <acme:print value="bookingAveragePassengers"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.customer-dashboard.form.label.booking-minimum-passengers"/>  
        </th>  
        <td>  
            <acme:print value="bookingMinimumPassengers"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.customer-dashboard.form.label.booking-maximum-passengers"/>  
        </th>  
        <td>  
            <acme:print value="bookingMaximumPassengers"/>  
        </td>  
    </tr>  
    <tr>  
        <th scope="row">  
            <acme:print code="customer.customer-dashboard.form.label.booking-deviation-passengers"/>  
        </th>  
        <td>  
            <acme:print value="bookingDeviationPassengers"/>  
        </td>  
    </tr>  
</table>  

<div>  
    <canvas id="canvas"></canvas>  
</div>

<acme:return/>