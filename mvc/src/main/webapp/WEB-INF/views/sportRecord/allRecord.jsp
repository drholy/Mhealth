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
    <%@ include file="/WEB-INF/views/base/head.jsp" %>
</head>
<body>
<%@ include file="/WEB-INF/views/base/nav.jsp" %>
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
                    <div class="row">
                        <div id="shrK" class="col-md-4"><p>运动心率：</p></div>
                        <div id="shrV" class="col-md-8"><p></p></div>
                    </div>
                    <div class="row">
                        <div id="scK" class="col-md-4"><p>步数：</p></div>
                        <div id="scV" class="col-md-8"><p></p></div>
                    </div>
                    <div class="row">
                        <div id="disK" class="col-md-4"><p>距离：</p></div>
                        <div id="disV" class="col-md-8"><p></p></div>
                    </div>
                    <div class="row">
                        <div id="eleK" class="col-md-4"><p>海拔：</p></div>
                        <div id="eleV" class="col-md-8"><p></p></div>
                    </div>
                    <div class="row">
                        <div id="btimeK" class="col-md-4"><p>开始时间：</p></div>
                        <div id="btimeV" class="col-md-8"><p></p></div>
                    </div>
                    <div class="row">
                        <div id="etimeK" class="col-md-4"><p>结束时间：</p></div>
                        <div id="etimeV" class="col-md-8"><p></p></div>
                    </div>
                    <div class="row">
                        <div id="utimeK" class="col-md-4"><p>上传时间：</p></div>
                        <div id="utimeV" class="col-md-8"><p></p></div>
                    </div>
                    <div class="row">
                        <div class="col-md-12"><h4>设备信息：</h4></div>
                    </div>
                    <div class="row">
                        <div id="dnameK" class="col-md-4"><p>名称：</p></div>
                        <div id="dnameV" class="col-md-8"><p></p></div>
                    </div>
                    <div class="row">
                        <div id="dbrandK" class="col-md-4"><p>品牌：</p></div>
                        <div id="dbrandV" class="col-md-8"><p></p></div>
                    </div>
                    <div class="row">
                        <div id="dmodelK" class="col-md-4"><p>型号：</p></div>
                        <div id="dmodelV" class="col-md-8"><p></p></div>
                    </div>
                    <div class="row">
                        <div id="dosK" class="col-md-4"><p>系统：</p></div>
                        <div id="dosV" class="col-md-8"><p></p></div>
                    </div>
                    <div class="row">
                        <div id="dtypeK" class="col-md-4"><p>类型：</p></div>
                        <div id="dtypeV" class="col-md-8"><p></p></div>
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
            var fmt = "yyyy年M月d日 h时m分s秒";
            var button = $(event.relatedTarget);
            var recipient = button.data('result');
            var modal = $(this);

            modal.find("#shrV").text(recipient["sport_heartRate"]);
            modal.find("#scV").text(recipient["stepCount"]);
            modal.find("#disV").text(recipient["distance"]);
            modal.find("#eleV").text(recipient["elevation"]);

            dbDate = new Date(Number(recipient["beginTime"])).format(fmt);
            modal.find("#btimeV").text(dbDate);

            dbDate = new Date(Number(recipient["endTime"])).format(fmt);
            modal.find("#etimeV").text(dbDate);

            dbDate = new Date(Number(recipient["uploadTime"])).format(fmt);
            modal.find("#utimeV").text(dbDate);

            $.ajax({
                url: "<%=path%>/service/deviceData/getDevice",
                type: "post",
                data: {id: recipient["deviceId"]},
                dataType: "json",
                success: function (data) {
                    if (data.resCode == "000000") {
                        modal.find("#dnameV").text(data.data.name);
                        modal.find("#dbrandV").text(data.data.brand);
                        modal.find("#dmodelV").text(data.data.model);
                        modal.find("#dosV").text(data.data.os);
                        modal.find("#dtypeV").text(data.data.type);
                    } else alert(data.resCode + ":" + data.resMsg);
                }
            });
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
            var fmt = "yyyy年M月d日 h时m分s秒";
            var month;
            var xt;
            for (var i in results) {
                var result = results[i];
                dbDate = new Date(Number(result["beginTime"])).format(fmt);
                var row = "<tr data-result='" + JSON.stringify(result)
                        + "' data-toggle='modal' data-target='#detailModal' style='cursor:pointer'>"
                        + "<td>" + result["sport_heartRate"] + "</td>"
                        + "<td>" + result["stepCount"] + "</td>"
                        + "<td>" + result["distance"] + "</td>"
                        + "<td>" + result["elevation"] + "</td>"
                        + "<td>" + dbDate + "</td>"
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
