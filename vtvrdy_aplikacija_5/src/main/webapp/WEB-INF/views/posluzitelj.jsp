<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
   display: flex;
    margin: 20px;
  }
  div.menu button {
  flex: 1;
  margin-right: 10px;
  margin-left: 10px;
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
  p {
  font-size: 18px; 
  text-align: center; 
  display: block;

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
  <h1>Poslužitelj</h1>
</div>
<button class="gumb"><a href="${pageContext.servletContext.contextPath}">Početna stranica</a></button><br>

<div class="menu">
 
   <button class="gumb"><a href="${pageContext.servletContext.contextPath}/mvc/posluzitelj/status">STATUS</a></button>
  <br>
   <button class="gumb"><a href="${pageContext.servletContext.contextPath}/mvc/posluzitelj/kraj">KRAJ</a></button>

  <br>
   <button class="gumb"><a href="${pageContext.servletContext.contextPath}/mvc/posluzitelj/init">INIT</a></button>

  <br>
   <button class="gumb"><a href="${pageContext.servletContext.contextPath}/mvc/posluzitelj/pauza">PAUZA</a></button>

  <br>
   <button class="gumb"><a href="${pageContext.servletContext.contextPath}/mvc/posluzitelj/infoDa">INFO DA</a></button>

  <br>
   <button class="gumb"><a href="${pageContext.servletContext.contextPath}/mvc/posluzitelj/infoNe">INFO NE</a></button>
  <br>
  
</div>
<c:if test="${not empty poruka}">
    <p>${poruka}</p><br>
  </c:if>
</body>
</html>
