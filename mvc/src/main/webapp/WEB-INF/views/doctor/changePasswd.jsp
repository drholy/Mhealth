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
    <title>mhealth</title>
    <%@ include file="/WEB-INF/views/base/head.jsp" %>
</head>
<body class="pfbg">
<%@ include file="/WEB-INF/views/base/doctor_nav.jsp" %>
<%@ include file="/WEB-INF/views/base/doctor_sidebar.jsp" %>

<div class="container">
    <div class="row">
        <div class="col-md-10 col-md-offset-2 pfcontent">
            <div class="page-header">
                <h1>密码修改</h1>
            </div>
            <form id="changepwForm" class="form-horizontal">
                <div class="form-group">
                    <label for="oldPassword" class="col-md-4 control-label">旧密码*</label>
                    <div class="col-md-4">
                        <input name="oldPassword" type="password" class="form-control" id="oldPassword"
                               placeholder="旧密码" required="required">
                    </div>
                </div>
                <div class="form-group">
                    <label for="newPassword" class="col-md-4 control-label">新密码*</label>
                    <div class="col-md-4">
                        <input name="newPassword" type="password" class="form-control" id="newPassword"
                               placeholder="新密码" required="required">
                    </div>
                </div>
                <div class="form-group">
                    <label for="againPassword" class="col-md-4 control-label">密码确认*</label>
                    <div class="col-md-4">
                        <input name="againPassword" type="password" class="form-control" id="againPassword"
                               placeholder="密码确认" required="required">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-md-1 col-md-offset-4">
                        <button type="submit" class="btn btn-primary">提交</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/base/footer.jsp" %>
<script type="text/javascript">
    $(document).ready(function () {
        $("#changepwForm").bootstrapValidator({
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            trigger: "blur",
            fields: {
                oldPassword: {
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
                newPassword: {
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
                                return value == $("#newPassword").val();
                            }
                        }
                    }
                }
            }
        }).on('success.form.bv', function (e) {
            e.preventDefault();
            var pw = {};
            pw["id"] = "${sessionScope.doctor.id}";
            pw["oldPassword"] = $("#oldPassword").val();
            pw["newPassword"] = $("#newPassword").val();
            pw["againPassword"] = $("#againPassword").val();
            pw = JSON.stringify(pw);
            $.ajax({
                url: "<%=path%>/service/doctorData/changePasswd",
                type: "post",
                data: {passwdJson: pw},
                dataType: "json",
                success: function (data) {
                    if (data.resCode == "000000") {
                        swal({
                            title: "成功",
                            text: "修改成功！",
                            type: "success",
                            confirmButtonText: "确定"
                        },function () {
                            location.reload();
                        });
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
