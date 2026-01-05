<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Order Success</title>
    <style>
        body { font-family: sans-serif; text-align: center; padding-top: 50px; background-color: #e8f5e9; }
        .card { background: white; padding: 20px; display: inline-block; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
        h1 { color: #28a745; }
    </style>
</head>
<body>
    <div class="card">
        <h1>🎉 Order Confirmed!</h1>
        <p>You have successfully purchased: <strong><%= request.getAttribute("userProduct") %></strong></p>
        <p>Total Amount Paid: <strong>₹<%= request.getAttribute("userTotal") %></strong></p>
        <br>
        <a href="index.html">Buy Something Else</a>
    </div>
</body>
</html>