<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.4.25.0025
  Time: 下午 10:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>mhealth</title>
    <%@ include file="/WEB-INF/views/base/head.jsp" %>
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
                                    <input type="password" class="form-control" id="regPwAgain" placeholder="密码确认"
                                           required="required">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="valid" class="col-md-4 control-label">验证码*</label>
                                <div class="col-md-5">
                                    <input id="valid" name="valid" type="text" class="form-control" placeholder="验证码"
                                           required="required">
                                </div>
                                <label class="col-md-3 control-label"><img id="validPic"
                                                                           src="<%=path%>/service/user/validCode"
                                                                           style="cursor:hand" alt="验证码"></label>
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

        $("#validPic").click(function () {
            var src = "<%=path%>/service/user/validCode";
            src = src + "?r=" + Math.random();
            $(this).attr("src", src);
            $('#registerForm')
            // Get the bootstrapValidator instance
                    .data('bootstrapValidator')
                    // Mark the field as not validated, so it'll be re-validated when the user change date
                    .updateStatus('valid', 'NOT_VALIDATED', null)
                    // Validate the field
                    .validateField('valid');
        });

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
        });

        $("#registerForm").bootstrapValidator({
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            trigger: "blur",
            fields: {
                loginName: {
                    enabled: true,
                    validators: {
                        notEmpty: {
                            message: "此项为必填项"
                        },
                        remote: {
                            message: '用户名不可用',
                            url: '<%=path%>/service/user/checkLoginName'
                        }
                    }
                },
                password: {
                    enabled: true,
                    validators: {
                        notEmpty: {
                            message: "此项为必填项"
                        },
                        stringLength: {
                            min: 6,
                            message: '密码长度少于6位'
                        }
                    }
                },
                regPwAgain: {
                    enabled: true,
                    selector: '#regPwAgain',
                    validators: {
                        notEmpty: {
                            message: "此项为必填项"
                        },
                        stringLength: {
                            min: 6,
                            message: '密码长度少于6位'
                        },
                        callback: {
                            message: '确认密码不一致',
                            callback: function (value, validator) {
                                return value == $("#regPassword").val();
                            }
                        }
                    }
                },
                valid: {
                    enabled: true,
//                    container: '#validMassage',
                    validators: {
                        notEmpty: {
                            message: "此项为必填项"
                        },
                        remote: {
                            message: '验证码错误',
                            url: '<%=path%>/service/user/checkValid'
                        }
                    }
                }
            }
        }).on('success.form.bv', function (e) {
            e.preventDefault();
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
        });

        $(".nav-tabs li a").click(function () {
            $("#loginForm").data('bootstrapValidator').resetForm(true);
            $("#registerForm").data('bootstrapValidator').resetForm(true);
        });
    });
</script>
</body>
</html>
