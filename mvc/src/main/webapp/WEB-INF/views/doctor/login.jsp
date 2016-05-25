<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.5.10.0010
  Time: 下午 3:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>mhealth_医生端</title>
    <%@ include file="/WEB-INF/views/base/head.jsp" %>
</head>
<body id="docLoginbg">

<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">mhealth</a>
            <ul class="nav navbar-nav">
                <li><a>请您登录</a></li>
                <li><a href="<%=path%>/">前往用户端</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <div class="row">
        <div id="logo" class="col-md-7">
            <h1><span>mhealth</span></h1>
            <h4>医生端</h4>>
        </div>
        <div id="docFrame" class="col-md-4">
            <div class="docContent">

                <!-- Nav tabs -->
                <ul id="user-tabs" class="nav nav-tabs nav-justified" role="tablist">
                    <li role="presentation" class="active"><a href="#login" aria-controls="login" role="tab"
                                                              data-toggle="tab">登录</a></li>
                    <li role="presentation"><a href="<%=path%>/doctor/register.ui">注册</a>
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
                                    <input type="text" name="loginName" class="form-control" placeholder="用户名"
                                           required="required">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="input-group">
                                    <span class="input-group-addon"><span
                                            class="glyphicon glyphicon-lock"></span></span>
                                    <input name="password" type="password" class="form-control" placeholder="密码"
                                           required="required">
                                </div>
                            </div>
                            <button type="submit" class="btn btn-primary btn-lg btn-block">登录</button>
                        </form>
                    </div>
                    <!--End login-->
                </div>
                <!--  End tab panes -->

            </div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/base/footer.jsp" %>
<script type="text/javascript">
    $(document).ready(function () {
        var uf = $("#docFrame").css("height");
        var ufh = uf.substr(0, uf.length - 2);
        var h1 = $("#logo>h1").css("height");
        var h1h = h1.substr(0, h1.length - 2);
        var height = (Number(ufh) - Number(h1h)) / 2;
        $("#logo>h1").css("margin-top", height);
//        $("#logo,#docFrame").css("margin-top", "50px");
        $("#logo,#docFrame").css("margin-top", ($(window).height() - $("#docFrame").height()) / 2 - 70);

        $("#loginForm").bootstrapValidator({
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            live: 'submitted',
            fields: {
                loginName: {
                    enabled: true,
                    validators: {
                        notEmpty: {
                            message: "此项为必填项"
                        }
                    }
                },
                password: {
                    enabled: true,
                    validators: {
                        notEmpty: {
                            message: "此项为必填项"
                        }
                    }
                }
            }
        }).on("success.form.bv", function (e) {
            e.preventDefault();
            $.ajax({
                url: "<%=path%>/service/doctorData/login",
                type: "post",
                data: $("#loginForm").serialize(),
                dataType: "json",
                success: function (data) {
                    if (data.resCode == "000000") {
                        location.href = "<%=path%>/doctor/getUsers.ui";
                    } else swal({
                        title: "错误",
                        text: data.resCode + ":" + data.resMsg,
                        type: "error",
                        confirmButtonText: "确定"
                    });
                }
            });
        });
    });
</script>
</body>
</html>
