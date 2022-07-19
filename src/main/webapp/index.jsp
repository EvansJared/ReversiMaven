<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
<style>
.gameArea{
 	width:430px;
 	border: 5px black solid;
 	padding-right: 7px;
 	background-color: rgb(170, 121, 65);
}
.grid{
	width: 50px;
	column-width: 50px;
	display: board;
	grid: 50px/auto auto auto;
	grid-gap: 2px;
	padding: 2px;
	display: grid;
	column-width: 40px;
}


.col {
	height: 30px;
	width: 50px;
	line-height: 30px;
	text-align: center;
	vertical-align: middle;
	text-align: center;
}

.col{
	background-color: green;
}

.WHITE {
	background-color: rgb(254, 255, 255);
}

.BLACK {
	background-color: rgb(0, 0, 0);
}

.win {
	text-align: center;
	background-color: gold;
}



</style>
  </head>
  
  <body>
  
  <div class="gameArea">
  <form action="<c:url value='makeMove'/>">
  <table>
  <div class="grid">
    
    <tr><c:forEach items="${game.board}" var="row" varStatus="y" >
    
    <c:forEach var="col" varStatus="x" items="${row}">
    <td>
  
    <button class="col" type="submit" name="loc" value="${x.index} ${y.index}">${col}</button>
    
    </td>
  </c:forEach>
  </tr>
  </c:forEach>
	</div>
    <button type="submit" name="newGame">New Game</button>
    
    
    <td colspan="4">
    <ul>
   <li> Current player: ${game.currentPlayer}
   <li> Is game over? ${game.over}
   <li> Winner: ${game.winner}
   
    </ul>
    </td>
    </table>
    
    </form>
    
    </div>
  </body>
</html>
