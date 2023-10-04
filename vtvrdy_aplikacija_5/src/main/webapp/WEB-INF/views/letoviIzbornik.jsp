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
  
  .gumb {
    display: inline-block;
    margin-bottom: 10px;
    color: #333;
    text-decoration: none;
    background-color: #EEE;
    padding: 10px;
    border-radius: 5px;
    transition: background-color 0.3s;
    border: none;
    cursor: pointer;
  }
  
  .gumb:hover {
    background-color: #DDD;
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
  Autor: </t>
    ${infoIme}</t>
    ${infoPrezime}<br>
    Predmet: </t>
    ${infoPredmet}<br>
    Godina i verzija aplikacije: </t>
    ${infoGodina}</t>
    ${infoVerzija}</t>
  <h1>Aerodromi izbornik</h1>
</div>
<div class="menu">
  <button class="gumb"><a href="${pageContext.servletContext.contextPath}">Početna stranica</a></button><br>
  <h2>Aerodromi</h2>
  <a href="${pageContext.servletContext.contextPath}/mvc/letovi/pregledSpremljenih">5.6.1 Pregled spremljenih letova u intervalu</a>
  <a href="${pageContext.servletContext.contextPath}/mvc/letovi/pregledSpremljenihNaDan">5.6.2 Pregled spremljenih letova na dan</a>
  <a href="${pageContext.servletContext.contextPath}/mvc/letovi/pregledSpremljenihNaDanOS">5.6.3 Pregled letova na datum </a>
</div>
</body>
</html>
