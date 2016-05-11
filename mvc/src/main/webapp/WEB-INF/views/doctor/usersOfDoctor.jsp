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
<%@ include file="/WEB-INF/views/base/doctor_nav.jsp" %>

<div class="container">
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <table id="usersTable" class="table table-striped table-hover">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>头像</th>
                    <th>用户名</th>
                    <th>性别</th>
                    <th>出生日期</th>
                    <th>血型</th>
                    <th>查看</th>
                    <th>评论</th>
                </tr>
                </thead>
                <tfoot></tfoot>
                <tbody></tbody>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript">
    $.ajax({
        url: "<%=path%>/service/doctorData/getUsersByDoc",
        type: "post",
        data: {doctorId: "${sessionScope.doctor.id}"},
        dataType: "json",
        success: function (data) {
            if (data.resCode == "000000") {
                showTable(data.data.userList);
            }
        }
    });

    function showTable(list) {
        var fmt = "yyyy年M月d日";
        for (var i in list) {
            var user = list[i];
            var dbDate = new Date(Number(user["birthday"])).format(fmt);
            var row = "<td><img src='#'  alt='头像' /></td>"
                    + "<td>" + user["loginName"] + "</td>"
                    + "<td>" + user["sex"] + "</td>"
                    + "<td>" + dbDate + "</td>"
                    + "<td>" + user["bloodType"] + "</td>"
                    + "<td><button></button></td>"
                    + "<td><button></button></td>";
            $("#usersTable").find("tbody").append(row);
        }
    }
</script>
</body>
</html>