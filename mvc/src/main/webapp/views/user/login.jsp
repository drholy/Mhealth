<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.4.25.0025
  Time: 下午 10:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>mhealth</title>
    <%@ include file="/views/base/head.jsp" %>
</head>
<body id="loginbg">

<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">mhealth</a>
            <ul class="nav navbar-nav">
                <li><a>请您登录</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <div class="row">
        <div id="logo" class="col-md-7">
            <h1><span>mhealth</span></h1>
        </div>
        <div id="userFrame" class="col-md-4">
            <div class="userContent">

                <!-- Nav tabs -->
                <ul id="user-tabs" class="nav nav-tabs nav-justified" role="tablist">
                    <li role="presentation" class="active"><a href="#login" aria-controls="login" role="tab"
                                                              data-toggle="tab">登录</a></li>
                    <li role="presentation"><a href="#register" aria-controls="register" role="tab"
                                               data-toggle="tab">注册</a>
                    </li>
                </ul>

                <!-- Tab panes -->
                <div class="tab-content">
                    <!-- login -->
                    <div role="tabpanel" class="tab-pane fade in active" id="login">
                        <form id="loginForm">
                            <div class="form-group">
                                <div class="input-group">
                                    <span class="input-group-addon"><span
                                            class="glyphicon glyphicon-user"></span></span>
                                    <input type="text" name="loginName" class="form-control" placeholder="用户名" required="required">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="input-group">
                                    <span class="input-group-addon"><span
                                            class="glyphicon glyphicon-lock"></span></span>
                                    <input name="password" type="password" class="form-control" placeholder="密码" required="required">
                                </div>
                            </div>
                            <button type="submit" class="btn btn-primary btn-lg btn-block">登录</button>
                        </form>
                    </div>
                    <!--End login-->
                    <!-- register -->
                    <div role="tabpanel" class="tab-pane fade" id="register">
                        <form id="registerForm" class="form-horizontal">
                            <div class="form-group">
                                <label for="regLoginName" class="col-md-4 control-label">用户名*</label>
                                <div class="col-md-8">
                                    <input name="loginName" type="text" class="form-control" id="regLoginName"
                                           placeholder="用户名" required="required">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="regPassword" class="col-md-4 control-label">密码*</label>
                                <div class="col-md-8">
                                    <input name="password" type="password" class="form-control" id="regPassword"
                                           placeholder="密码" required="required">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="regPwAgain" class="col-md-4 control-label">密码确认*</label>
                                <div class="col-md-8">
                                    <input type="password" class="form-control" id="regPwAgain" placeholder="密码确认" required="required">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="valid" class="col-md-4 control-label">验证码*</label>
                                <div class="input-group col-md-8">
                                    <input id="valid" name="valid" type="text" class="form-control" placeholder="验证码" required="required">
                                    <span class="input-group-addon"><img src="<%=path%>/service/user/validCode"
                                                                         onclick="this.src='<%=path%>/service/user/validCode?r='+Math.random()+';'"
                                                                         style="cursor:hand" alt="验证码"></span>
                                </div>
                            </div>
                            <button type="submit" class="btn btn-primary btn-lg btn-block">提交</button>
                        </form>
                    </div>
                    <!--End register-->
                </div>
                <!--  End tab panes -->

            </div><!--End userContent-->
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        var uf = $("#userFrame").css("height");
        var ufh = uf.substr(0, uf.length - 2);
        var h1 = $("#logo>h1").css("height");
        var h1h = h1.substr(0, h1.length - 2);
        var height = (Number(ufh) - Number(h1h)) / 2;
        $("#logo>h1").css("margin-top", height);
        $("#logo,#userFrame").css("margin-top", "50px");

        $("#loginForm").submit(function () {
            $.ajax({
                url: "<%=path%>/service/user/login",
                type: "post",
                data: $("#loginForm").serialize(),
                dataType: "json",
                success: function (data) {
                    if (data.resCode == "000000") {
                        location.href = "<%=path%>/record/overview.ui";
                    } else if (data.resCode == "100104") {
                        location.href = "<%=path%>/user/active.ui"
                    } else alert(data.resCode + ":" + data.resMsg);
                }
            });
            return false;
        });

        $("#registerForm").submit(function () {
            if ($("#regPassword").val() != $("#regPwAgain").val()) {
                alert("密码不一致");
                return false;
            }
            var user = {};
            user["loginName"] = $("#regLoginName").val();
            user["password"] = $("#regPassword").val();
            user = JSON.stringify(user);
            $.ajax({
                url: "<%=path%>/service/user/insertUser",
                type: "post",
                data: {userJson: user, valid: $("#valid").val()},
                dataType: "json",
                success: function (data) {
                    if (data.resCode == "000000") {
                        alert(data.data.loginName);
                    } else alert(data.resCode + ":" + data.resMsg);
                }
            });
            return false;
        });
    });
</script>
</body>
</html>
