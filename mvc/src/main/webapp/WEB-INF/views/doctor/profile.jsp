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
                <h1>个人资料</h1>
            </div>
            <div class="row">
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
                </div>
            </div>
        </div>
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
