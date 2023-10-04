<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
     import="java.util.List"
    import="org.foi.nwtis.vtvrdy.aplikacija_4.ws.WsKorisnici.endpoint.Korisnik"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pregled korisnika</title>
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
    <h1>Pregled korisnika</h1> 
  </div><br>
 
  <button class="gumb"> <a href = ${pageContext.servletContext.contextPath} >Početna stranica</a></button><br>
    <br>
    <div>
  <input type="text" name="traziIme" id="traziIme" placeholder="Filtar ime">
  <input type="text" name="traziPrezime" id="traziPrezime" placeholder="Filtar prezime">
  <button class="gumb" onclick="prikaziKorisnike()">Prikaži</button>
</div>
<script>
  function prikaziKorisnike() {
    var ime = document.getElementById("traziIme").value;
    var prezime = document.getElementById("traziPrezime").value;
    var putanja = "${pageContext.servletContext.contextPath}/mvc/korisnici/pregledaj?traziImeKorisnika=" + ime + "&traziPrezimeKorisnika=" + prezime;
    window.location.href = putanja;
  }
</script>
<br>
<%
String poruka = (String)request.getAttribute("poruka");
List<Korisnik> korisnici = (List<Korisnik>)request.getAttribute("korisnici");
if(poruka == null){
%>
  <table>
    <thead>
      <tr>
        <th>Ime</th>
        <th>Prezime</th>
        <th>Mail</th>
        <th>Korisničko ime</th>
        <th>Lozinka</th>
      </tr>
    </thead>
    <tbody>
    
      <%
for(Korisnik k : korisnici){%>
  <tr>
	  <td><%=k.getIme() %></td>
	  <td><%=k.getPrezime() %></td>
	  <td><%=k.getMail() %></td>
	  <td><%=k.getKorime() %></td>
	  <td><%=k.getLozinka() %></td>
  </tr><%
}
%>
    </tbody>
  </table>
    <%}else { %>
  <div><%=poruka %></div>
  
  <%} %>
</body>
</html>