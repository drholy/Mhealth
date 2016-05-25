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
<body class="docCont">
<%@ include file="/WEB-INF/views/base/doctor_nav.jsp" %>

<div class="container">
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <table id="usersTable" class="table table-striped table-hover">
                <thead>
                <tr>
                    <th>用户名</th>
                    <th>性别</th>
                    <th>出生日期</th>
                    <th>血型</th>
                    <th>查看</th>
                    <th>点评</th>
                </tr>
                </thead>
                <tfoot></tfoot>
                <tbody></tbody>
            </table>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/base/footer.jsp" %>
<script type="text/javascript">
    $.ajax({
        url: "<%=path%>/service/doctorData/getUsersByDoc",
        type: "post",
        data: {doctorId: "${sessionScope.doctor.id}"},
        dataType: "json",
        success: function (data) {
            if (data.resCode == "000000") {
                showTable(data.data.userList);
            } else swal({
                title: "错误",
                text: data.resCode + ":" + data.resMsg,
                type: "error",
                confirmButtonText: "确定"
            });//alert(data.resCode + ":" + data.resMsg);
        }
    });
    $("#usersTable").on("click", "#check", function () {
        var userId = $(this).attr("data-userId");
        location.href = "<%=path%>/record/overview.ui?id=" + userId;
    });
    $("#usersTable").on("click", "#comment", function () {
        var userId = $(this).attr("data-userId");
        location.href = "<%=path%>/doctor/comment.ui?userId=" + userId;
    });

    function showTable(list) {
        var fmt = "yyyy年M月d日";
        for (var i in list) {
            var user = list[i];
            var dbDate = new Date(Number(user["birthday"])).format(fmt);
            var sex = (user["sex"] == "0") ? "女" : "男";
            var row = "<td>" + user["loginName"] + "</td>"
                    + "<td>" + sex + "</td>"
                    + "<td>" + dbDate + "</td>"
                    + "<td>" + user["bloodType"] + "</td>"
                    + "<td><button id='check' class='btn btn-default' data-userId='" + user["id"] + "'>查看</button></td>"
                    + "<td><button  id='comment' class='btn btn-default' data-userId='" + user["id"] + "'>点评</button></td>";
            $("#usersTable").find("tbody").append(row);
        }
    }
</script>
</body>
</html>
