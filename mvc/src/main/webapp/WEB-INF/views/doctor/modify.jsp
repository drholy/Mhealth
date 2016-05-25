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
    <div class="col-md-10 col-md-offset-2 pfcontent">
        <div class="page-header">
            <h1>资料修改</h1>
        </div>
        <form id="modifyForm" class="form-horizontal">
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
                <div class="col-md-1 col-md-offset-4">
                    <button type="submit" class="btn btn-primary">提交</button>
                </div>
            </div>
        </form>
    </div>
</div>
<%@ include file="/WEB-INF/views/base/footer.jsp" %>
<script type="text/javascript">
    $(document).ready(function () {
        $.ajax({
            url: "<%=path%>/service/doctorData/getDocById",
            type: "post",
            data: {doctorId: "${sessionScope.doctor.id}"},
            dataType: "json",
            success: function (data) {
                if (data.resCode == "000000") {
                    $("#realName").val(data.data.doctor.realName);
                    $("#organization").val(data.data.doctor.organization);
                    $("#office").val(data.data.doctor.office);
                    $("#mobilePhone").val(data.data.doctor.mobilePhone);
                    $("#email").val(data.data.doctor.email);
                } else swal({
                    title: "错误",
                    text: data.resCode + ":" + data.resMsg,
                    type: "error",
                    confirmButtonText: "确定"
                });
            }
        });

        $("#modifyForm").bootstrapValidator({
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            trigger: "blur",
            fields: {
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
            }
        }).on('success.form.bv', function (e) {
            e.preventDefault();
            var doctor = {};
            doctor["id"] = "${sessionScope.doctor.id}";
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

            $.ajax({
                url: "<%=path%>/service/doctorData/modify",
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
                        swal({
                            title: "成功",
                            text: "修改成功！新资料将由管理员审核！",
                            type: "success",
                            confirmButtonText: "确定"
                        },function () {
                            location.href = "<%=path%>/doctor/login.ui"
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
