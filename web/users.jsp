<%--
  Created by IntelliJ IDEA.
  User: airid
  Date: 2020-11-25
  Time: 22:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>UserList</title>
    <h1>List of system users</h1>
    <p>
        <jsp:useBean id="bnd" scope="session" class="webControllers.WebUserController"></jsp:useBean>
        <%=bnd.userlist()%>
    </p>
</head>
<body>

</body>
</html>
