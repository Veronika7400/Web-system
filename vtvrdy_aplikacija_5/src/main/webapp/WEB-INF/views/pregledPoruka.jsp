<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
        import="java.util.List"
    import="org.foi.nwtis.vtvrdy.projekt.zrna.SakupljacJmsPoruka"
    import="java.lang.Integer"
    import="java.util.Properties, java.io.FileInputStream"
    import="org.foi.nwtis.vtvrdy.projekt.slusaci.SlusacAplikacije"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Poruke</title>
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
  <div  class="info">
    Autor: </t>
    ${infoIme}</t>
    ${infoPrezime}<br>
    Predmet: </t>
    ${infoPredmet}<br>
    Godina i verzija aplikacije: </t>
    ${infoGodina}</t>
    ${infoVerzija}</t>
    <h1>Pregled svih poruka</h1> 
  </div><br>
 <button class="gumb"> <a href = ${pageContext.servletContext.contextPath} >Početna stranica</a></button></t>
 <h2>Primljene poruke</h2>
 
   <button class="gumb"><a href=${pageContext.servletContext.contextPath}/mvc/poruke/porukeIzbornik?odBroja=1&broj=<%=broj%>>Početak</a></button></t>

<%  if(request.getAttribute("poruke") != null && !((List) request.getAttribute("poruke")).isEmpty()) {
%> 
 <button class="gumb"><a href=${pageContext.servletContext.contextPath}/mvc/poruke/porukeIzbornik/obrisi>Obriši</a></button></t>
<%
if(odBroja > 1) {
  %>
 <button class="gumb">
    <a href=${pageContext.servletContext.contextPath}/mvc/poruke/porukeIzbornik?odBroja=<%= odBroja-broj %>&broj=<%=broj%>>Prethodna stranica</a>
 <%
}
%>
</button></t>
<button class="gumb">
    <a href=${pageContext.servletContext.contextPath}/mvc/poruke/porukeIzbornik?odBroja=<%= odBroja+broj %>&broj=<%=broj%>>Sljedeća stranica</a>
  </button> </t>
  <table>
    <thead>
      <tr>
        <th>Pristigle poruke</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="poruka"  items="${requestScope.poruke}">
        <tr>
          <td>${poruka}</td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
  <%}else { %>
  <div> Nema poruka!</div>
  
  <%} %>
</body>
</html>