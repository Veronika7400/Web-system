<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.List"
     import="java.lang.Integer"
    import="org.foi.nwtis.vtvrdy.aplikacija_4.ws.WsLetovi.endpoint.LetAviona"
import="org.foi.nwtis.vtvrdy.aplikacija_4.ws.WsLetovi.endpoint.Letovi"
import="org.foi.nwtis.vtvrdy.projekt.slusaci.SlusacAplikacije"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Prikaz letova</title>
<style>

label {
  display: block;
  margin-bottom: 10px;
  font-weight: bold;
}

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
String poruka = (String)request.getAttribute("poruka");
String icao = (String)request.getAttribute("icao");
String danOd = (String)request.getAttribute("danOd");
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
    <h1>Prikaz letova</h1> 
  </div>
  <br>
  <button class="gumb"> <a href = ${pageContext.servletContext.contextPath} >Početna stranica</a></button></t>
 
<%if(poruka == null){ %>
 <form action="${pageContext.servletContext.contextPath}/mvc/letovi/pregledSpremljenihNaDan" method="get">
  <label for="icao">Icao:</label>
  <input type="text" id="icao" name="icao" placeholder="<%=icao%>">

  <label for="danOd">Dan od:</label>
  <input type="text" id="danOd" name="danOd" placeholder="<%=danOd%>">

  <input type="hidden" id="odBroja" name="odBroja" value="1">
	<input type="hidden" id="broj" name="broj" value="<%=broj%>">


  <br><input type="submit" value="Prikaži"> <br>
</form>

  <button class="gumb"><a href=${pageContext.servletContext.contextPath}/mvc/letovi/pregledSpremljenihNaDan?odBroja=1&broj=<%=broj%>&icao=<%=icao%>&danOd=<%=danOd%>>Početak</a></button></t>

<%  if(request.getAttribute("polasci") != null && !((List) request.getAttribute("polasci")).isEmpty()) {
	if(odBroja > 1) {
  %>
 <button class="gumb">
    <a href=${pageContext.servletContext.contextPath}/mvc/letovi/pregledSpremljenihNaDan?odBroja=<%= odBroja-broj %>&broj=<%=broj%>&icao=<%=icao%>&danOd=<%=danOd%>>Prethodna stranica</a>
 <%
}
%>
</button></t>

<button class="gumb">
    <a href=${pageContext.servletContext.contextPath}/mvc/letovi/pregledSpremljenihNaDan?odBroja=<%= odBroja+broj %>&broj=<%=broj%>&icao=<%=icao%>&danOd=<%=danOd%>>Sljedeća stranica</a>

</button> </t>
  <table>
    <thead>
      <tr>
       	<th>ICAO24</th>
		<th>Aerodrom polaska</th>
		<th>Aerodrom dolaska</th>
		<th>Polazak</th>
		<th>Dolazak</th>
		<th>Pozivni znak</th>
		<th>Horizontalna distanca aerodrom polaska</th>
		<th>Vertikalna distanca aerodrom polaska</th>
		<th>Horizontalna distanca aerodrom dolaska</th>
		<th>Vertikalna distanca aerodrom dolaska</th>
		<th>Broj kandidata aerodroma polaska</th>
		<th>Broj kandidata aerodroma dolaska</th>
      </tr>
    </thead>
     <tbody>
    	<c:forEach var="a" items="${requestScope.polasci}">
					<tr>
						<td>${a.icao24}</td>
						<td>${a.estDepartureAirport}</td>
						<td>${a.estArrivalAirport}</td>
						<td>${a.firstSeen}</td>
						<td>${a.lastSeen}</td>
						<td>${a.callsign}</td>
						<td>${a.estDepartureAirportHorizDistance}</td>
						<td>${a.estDepartureAirportVertDistance}</td>
						<td>${a.estArrivalAirportHorizDistance}</td>
						<td>${a.estArrivalAirportVertDistance}</td>
						<td>${a.departureAirportCandidatesCount}</td>
						<td>${a.arrivalAirportCandidatesCount}</td>	
					</tr>
				</c:forEach>
    </tbody>
  </table>
  <%}else { %>
  <div>Ovdje se nema više što prikazati!</div>
  
  <%}
}else { %>
  <div><%=poruka %></div>
  
  <%} %>

</body>
</html>