<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Projekt</title>
<style>
  body {
    font-family: Arial, sans-serif;
  }
  
  h1 {
    text-align: center;
  }

  div.info {
    background-color: #CCC;
    font-size: 14px;
    padding: 10px;
  }
  
  div.menu {
    margin: 20px;
  }
  
  div.menu a {
    display: block;
    margin-bottom: 10px;
    color: #333;
    text-decoration: none;
    background-color: #EEE;
    padding: 10px;
    border-radius: 5px;
    transition: background-color 0.3s;
  }
  
  div.menu a:hover {
    background-color: #DDD;
  }
</style>
</head>
<body>
<div class="info">
  <h1>Početna stranica projekta</h1>
</div>
<div class="menu">
  <h2>Početni izbornik</h2>
  <a href="${pageContext.servletContext.contextPath}/mvc/izbornici/korisniciIzbornik">5.2 Korisnici</a>
  <a href="${pageContext.servletContext.contextPath}/mvc/izbornici/posluziteljIzbornik">5.3 Poslužitelj</a>
  <a href="${pageContext.servletContext.contextPath}/mvc/poruke/porukeIzbornik">5.4 Poruke</a>
  <a href="${pageContext.servletContext.contextPath}/mvc/izbornici/aerodromiIzbornik">5.5 Aerodromi</a>
  <a href="${pageContext.servletContext.contextPath}/mvc/izbornici/letoviIzbornik">5.6 Letovi</a>
  <a href="${pageContext.servletContext.contextPath}/mvc/izbornici/dnevnikIzbornik">5.7 Dnevnik</a>
</div>
</body>
</html>
