<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.5.18.0018
  Time: 下午 3:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">mhealth</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="<%=request.getContextPath()%>/admin/examDoc.ui">医生申请</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><p class="navbar-text">欢迎</p></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">${sessionScope.admin.loginName}<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="#" id="adminLogout">退出</a></li>
                    </ul>
                </li>
            </ul>
        </div><!--/.nav-collapse -->
    </div>
</nav>
