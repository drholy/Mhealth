<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.4.27.0027
  Time: 下午 1:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>mhealth</title>
    <%@ include file="/WEB-INF/views/base/head.jsp" %>
</head>
<body>
<nav class="navbar navbar-inverse">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">mhealth</a>
            <ul class="nav navbar-nav">
                <li><a>账户激活</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <form id="activeForm" class="form-horizontal">
        <div class="form-group">
            <label for="loginName" class="col-md-4 control-label">登录名*</label>
            <div class="col-md-4">
                <input name="loginName" type="text" class="form-control" id="loginName"
                       value="${sessionScope.loginName}" readonly="readonly">
            </div>
        </div>
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
                    <input type="radio" name="sex" value="0" checked="checked"> 女
                </label>
                <label class="radio-inline">
                    <input type="radio" name="sex" value="1"> 男
                </label>
            </div>
        </div>
        <div class="form-group">
            <label for="birthday" class="col-md-4 control-label">出生日期*</label>
            <div id="dayCal" class="input-group date form_datetime col-md-4" data-date="" data-date-format="yyyy-m-d"
                 data-link-field="birthday">
                <input class="form-control" size="16" type="text" value="" readonly>
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
        });

        $("#activeForm").submit(function () {
            var user = {};
            user["id"] = "${sessionScope.userId}";
            user["loginName"] = "${sessionScope.loginName}";
            user["username"]=$("#username").val();
            user["sex"] = $('input:radio[name="sex"]:checked').val();
            user["birthday"] = $("#birthday").val();
            user["bloodType"] = $("#bloodType").val();
            user["mobilePhone"] = $("#mobilePhone").val();
            user["email"] = $("#email").val();
            user = JSON.stringify(user);

            $.ajax({
                url: "<%=path%>/service/user/active",
                type: "post",
                data: {dataJson: user},
                dataType: "json",
                success: function (data) {
                    if (data.resCode == "000000") {
                        location.href = "<%=path%>/record/overview.ui";
                    } else alert(data.resCode + ":" + data.resMsg);
                }
            });
            return false;
        });
    });
</script>
</body>
</html>
