<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.5.3.0003
  Time: 下午 3:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>mhealth</title>
    <%@ include file="/WEB-INF/views/base/head.jsp" %>
</head>
<body class="pfbg">
<%@ include file="/WEB-INF/views/base/nav.jsp" %>

<div class="container">
    <div class="row">
        <%@ include file="/WEB-INF/views/base/sidebar.jsp" %>
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
<script type="text/javascript">
    $(document).ready(function () {
        $("#changepwForm").submit(function () {
            var pw = {};
            pw["id"] = "${sessionScope.user.id}";
            pw["oldPassword"] = $("#oldPassword").val();
            pw["newPassword"] = $("#newPassword").val();
            pw["againPassword"] = $("#againPassword").val();
            pw = JSON.stringify(pw);
            $.ajax({
                url: "<%=path%>/service/user/changePasswd",
                type: "post",
                data: {passwdJson: pw},
                dataType: "json",
                success: function (data) {
                    if (data.resCode == "000000") {
                        alert("修改成功！");
                        $("#oldPassword").val("");
                        $("#newPassword").val("");
                        $("#againPassword").val("");
                    } else alert(data.resCode + ":" + data.resMsg);
                }
            });
            return false;
        });
    });
</script>
</body>
</html>
