<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.4.19.0019
  Time: 下午 11:04
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
        <li><a href="<%=path%>/record/recordByYear.ui?key=${key}&time=${time}">年</a></li>
        <li><a href="<%=path%>/record/recordByMonth.ui?key=${key}&time=${time}">月</a></li>
        <li><a href="<%=path%>/record/recordByDay.ui?key=${key}&time=${time}">日</a></li>
        <li><a href="<%=path%>/record/recordByTime.ui?key=${key}&time=${time}">详情</a></li>
    </ol>
    <div class="starter-template">
        <h1></h1>
    </div>
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <table id="valTable" class="table table-striped table-hover">
                <thead>
                <tr>
                    <th>时间（时:分）</th>
                    <th>数值</th>
                </tr>
                </thead>
                <tfoot></tfoot>
                <tbody></tbody>
            </table>
        </div>
    </div>
</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">详细信息</h4>
            </div>
            <div class="modal-body">
                <div class="container">
                    <div class="row">
                        <div id="valK" class="col-md-4"><p>数据值：</p></div>
                        <div id="valV" class="col-md-8"><p></p></div>
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
<script type="text/javascript">
    $(document).ready(function () {
        showTitle("${key}");

        $.ajax({
            url: "<%=path%>/service/sportRecord/getRecordByBTime",
            type: "post",
            data: {userId: "${sessionScope.user.id}", key: "${key}", beginTime: "${time}"},
            dataType: "json",
            success: function (data) {
                if (data.resCode == "000000") {
                    getTable(data.data.result);
                }
            }
        })

        function getTable(details) {
            for (var i in details) {
                var detail = details[i];
                var dbDate = new Date(Number(detail["beginTime"]));
                var xt = dbDate.getHours() + ":" + dbDate.getMinutes();
                var row = "<tr data-toggle='modal' data-target='#myModal' data-detail='" + JSON.stringify(detail) + "' style='cursor:pointer'><td>" + xt + "</td><td>" + detail["${key}"] + "</td></tr>";
                $("#valTable").append(row);
            }
        }

        $('#myModal').on('show.bs.modal', function (event) {

            var fmt = "yyyy年M月d日 h时m分s秒";
            var dbDate;
            var button = $(event.relatedTarget);
            var recipient = button.data('detail');
            var modal = $(this);
            modal.find("#valV").text(recipient["${key}"]);

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
    });
</script>
</body>
</html>
