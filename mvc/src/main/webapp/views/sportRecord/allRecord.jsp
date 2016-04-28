<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.4.20.0020
  Time: 下午 3:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>mhealth</title>
    <%@ include file="/views/base/head.jsp" %>
</head>
<body>
<%@ include file="/views/base/nav.jsp" %>
<div class="container">
    <ol class="breadcrumb">
        <li><a href="<%=path%>/record/overview.ui">首页</a></li>
        <li class="active"><a href="<%=path%>/record/allRecords.ui">所有数据</a></li>
    </ol>
    <div class="starter-template">
        <h1>所有数据</h1>
    </div>
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <table id="allValTable" class="table table-striped table-hover">
                <thead>
                <tr>
                    <th>运动心率</th>
                    <th>步数</th>
                    <th>距离</th>
                    <th>海拔</th>
                    <th>时间</th>
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
<div class="modal fade" id="detailModal" tabindex="-1" role="dialog" aria-labelledby="detailModalTitle">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="detailModalTitle">详细信息</h4>
            </div>
            <div class="modal-body">
                <div class="container">
                    <div id="shr" class="row">
                        <div class="col-md-4"><p>运动心率：</p></div>
                        <div class="col-md-8"><p></p></div>
                    </div>
                    <div id="sc" class="row">
                        <div class="col-md-4"><p>步数：</p></div>
                        <div class="col-md-8"><p></p></div>
                    </div>
                    <div id="dis" class="row">
                        <div class="col-md-4"><p>距离：</p></div>
                        <div class="col-md-8"><p></p></div>
                    </div>
                    <div id="ele" class="row">
                        <div class="col-md-4"><p>海拔：</p></div>
                        <div class="col-md-8"><p></p></div>
                    </div>
                    <div id="btime" class="row">
                        <div class="col-md-4"><p>开始时间：</p></div>
                        <div class="col-md-8"><p></p></div>
                    </div>
                    <div id="etime" class="row">
                        <div class="col-md-4"><p>结束时间：</p></div>
                        <div class="col-md-8"><p></p></div>
                    </div>
                    <div id="utime" class="row">
                        <div class="col-md-4"><p>上传时间：</p></div>
                        <div class="col-md-8"><p></p></div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
        var PAGESIZE = 20;
        getData("${sessionScope.user.id}", 1, PAGESIZE);
        $('#detailModal').on('show.bs.modal', function (event) {
            var dbDate;
            var month;
            var xt;
            var button = $(event.relatedTarget);
            var recipient = button.data('result');
            var modal = $(this);

            modal.find("#shr .col-md-8 p").text(recipient["sport_heartRate"]);
            modal.find("#sc .col-md-8 p").text(recipient["stepCount"]);
            modal.find("#dis .col-md-8 p").text(recipient["distance"]);
            modal.find("#ele .col-md-8 p").text(recipient["elevation"]);

            dbDate = new Date(Number(recipient["beginTime"]));
            month = dbDate.getMonth() + 1;
            xt = dbDate.getFullYear() + "年" + month + "月" + dbDate.getDate() + "日 "
                    + dbDate.getHours() + "时" + dbDate.getMinutes() + "分" + dbDate.getSeconds() + "秒";
            modal.find("#btime .col-md-8 p").text(xt);

            dbDate = new Date(Number(recipient["endTime"]));
            month = dbDate.getMonth() + 1;
            xt = dbDate.getFullYear() + "年" + month + "月" + dbDate.getDate() + "日 "
                    + dbDate.getHours() + "时" + dbDate.getMinutes() + "分" + dbDate.getSeconds() + "秒";
            modal.find("#etime .col-md-8 p").text(xt);

            dbDate = new Date(Number(recipient["uploadTime"]));
            month = dbDate.getMonth() + 1;
            xt = dbDate.getFullYear() + "年" + month + "月" + dbDate.getDate() + "日 "
                    + dbDate.getHours() + "时" + dbDate.getMinutes() + "分" + dbDate.getSeconds() + "秒";
            modal.find("#utime .col-md-8 p").text(xt);
        });

        function getData(userId, currPage, pageSize) {
            $.ajax({
                url: "<%=path%>/service/sportRecord/allRecords",
                type: "post",
                data: {userId: userId, currPage: currPage, pageSize: pageSize},
                dataType: "json",
                success: function (data) {
                    if (data.resCode = "000000") {
                        getTable(data.data.rows);
                        showPager(data.data);
                    }
                }
            });
        }

        function getTable(results) {
            $("#allValTable tbody").html("");
            var dbDate;
            var month;
            var xt;
            for (var i in results) {
                var result = results[i];
                dbDate = new Date(Number(result["beginTime"]));
                month = dbDate.getMonth() + 1;
                xt = dbDate.getFullYear() + "年" + month + "月" + dbDate.getDate() + "日 "
                        + dbDate.getHours() + "时" + dbDate.getMinutes() + "分" + dbDate.getSeconds() + "秒";
                var row = "<tr data-result='" + JSON.stringify(result)
                        + "' data-toggle='modal' data-target='#detailModal' style='cursor:pointer'>"
                        + "<td>" + result["sport_heartRate"] + "</td>"
                        + "<td>" + result["stepCount"] + "</td>"
                        + "<td>" + result["distance"] + "</td>"
                        + "<td>" + result["elevation"] + "</td>"
                        + "<td>" + xt + "</td>"
                        + "</tr>";
                $("#allValTable tbody").append(row);
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
                    getData("${sessionScope.user.id}", page, PAGESIZE);
                }
            });
        }
    });
</script>
</body>
</html>
