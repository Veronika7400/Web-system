<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
     import="java.util.List"
    import="org.foi.nwtis.podaci.Udaljenost"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Udaljenost između dva aerodroma</title>
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
    <h1>Pregled udaljenosti između dva aerodroma</h1> 
  </div><br>
 
  <button class="gumb"> <a href = ${pageContext.servletContext.contextPath} >Početna stranica</a></button><br>
    <br>
<form action="${pageContext.servletContext.contextPath}/mvc/aerodromi/udaljenostDva"  method="get">
   <label for="icaoOd">Icao od:</label>
  <input type="text" name="icaoOd" id="icaoOd" placeholder=${icaoOd}>
   <label for="icaoDo">Icao do:</label>
  <input type="text" name="icaoDo" id="icaoDo" placeholder=${icaoDo}>

  <input type="submit" value="Prikaži">
</form><br>
<br>
  <table>
    <thead>
      <tr>
        <th>Udaljenost</th>
        <th>${udaljenost}</th>
      </tr>
    </thead>
  </table>
</body>
</html>