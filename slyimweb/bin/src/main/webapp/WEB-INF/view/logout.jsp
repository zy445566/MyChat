<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html lang="en">
  <head>
    <title>Hello Security</title>
  </head>

  <body>
    <div class="container">
      <h1>This is secured!</h1>
      <p>
        Hello <b><c:out value="${pageContext.request.remoteUser}"/></b>
      </p>
      <c:url var="logoutUrl" value="/logout"/>
      <form class="form-inline" action="${logoutUrl}" method="post">
          <input type="submit" value="Log out" />
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      </form>
    </div>
  </body>
</html>