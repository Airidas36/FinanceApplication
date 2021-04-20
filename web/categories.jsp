<%--
  Created by IntelliJ IDEA.
  User: airid
  Date: 2020-11-25
  Time: 23:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Categories</title>
    <h1> List of categories</h1>
    <jsp:useBean id="sys" scope="session" class="webControllers.WebCategoryController"></jsp:useBean>
    <p>Categories: <%=sys.getCats()%></p>
</head>
<body>

</body>
</html>
