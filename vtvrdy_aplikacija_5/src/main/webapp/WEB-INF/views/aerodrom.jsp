<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
     import="org.foi.nwtis.vtvrdy.aplikacija_4.ws.WsMeteo.endpoint.MeteoPodaci"
     import="org.foi.nwtis.vtvrdy.aplikacija_4.ws.WsMeteo.endpoint.Meteo"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Aerodrom</title>
<style>
  form {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #CCC;
  border-radius: 5px;
  background-color: #CCC; 
}
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
 <div  class="info">
  Autor: </t>
    ${infoIme}</t>
    ${infoPrezime}<br>
    Predmet: </t>
    ${infoPredmet}<br>
    Godina i verzija aplikacije: </t>
    ${infoGodina}</t>
    ${infoVerzija}</t>
 <h1>Aerodrom</h1> 
 </div>
<br>
  <button class="gumb"> <a href = ${pageContext.servletContext.contextPath} >Početna stranica</a></button><br>
    <br>
    <h2>Podaci o aerodromu</h2> 
<table>
<tr><td>ICAO:</td><td>${aerodrom.icao}</td></tr>
<tr><td>Naziv:</td><td>${aerodrom.naziv}</td></tr>
<tr><td>Država:</td><td>${aerodrom.drzava}</td></tr>
</table>

    <br><br>
<%  if(request.getAttribute("meteo") != null) {
  %>
      <h2>Meteo podaci za aerodrom</h2> 
<table>
<% MeteoPodaci meteo = (MeteoPodaci)request.getAttribute("meteo"); %>
<tr><td>Naziv:</td><td><%=meteo.getCloudsName() %></td></tr>
<tr><td>Vrijednost:</td><td><%=meteo.getCloudsValue() %></td></tr>
<tr><td>Jedinica vlage:</td><td><%=meteo.getHumidityUnit() %></td></tr>
<tr><td>Vlaga:</td><td><%=meteo.getHumidityValue() %></td></tr>
<tr><td>Posljednje ažuriranje:</td><td><%=meteo.getLastUpdate() %></td></tr>
<tr><td>Oborine:</td><td><%=meteo.getPrecipitationMode() %></td></tr>
<tr><td>Jedinica oborine:</td><td><%=meteo.getPrecipitationUnit() %></td></tr>
<tr><td>Vrijednost oborine:</td><td><%=meteo.getPrecipitationValue() %></td></tr>
<tr><td>Jedinica tlaka:</td><td><%=meteo.getPressureUnit() %></td></tr>
<tr><td>Tlak:</td><td><%=meteo.getPressureValue() %></td></tr>
<tr><td>Izlazak sunca:</td><td><%=meteo.getSunRise() %></td></tr>
<tr><td>Zalazak sunca:</td><td><%=meteo.getSunSet() %></td></tr>
<tr><td>Maksimalna temperatura:</td><td><%=meteo.getTemperatureMax() %></td></tr>
<tr><td>Minimalna temperatura:</td><td><%=meteo.getTemperatureMin() %></td></tr>
<tr><td>Jedinica temperature:</td><td><%=meteo.getTemperatureUnit() %></td></tr>
<tr><td>Temperatura:</td><td><%=meteo.getTemperatureValue() %></td></tr>
<tr><td>Ikona vremena:</td><td><%=meteo.getWeatherIcon() %></td></tr>
<tr><td>Broj vremena:</td><td><%=meteo.getWeatherNumber() %></td></tr>
<tr><td>Vrijeme:</td><td><%=meteo.getWeatherValue() %></td></tr>
</table>
<%}else { %>
  <div>Nema meteo podataka!</div>
  
  <%} %>
  </body>
</html>