<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Session Expired</title>

<link href="css/bootstrap.min.css" rel="stylesheet">

<style>
    body {
        background: linear-gradient(135deg, #f8f9fa, #e9ecef);
        height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
        font-family: Arial, sans-serif;
    }

    .card-box {
        max-width: 420px;
        width: 100%;
        padding: 30px;
        border-radius: 15px;
        background: #ffffff;
        box-shadow: 0 10px 30px rgba(0,0,0,0.15);
        text-align: center;
    }

    .icon {
        font-size: 60px;
        color: #dc3545;
        margin-bottom: 15px;
    }

    .title {
        font-size: 24px;
        font-weight: bold;
        color: #dc3545;
    }

    .text {
        color: #6c757d;
        margin-top: 10px;
        margin-bottom: 20px;
    }

    .btn-login {
        background: #0d6efd;
        color: white;
        padding: 10px 20px;
        border-radius: 8px;
        text-decoration: none;
        display: inline-block;
        transition: 0.3s;
    }

    .btn-login:hover {
        background: #0b5ed7;
        color: #fff;
    }
</style>

</head>

<body>

<div class="card-box">
    

    <div class="title">Session Expired</div>

    <div class="text">
        Your session has expired due to inactivity.<br>
        Please login again to continue.
    </div>

    <a href="Login.html" class="btn-login">Login Again</a>

</div>

</body>
</html>