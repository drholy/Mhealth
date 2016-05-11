<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.5.11.0011
  Time: 下午 8:35
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
                <h1>我的医生</h1>
            </div>
            <div class="row" id="docContent">
                <div class="col-md-8">
                    <div class="row">
                        <div class="col-md-4"><h4>姓名：</h4></div>
                        <div id="realNameV" class="col-md-4"><p></p></div>
                    </div>
                    <div class="row">
                        <div class="col-md-4"><h4>组织：</h4></div>
                        <div id="organizationV" class="col-md-4"><p></p></div>
                    </div>
                    <div class="row">
                        <div class="col-md-4"><h4>部门：</h4></div>
                        <div id="officeV" class="col-md-4"><p></p></div>
                    </div>
                    <div class="row">
                        <div class="col-md-4"><h4>手机号：</h4></div>
                        <div id="mobilePhoneV" class="col-md-4"><p></p></div>
                    </div>
                    <div class="row">
                        <div class="col-md-4"><h4>邮箱：</h4></div>
                        <div id="emailV" class="col-md-4"><p></p></div>
                    </div>
                    <div class="row">
                        <div class="col-md-4"><h4>加入时间：</h4></div>
                        <div id="regTimeV" class="col-md-4"><p></p></div>
                    </div>
                    <div class="row">
                        <div class="col-md-4"><h4>证书证明：</h4></div>
                    </div>
                    <div class="row">
                        <div id="certificateV" class="col-md-4"><p></p></div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="row">
                        <div id="headImgV" class="col-md-12"><p></p></div>
                    </div>
                    <div class="row">
                        <div id="cancel" class="col-md-6">
                            <button type="button" class="btn btn-danger btn-lg btn-block">取消医生</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $.ajax({
            url: "<%=path%>/service/user/getDocByUser",
            type: "post",
            data: {userId: "${sessionScope.user.id}"},
            dataType: "json",
            success: function (data) {
                if (data.resCode == "000000") {
                    $("#realNameV").find("p").html(data.data.doctor.realName);
                    $("#organizationV").find("p").html(data.data.doctor.organization);
                    $("#officeV").find("p").html(data.data.doctor.office);
                    $("#mobilePhoneV").find("p").html(data.data.doctor.mobilePhone);
                    $("#emailV").find("p").html(data.data.doctor.email);
                    var regTime = new Date(Number(data.data.doctor.regTime)).format("yyyy年M年d日");
                    $("#regTimeV").find("p").html(regTime);
                    $("#certificateV").find("p").html("<img src='<%=path%>/service/pic/getUserImg/"
                            + data.data.doctor.certificate + "' alt='证书' width='600' height='400'/>");
                    $("#headImgV").find("p").html("<img src='<%=path%>/service/pic/getUserImg/"
                            + data.data.doctor.headImg + "' width='130' height='180' alt='头像'>");
                    $("#cancel").data("docId", data.data.doctor.id);
                } else {
                    $("#docContent").html("");
                    $("#docContent").html("<div class='col-md-12'><h3 style='color:white'>您未选择医生</h3></div>");
                }
            }
        });
        $("#cancel").click(function () {
            var doctorId = $(this).data("docId");
            $.ajax({
                url: "<%=path%>/service/user/cancelDoctor",
                type: "post",
                data: {userId: "${sessionScope.user.id}", doctorId: doctorId},
                dataType: "json",
                success: function (data) {
                    if (data.resCode == "000000") {
                        alert("取消成功！");
                        location.reload();
                    } else alert(data.resCode + ":" + data.resMsg);
                }
            });
        })
    });
</script>
</body>
</html>
