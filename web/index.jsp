<%@ page contentType="text/html;charset=UTF-8" language="java" import="model.Users" %>
<%
  Users users = new Users();
  String utoken = (String) session.getAttribute("utoken");
  String login = users.authentication(utoken);
  if (utoken == null){
    response.sendRedirect("/login");
  }else if(login.length() < 5){
    response.sendRedirect("/login");
  }
%>
<%@include file="/WEB-INF/include/header.jsp"%>
<html>
  <head>
    <title>Сайт</title>
  </head>
  <body>
  <header>
    <div class="top-header">
      <nav class="navbar navbar-inverse" style="border-radius: 0;">
        <div class="container-fluid">
          <ul class="nav navbar-nav pull-right">
            <li><a href="javascript:void(0);">Приветь: <strong style="text-decoration: underline;"><%= login %></strong></a></li>
            <li><a href="/logout">Выход</a></li>
          </ul>
        </div>
      </nav>

    </div>
  </header>

  </body>
</html>
