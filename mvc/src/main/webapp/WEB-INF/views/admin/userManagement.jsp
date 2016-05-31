<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.5.31.0031
  Time: 下午 9:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>mhealth_管理端</title>
    <%@ include file="/WEB-INF/views/base/head.jsp" %>
</head>
<body class="examDoc">
<%@ include file="/WEB-INF/views/base/admin_nav.jsp" %>
<div class="container">
    <div class="starter-template">
        <h1>用户管理</h1>
    </div>
    <div class="row">
        <div class="col-md-2 col-md-offset-8" style="margin-bottom: 10px">
            <input type="text" class="form-control" id="detail" placeholder="用户名">
        </div>
    </div>
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
                </tr>
                </thead>
                <tfoot></tfoot>
                <tbody></tbody>
            </table>
        </div>
    </div>
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <ul id="pager"></ul>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/base/footer.jsp" %>
<script type="text/javascript">
    $(document).ready(function () {
        var PAGESIZE = 20;
        getData("1", PAGESIZE, "");
        $("#usersTable").on("click", "#check", function () {
            var userId = $(this).attr("data-userId");
            location.href = "<%=path%>/record/overview.ui?id=" + userId;
        });
        $("#detail").keyup(function () {
            $('#pager').twbsPagination('destroy');
            getData("1", PAGESIZE, $(this).val());
        });

        function getData(currPage, pageSize, detail) {
            $.ajax({
                url: "<%=path%>/service/adminData/getAllUsers",
                type: "post",
                data: {currPage: currPage, pageSize: pageSize, detail: detail},
                dataType: "json",
                success: function (data) {
                    if (data.resCode == "000000") {
                        showData(data.data.rows);
                        showPager(data.data);
                    } else swal({
                        title: "错误",
                        text: data.resCode + ":" + data.resMsg,
                        type: "error",
                        confirmButtonText: "确定"
                    });
                }
            });
        }

        function showData(list) {
            $("#usersTable").find("tbody").html("");
            var fmt = "yyyy年M月d日";
            for (var i in list) {
                var user = list[i];
                var dbDate = new Date(Number(user["birthday"])).format(fmt);
                var sex = (user["sex"] == "0") ? "女" : "男";
                var row = "<tr>"
                        + "<td>" + user["loginName"] + "</td>"
                        + "<td>" + sex + "</td>"
                        + "<td>" + dbDate + "</td>"
                        + "<td>" + user["bloodType"] + "</td>"
                        + "<td><button id='check' class='btn btn-default' data-userId='" + user["id"] + "'>查看</button></td>"
                        + "</tr>";
                $("#usersTable").find("tbody").append(row);
            }
        }

        function showPager(data) {
            $('#pager').twbsPagination({
                totalPages: Number(data.totalPages),
                startPage: Number(data.currPage),
                visiblePages: 5,
                first: '1页',
                prev: '&laquo;',
                next: '&raquo;',
                last: data.totalPages + '页',
                onPageClick: function (event, page) {
                    getData(page, PAGESIZE, $("#detail").val());
                }
            });
        }
    });
</script>
</body>
</html>
