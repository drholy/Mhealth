<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.5.2.0002
  Time: 下午 8:14
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
    <div class="row">
        <div class="col-md-10 col-md-offset-2 pfcontent">
            <div class="page-header">
                <h1>个人资料</h1>
            </div>
            <div>
                <div class="row">
                    <div class="col-md-4"><h4>昵称：</h4></div>
                    <div id="usernameV" class="col-md-8"><p></p></div>
                </div>
                <div class="row">
                    <div class="col-md-4"><h4>性别：</h4></div>
                    <div id="sexV" class="col-md-8"><p></p></div>
                </div>
                <div class="row">
                    <div class="col-md-4"><h4>出生年月：</h4></div>
                    <div id="birthdayV" class="col-md-8"><p></p></div>
                </div>
                <div class="row">
                    <div class="col-md-4"><h4>血型：</h4></div>
                    <div id="bloodTypeV" class="col-md-8"><p></p></div>
                </div>
                <div class="row">
                    <div class="col-md-4"><h4>手机号：</h4></div>
                    <div id="mobilePhoneV" class="col-md-8"><p></p></div>
                </div>
                <div class="row">
                    <div class="col-md-4"><h4>邮箱：</h4></div>
                    <div id="emailV" class="col-md-8"><p></p></div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/base/footer.jsp" %>
<script type="text/javascript">
    $(document).ready(function () {
        $.ajax({
            url: "<%=path%>/service/user/getUserById",
            type: "post",
            data: {id: "${sessionScope.user.id}"},
            dataType: "json",
            success: function (data) {
                if (data.resCode == "000000") {
                    $("#usernameV").find("p").html(data.data.user.username);
                    var sex = data.data.user.sex == "0" ? "女" : "男";
                    $("#sexV").find("p").html(sex);
                    var birthday = new Date(Number(data.data.user.birthday)).format("yyyy年M年d日");
                    $("#birthdayV").find("p").html(birthday);
                    $("#bloodTypeV").find("p").html(data.data.user.bloodType);
                    $("#mobilePhoneV").find("p").html(data.data.user.mobilePhone);
                    $("#emailV").find("p").html(data.data.user.email);
                } else swal({
                    title: "错误",
                    text: data.resCode + ":" + data.resMsg,
                    type: "error",
                    confirmButtonText: "确定"
                });
            }
        });
    });
</script>
</body>
</html>
