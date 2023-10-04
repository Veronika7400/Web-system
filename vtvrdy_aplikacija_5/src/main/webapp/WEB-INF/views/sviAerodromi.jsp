<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
        import="java.util.List"
    import="org.foi.nwtis.podaci.Aerodrom"
    import="java.lang.Integer"
    import="java.util.Properties, java.io.FileInputStream"
    import="org.foi.nwtis.vtvrdy.projekt.slusaci.SlusacAplikacije"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Svi aerodromi</title>
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
	h1 {
	  text-align: center;
	}

    div.info {
      background-color: #CCC;
      font-size: 14px;
      padding: 10px;
      }
        .gumb {
    background-color: #ccc; 
    color: #fff;
    padding: 7px 7px; 
    text-align: center;
    cursor: pointer;
    border-radius: 10px; 
  }
  a{
   color: black;
  }
  </style>
</head>
<body>
<%
int odBroja = 1;
int broj = 0;
String drzava = (String)request.getAttribute("drzava"); 
String naziv =  (String)request.getAttribute("naziv"); 
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
    <h1>Pregled svih aerodroma</h1> 
  </div><br>
  <button class="gumb"> <a href = ${pageContext.servletContext.contextPath} >Početna stranica</a></button><br></t>
<br>
 <form action="${pageContext.servletContext.contextPath}/mvc/aerodromi/svi?odBroja=1&broj="<%=broj%>  method="get">
   <label for="traziNaziv">Naziv:</label>
  <input type="text" name="traziNaziv" id="traziNaziv" placeholder=${naziv}>
   <label for="traziDrzavu">Drzava:</label>
  <input type="text" name="traziDrzavu" id="traziDrzavu" placeholder=${drzava}>

  <input type="submit" value="Filtriraj">
</form><br>


  <button class="gumb"><a href=${pageContext.servletContext.contextPath}/mvc/aerodromi/svi?odBroja=1&broj=<%=broj%>&traziDrzavu=<%=drzava%>&traziNaziv=<%=naziv%>>Početak</a></button></t>

<%  if(request.getAttribute("aerodromi") != null && !((List) request.getAttribute("aerodromi")).isEmpty()) {
	if(odBroja > 1) {
  %>
 <button class="gumb">
    <a href=${pageContext.servletContext.contextPath}/mvc/aerodromi/svi?odBroja=<%= odBroja-broj %>&broj=<%=broj%>&traziDrzavu=<%=drzava%>&traziNaziv=<%=naziv%>>Prethodna stranica</a>
 <%
}
%>
</button></t>

<button class="gumb">
    <a href=${pageContext.servletContext.contextPath}/mvc/aerodromi/svi?odBroja=<%= odBroja+broj %>&broj=<%=broj%>&traziDrzavu=<%=drzava%>&traziNaziv=<%=naziv%>>Sljedeća stranica</a>

</button> </t>
  <table>
    <thead>
      <tr>
        <th>ICAO</th>
        <th>Naziv</th>
        <th>Država</th>
        <th>Latitude</th>
        <th>Longitude</th>
        <th>5.5.2. Pregled aerodroma</th>
        <th>Dodaj za preuzimanje</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="aerodromi"  items="${requestScope.aerodromi}">
        <tr>
          <td>${aerodromi.icao}</td>
          <td>${aerodromi.naziv}</td>
          <td>${aerodromi.drzava}</td>
          <td>${aerodromi.lokacija.latitude}</td>
          <td>${aerodromi.lokacija.longitude}</td>
          <td><button class="gumb"> <a href =${pageContext.servletContext.contextPath}/mvc/aerodromi/pregled/${aerodromi.icao}>Pregled</a>
          </button></td>
          <td><button class="gumb"> <a href =${pageContext.servletContext.contextPath}/mvc/aerodromi/dodaj/${aerodromi.icao}>Dodaj</a>
          </button></td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
  <%}else { %>
  <div>Ovdje se nema više što prikazati!</div>
  
  <%} %>
</body>
</html>