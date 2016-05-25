<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.5.24.0024
  Time: 下午 9:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>mhealth</title>
    <%@ include file="/WEB-INF/views/base/head.jsp" %>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-12 userRegSucc">
            <h2><span>${loginName},</span>恭喜您注册成功！</h2>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12 userRegSucc">
            <h4 id="autoJump"><span></span>秒后自动跳转……</h4>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12 userRegSuccBtn">
            <button id="returnLogin" class="btn btn-success">返回登录</button>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        var time = 5;
        autoJump();
        $("#returnLogin").click(function () {
            location.href = "<%=path%>/user/login.ui";
        });

        function autoJump() {
            $("#autoJump").find("span").text(time);
            time--;
            if (time == -1) location.href = "<%=path%>/user/login.ui";
            setTimeout(autoJump, 1000);
        }
    });
</script>
</body>
</html>
