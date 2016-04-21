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
                    <div id="val" class="row">
                        <div class="col-md-4"><p>数据值：</p></div>
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
<script type="text/javascript">
    $(document).ready(function () {
        $.ajax({
            url: "<%=path%>/service/sportRecord/getRecordByBTime",
            type: "post",
            data: {userId: "9677167136687", key: "${key}", beginTime: "${time}"},
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
            var dbDate;
            var month;
            var xt;
            var button = $(event.relatedTarget);
            var recipient = button.data('detail');
            var modal = $(this);
            modal.find("#val .col-md-8 p").text(recipient["${key}"]);

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
    });
</script>
</body>
</html>
