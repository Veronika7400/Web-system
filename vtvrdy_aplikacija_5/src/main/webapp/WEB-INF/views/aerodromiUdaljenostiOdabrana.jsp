<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
     import="java.util.List"
    import="org.foi.nwtis.podaci.UdaljenostAerodrom"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pregled manjih udaljenosti u odabranoj državi</title>
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
    <h1>Pregled manjih udaljenosti u odabranoj državi</h1> 
  </div><br>
 
  <button class="gumb"> <a href = ${pageContext.servletContext.contextPath} >Početna stranica</a></button><br>
    <br>
<form action="${pageContext.servletContext.contextPath}/mvc/aerodromi/udaljenostOdabrana"  method="get">
   <label for="icao">Icao:</label>
  <input type="text" name="icao" id="icao" placeholder=${icao}>
   <label for="drzava">Država:</label>
  <input type="text" name="drzava" id="drzava" placeholder=${drzava}>
    <label for="km">Kilometri:</label>
  <input type="text" name="km" id="km" placeholder=${km}>

  <input type="submit" value="Prikaži">
</form><br>
<br>
<%
float ukupno=0;
List<UdaljenostAerodrom> udaljenosti = (List<UdaljenostAerodrom>)request.getAttribute("udaljenosti");
if(udaljenosti != null){
%>
  <table>
    <thead>
      <tr>
        <th>Icao</th>
        <th>Kilometri</th>
      </tr>
    </thead>
    <tbody>
    
      <%
for(UdaljenostAerodrom u : udaljenosti){%>
  <tr>
	  <td><%=u.getIcao() %></td>
	  <td><%=u.getKm() %></td>
  </tr>
    </tbody>

    <%}
      }else { %>
  <div>Ovdje se nema više što prikazati!</div>
  
  <%} %>
</body>
  </table>
</html>