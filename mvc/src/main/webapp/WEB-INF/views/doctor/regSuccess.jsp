<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.5.25.0025
  Time: 下午 2:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>mhealth_医生端</title>
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
            <p>您的信息将交由管理人员进行审核，通过后方可正常登录使用。</p>
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
            location.href = "<%=path%>/doctor/login.ui";
        });

        function autoJump() {
            $("#autoJump").find("span").text(time);
            time--;
            if (time == -1) location.href = "<%=path%>/doctor/login.ui";
            setTimeout(autoJump, 1000);
        }
    });
</script>
</body>
</html>
