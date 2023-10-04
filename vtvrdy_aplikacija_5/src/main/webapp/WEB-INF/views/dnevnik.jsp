<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
     import="java.util.List"
    import="org.foi.nwtis.podaci.Dnevnik"
        import="java.lang.Integer"
    import="java.util.Properties, java.io.FileInputStream"
    import="org.foi.nwtis.vtvrdy.projekt.slusaci.SlusacAplikacije"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Projekt</title>
<style>
  table {
      border-collapse: collapse;
      width: 100%;
    }

    th, td {
      text-align: left;
      padding: 8px;
    }

    th {
      background-color: #666;
      color: white;
      font-weight: bold;
      border: 1px solid #ccc;
    }

    tr:nth-child(even) {
      background-color: #f2f2f2;
    }
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
    margin-bottom: 5px;
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
<% 
int odBroja = 1;
int broj = 0;
int brojRedova = Integer.parseInt(SlusacAplikacije.konfig.dajPostavku("stranica.brojRedova"));  
try {
    odBroja = Integer.parseInt(request.getParameter("odBroja"));
    broj = Integer.parseInt(request.getParameter("broj"));
} catch (NumberFormatException e) {
  odBroja = 1;
  broj = brojRedova;
}

%>
<div class="info">
  Autor: </t>
    ${infoIme}</t>
    ${infoPrezime}<br>
    Predmet: </t>
    ${infoPredmet}<br>
    Godina i verzija aplikacije: </t>
    ${infoGodina}</t>
    ${infoVerzija}</t>
  <h1>Dnevnik</h1>
</div>
<div class="menu">
  <button class="gumb"><a href="${pageContext.servletContext.contextPath}">Početna stranica</a></button><br>
<form action="${pageContext.servletContext.contextPath}/mvc/izbornici/dnevnikIzbornik"  method="get">
   <label for="vrsta">Vrsta zahtjeva:</label>
   <input type="text" name="vrsta" id="vrsta" placeholder=${filtar}>
    <input type="hidden" id="odBroja" name="odBroja" value="1">
	<input type="hidden" id="broj" name="broj" value="<%=broj%>">

  <input type="submit" value="Prikaži">
</form><br>
  
    <h2>Zapisi dnevnika</h2>
   <button class="gumb"><a href=${pageContext.servletContext.contextPath}/mvc/izbornici/dnevnikIzbornik?odBroja=1&broj=<%=broj%>&vrsta=${filtar}>Početak</a></button></t>
   <%  if(request.getAttribute("dnevnik") != null && !((List) request.getAttribute("dnevnik")).isEmpty()) {
	if(odBroja > 1) {
  %>
 <button class="gumb">
    <a href=${pageContext.servletContext.contextPath}/mvc/izbornici/dnevnikIzbornik?odBroja=<%= odBroja-broj %>&broj=<%=broj%>&vrsta=${filtar}>Prethodna stranica</a>
 <%
}
%>
</button></t>

<button class="gumb">
    <a href=${pageContext.servletContext.contextPath}/mvc/izbornici/dnevnikIzbornik?odBroja=<%= odBroja+broj %>&broj=<%=broj%>&vrsta=${filtar}>Sljedeća stranica</a>

</button> </t>
  <table>
    <thead>
      <tr>
        <th>Vrsta</th>
        <th>Zahtjev</th>
        <th>Vrijeme</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="dnevnik"  items="${requestScope.dnevnik}">
        <tr>
          <td>${dnevnik.vrsta}</td>
          <td>${dnevnik.zahtjev}</td>
          <td>${dnevnik.vrijeme}</td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
  <%}else { %>
  <div>Ovdje se nema više što prikazati!</div>
  
  <%} %>
</body>
</html>
