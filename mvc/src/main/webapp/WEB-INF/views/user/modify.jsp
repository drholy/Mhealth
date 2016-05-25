<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.4.27.0027
  Time: 下午 1:45
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
<%@ include file="/WEB-INF/views/base/nav.jsp" %>
<%@ include file="/WEB-INF/views/base/sidebar.jsp" %>

<div class="container">
    <div class="col-md-10 col-md-offset-2 pfcontent">
        <div class="page-header">
            <h1>资料修改</h1>
        </div>
        <form id="modifyForm" class="form-horizontal">
            <div class="form-group">
                <label for="username" class="col-md-4 control-label">昵称*</label>
                <div class="col-md-4">
                    <input name="username" type="text" class="form-control" id="username"
                           placeholder="昵称" required="required">
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-4 control-label">性别*</label>
                <div class="col-md-4">
                    <label class="radio-inline">
                        <input id="female" type="radio" name="sex" value="0" checked="checked"> 女
                    </label>
                    <label class="radio-inline">
                        <input id="male" type="radio" name="sex" value="1"> 男
                    </label>
                </div>
            </div>
            <div class="form-group">
                <label for="birthday" class="col-md-4 control-label">出生日期*</label>
                <div id="dayCal" class="input-group date form_datetime col-md-4" data-date=""
                     data-date-format="yyyy-m-d"
                     data-link-field="birthday">
                    <input id="calV" class="form-control" size="16" type="text" value="" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
                </div>
                <input type="hidden" id="birthday" name="birthday" value=""/><br/>
            </div>
            <div class="form-group">
                <label for="bloodType" class="col-md-4 control-label">血型*</label>
                <div class="col-md-4">
                    <select id="bloodType" name="bloodType" class="form-control">
                        <option>A</option>
                        <option>B</option>
                        <option>O</option>
                        <option>AB</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label for="mobilePhone" class="col-md-4 control-label">手机号</label>
                <div class="col-md-4">
                    <input name="mobilePhone" type="text" class="form-control" id="mobilePhone"
                           placeholder="手机号">
                </div>
            </div>
            <div class="form-group">
                <label for="email" class="col-md-4 control-label">邮箱</label>
                <div class="col-md-4">
                    <input name="email" type="text" class="form-control" id="email"
                           placeholder="邮箱">
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
        $("#dayCal").datetimepicker({
            language: 'zh-CN',
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,//开始视图：月内
            forceParse: 0,
            showMeridian: 1,
            pickerPosition: "bottom-left",
            minView: 2
        }).on("changeDate", function (ev) {
            $("#birthday").val("");
            $("#birthday").val(ev.date.valueOf());
            $('#modifyForm')
            // Get the bootstrapValidator instance
                    .data('bootstrapValidator')
                    // Mark the field as not validated, so it'll be re-validated when the user change date
                    .updateStatus('calV', 'NOT_VALIDATED', null)
                    // Validate the field
                    .validateField('calV');
        });

        $.ajax({
            url: "<%=path%>/service/user/getUserById",
            type: "post",
            data: {id: "${sessionScope.user.id}"},
            dataType: "json",
            success: function (data) {
                if (data.resCode == "000000") {
                    $("#username").val(data.data.user.username);
                    data.data.user.sex == 0 ? $("#female").attr("checked", true) : $("#male").attr("checked", true)
                    $("#calV").val(new Date(Number(data.data.user.birthday)).format("yyyy-M-d"));
                    $("#dayCal").datetimepicker("update");
                    $("#birthday").val(data.data.user.birthday);
                    $("#bloodType").val(data.data.user.bloodType);
                    $("#mobilePhone").val(data.data.user.mobilePhone);
                    $("#email").val(data.data.user.email);
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
                username: {
                    enabled: true,
                    validators: {
                        notEmpty: {
                            message: "此项为必填项"
                        }
                    }
                },
                calV: {
                    enabled: true,
                    selector: "#calV",
                    validators: {
                        notEmpty: {
                            message: "此项为必填项"
                        }
                    }
                },
                mobilePhone: {
                    enabled: true,
                    validators: {
                        regexp: {
                            regexp: /^1(3|4|5|7|8)\d{9}$/,
                            message: '手机号格式错误'
                        }
                    }
                },
                email: {
                    enabled: true,
                    validators: {
                        emailAddress: {
                            message: '邮箱格式错误'
                        }
                    }
                }
            }
        }).on('success.form.bv', function (e) {
            e.preventDefault();
            var user = {};
            user["id"] = "${sessionScope.user.id}";
            user["username"] = $.trim($("#username").val());
            user["sex"] = $('input:radio[name="sex"]:checked').val();
            user["birthday"] = $("#birthday").val();
            user["bloodType"] = $("#bloodType").val();
            user["mobilePhone"] = $("#mobilePhone").val();
            user["email"] = $("#email").val();
            user = JSON.stringify(user);

            $.ajax({
                url: "<%=path%>/service/user/modify",
                type: "post",
                data: {dataJson: user},
                dataType: "json",
                success: function (data) {
                    if (data.resCode == "000000") {
                        location.href = "<%=path%>/user/profile.ui";
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
