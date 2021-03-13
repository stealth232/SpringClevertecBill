<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Check</title>
</head>
<body>
<style>
    .semi {
        opacity: 0.5;
    }
</style>
<div class="semi"><pre>     New version of checkService</pre></div>
<head><h2><pre><p><c:out value="    CASH RECEIPT"/></p></pre></h2></head>
<h2><pre> <c:out value=' "The Two Geese"'/></pre></h2>
<p><c:out value="${order.getDate()}"/></p>
<table>
    <thead>
    <tr>
        <th>QTY</th>
        <th>PRODUCT</th>
        <th>COST</th>
        <th>TOTAL</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="product" items="${list}">
        <tr>
            <td>${product.quantity}</td>
            <td>${product.name}</td>
            <td>${String.format('%.2f',product.price)}</td>
            <td>${String.format('%.2f', product.totalPrice)}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div>
    <th>========================</th>
</div>

<c:if test="${order.getCard() > 0}">
<div> <td>${String.format('%s %d %s%.0f %s', "Your card #",order.getCard(),
     " with ", order.cardPercent, "% discont")} </td></div>
<div> <td>${String.format('%s %.2f', "Your Discount ",order.getDiscount())} </td></div>
    <div> <td>${String.format('%s %.2f', "Total Price ",order.getTotalPrice())} </td></div>
</c:if>
<c:if test="${order.getCard() == 0}">
    <div> <td>${String.format("No card")} </td></div>
    <div> <td>${String.format('%s %.2f', "Your Discount ",order.getDiscount())} </td></div>
    <div> <td>${String.format('%s %.2f', "Total Price ",order.getTotalPrice())} </td></div>
</c:if>
<br/>
<br/>
<form action="/check" method="GET">
    <tr>
        <th>Enter your email</th><br/>
        <td><input type="text" name="mail" placeholder="mail"></td>
        <br/>
        <td><input type="submit" value="Send"></td>
    </tr>
    ${pageContext.request.contextPath}
</form>
<br/>
<br/>
<div class="semi"><pre>           Old version of checkService</pre></div>
${html}
</body>
</html>
