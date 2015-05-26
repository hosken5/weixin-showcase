<%@page language="java" pageEncoding="utf-8" %>
<HTML>
<HEAD>
    <TITLE>JSP测试页面---HelloWorld!</TITLE>
</HEAD>
<BODY>
    <%
    out.println("<h1>Hello World!<br>世界，你好！</h1>");
    out.println("username:"+request.getParameter("username"));
    %>

    <%
    out.println("session中保存的值为:"+session.getAttribute("user"));
    %>

    <form  method="post" action="/logout">
        <input type="submit" value="Log out" />
    </form>

</BODY>
</HTML>