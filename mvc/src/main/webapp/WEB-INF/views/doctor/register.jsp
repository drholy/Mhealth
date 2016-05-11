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
<body>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">mhealth</a>
            <ul class="nav navbar-nav">
                <li><a>医生注册</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <form id="registerForm" class="form-horizontal" action="" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label for="loginName" class="col-md-4 control-label">登录名*</label>
            <div class="col-md-4">
                <input name="loginName" type="text" class="form-control" id="loginName" placeholder="登录名">
            </div>
        </div>
        <div class="form-group">
            <label for="password" class="col-md-4 control-label">密码*</label>
            <div class="col-md-4">
                <input name="password" type="password" class="form-control" id="password" placeholder="密码">
            </div>
        </div>
        <div class="form-group">
            <label for="againPassword" class="col-md-4 control-label">密码确认*</label>
            <div class="col-md-4">
                <input type="password" class="form-control" id="againPassword" placeholder="密码确认">
            </div>
        </div>
        <div class="form-group">
            <label for="realName" class="col-md-4 control-label">真实姓名*</label>
            <div class="col-md-4">
                <input name="realName" type="text" class="form-control" id="realName"
                       placeholder="真实姓名">
            </div>
        </div>
        <div class="form-group">
            <label for="organization" class="col-md-4 control-label">单位*</label>
            <div class="col-md-4">
                <input name="organization" type="text" class="form-control" id="organization"
                       placeholder="单位">
            </div>
        </div>
        <div class="form-group">
            <label for="office" class="col-md-4 control-label">部门*</label>
            <div class="col-md-4">
                <input name="office" type="text" class="form-control" id="office"
                       placeholder="部门">
            </div>
        </div>
        <div class="form-group">
            <label for="mobilePhone" class="col-md-4 control-label">手机号*</label>
            <div class="col-md-4">
                <input name="mobilePhone" type="text" class="form-control" id="mobilePhone"
                       placeholder="手机号">
            </div>
        </div>
        <div class="form-group">
            <label for="email" class="col-md-4 control-label">邮箱*</label>
            <div class="col-md-4">
                <input name="email" type="text" class="form-control" id="email"
                       placeholder="邮箱">
            </div>
        </div>
        <div class="form-group">
            <label for="certificate" class="col-md-4 control-label">工作证明*</label>
            <div class="col-md-4">
                <input name="certificate" type="file" class="form-control" id="certificate"
                       placeholder="工作证明">
            </div>
        </div>
        <div class="form-group">
            <label for="headImg" class="col-md-4 control-label">头像*</label>
            <div class="col-md-4">
                <input name="headImg" type="file" class="form-control" id="headImg"
                       placeholder="头像">
            </div>
        </div>
        <div class="form-group">
            <label for="valid" class="col-md-4 control-label">验证码*</label>
            <div class="col-md-3">
                <input id="valid" name="valid" type="text" class="form-control" placeholder="验证码">
            </div>
            <label class="col-md-1 control-label"><img id="validPic"
                                                       src="<%=path%>/service/user/validCode"
                                                       style="cursor:hand" alt="验证码"></label>
        </div>
        <div class="form-group">
            <div class="col-md-1 col-md-offset-4">
                <button type="submit" class="btn btn-primary">提交</button>
            </div>
            <div class="col-md-1">
                <button type="button" class="btn btn-default" onclick="location.href='/doctor/login.ui'">取消</button>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    $(document).ready(function () {

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
                            url: '<%=path%>/service/doctorData/checkLoginName'
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
                againPassword: {
                    enabled: true,
                    selector: '#againPassword',
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
                                return value == $("#password").val();
                            }
                        }
                    }
                },
                realName: {
                    enabled: true,
                    validators: {
                        notEmpty: {
                            message: "此项为必填项"
                        }
                    }
                },
                organization: {
                    enabled: true,
                    validators: {
                        notEmpty: {
                            message: "此项为必填项"
                        }
                    }
                },
                office: {
                    enabled: true,
                    validators: {
                        notEmpty: {
                            message: "此项为必填项"
                        }
                    }
                },
                mobilePhone: {
                    enabled: true,
                    validators: {
                        notEmpty: {
                            message: "此项为必填项"
                        },
                        regexp: {
                            regexp: /^1(3|4|5|7|8)\d{9}$/,
                            message: '手机号格式错误'
                        }
                    }
                },
                email: {
                    enabled: true,
                    validators: {
                        notEmpty: {
                            message: "此项为必填项"
                        },
                        emailAddress: {
                            message: '邮箱格式错误'
                        }
                    }
                },
                certificate: {
                    enable: true,
                    trigger: 'keyup',
                    validators: {
                        notEmpty: {
                            message: "此项为必填项"
                        },
                        file: {
                            message: '文件格式有误',
                            extension: 'jpeg,png,jpg',
                            type: 'image/jpeg,image/png',
                            maxSize: 10 * 1024 * 1024,   // 10 MB
                        }
                    }
                },
                headImg: {
                    enable: true,
                    trigger: 'keyup',
                    validators: {
                        notEmpty: {
                            message: "此项为必填项"
                        },
                        file: {
                            message: '文件格式有误',
                            extension: 'jpeg,png,jpg',
                            type: 'image/jpeg,image/png',
                            maxSize: 10 * 1024 * 1024,   // 10 MB
                        }
                    }
                },
                valid: {
                    enabled: true,
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
            var doctor = {};
            doctor["loginName"] = $("#loginName").val();
            doctor["password"] = $("#password").val();
            doctor["realName"] = $("#realName").val();
            doctor["organization"] = $("#organization").val();
            doctor["office"] = $("#office").val();
            doctor["mobilePhone"] = $("#mobilePhone").val();
            doctor["email"] = $("#email").val();
            doctor = JSON.stringify(doctor);

            var formData = new FormData();
            formData.append("docJson", doctor);
            formData.append("certificate", $("#certificate")[0].files[0]);
            formData.append("headImg", $("#headImg")[0].files[0]);
            formData.append("valid", $("#valid").val());

            $.ajax({
                url: "<%=path%>/service/doctorData/insertDoctor",
                type: "post",
                data: formData,
                dataType: "json",
                /**
                 * 必须false才会避开jQuery对 formdata 的默认处理
                 * XMLHttpRequest会对 formdata 进行正确的处理
                 */
                processData: false,
                /**
                 *必须false才会自动加上正确的Content-Type
                 */
                contentType: false,
                success: function (data) {
                    if (data.resCode == "000000") {
                        alert("注册成功！" + data.data.loginName);
                        location.href = "<%=path%>/doctor/login.ui"
                    } else alert(data.resCode + ":" + data.resMsg);
                }
            });
        });
    });
</script>
</body>
</html>
