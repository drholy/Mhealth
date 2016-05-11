<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.4.17.0017
  Time: 下午 6:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>mhealth</title>
    <%@ include file="/WEB-INF/views/base/head.jsp" %>
</head>
<body class="bgable">
<c:if test="${sessionScope.user!=null}">
    <%@ include file="/WEB-INF/views/base/nav.jsp" %>
</c:if>
<c:if test="${sessionScope.doctor!=null}">
    <%@ include file="/WEB-INF/views/base/doctor_sportRecord_nav.jsp" %>
</c:if>
<div class="container">
    <ol class="breadcrumb">
        <li><a href="<%=path%>/record/overview.ui">首页</a></li>
        <li class="active"><a href="<%=path%>/record/recordByYear.ui?key=${key}&time=${time}">年</a></li>
    </ol>
    <div class="starter-template">
        <h1></h1>
    </div>
    <div class="row">
        <div class="col-md-4 col-md-offset-4">
            <div class="form-group">
                <label for="year" class="control-label">选择日期：</label>
                <div id="yearCal" class="input-group date form_datetime" data-date="" data-date-format="yyyy"
                     data-link-field="year">
                    <input id="yearVal" class="form-control" size="16" type="text" value="" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
                </div>
                <input type="hidden" id="year" value=""/><br/>
            </div>
        </div>
    </div>
    <div class="row">
        <div id="chartDiv" class="col-md-12" style="text-align: center;">
            <canvas id="yearChart"></canvas>
        </div>
    </div>
    <div class="row"><!--table-->
        <div class="col-md-8 col-md-offset-2">
            <table id="valTable" class="table table-striped table-hover">
                <thead>
                <tr>
                    <th>时间（月）</th>
                    <th class="thUnit">数值</th>
                </tr>
                </thead>
                <tfoot></tfoot>
                <tbody></tbody>
            </table>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        var USERID = ("${sessionScope.user.id}" == "") ? "${id}" : "${sessionScope.user.id}";
        $(".breadcrumb").html("");
        var bread = '<li><a href="<%=path%>/record/overview.ui?id=' + USERID + '&key=${key}">首页</a></li>' +
                '<li class="active"><a href="<%=path%>/record/recordByYear.ui?id=' + USERID + '&key=${key}&time=${time}">年</a></li>';
        $(".breadcrumb").append(bread);
        showTitle("${key}");

        $("#yearCal").datetimepicker({
            language: 'zh-CN',
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 4,//开始视图：十年内
            forceParse: 0,
            showMeridian: 1,
            pickerPosition: "bottom-left",
            minView: 4,
            endDate: new Date(),
            initialDate: new Date(Number("${time}"))
        });

        var date = new Date(Number("${time}"));
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var day = date.getDate();
        $("#yearVal").val(year);

        getValue("${key}", "${time}", "3");

        $("#yearCal").datetimepicker().on("changeYear", function (ev) {
            $(".breadcrumb").html("");
            var bread = '<li><a href="<%=path%>/record/overview.ui?id=' + USERID + '">首页</a></li>' +
                    '<li class="active"><a href="<%=path%>/record/recordByYear.ui?id=' + USERID + '&key=${key}&time=' + ev.date.valueOf() + '">年</a></li>';
            $(".breadcrumb").append(bread);
            getValue("${key}", ev.date.valueOf(), "3");
        });

        function getValue(key, beginTime, timeUnit) {
            var url;
            if (key == "sport_heartRate") url = "<%=path%>/service/sportRecord/getAvgVal";
            else url = "<%=path%>/service/sportRecord/getSumVal";
            if (beginTime == null || beginTime == "") beginTime = new Date().getTime();
            $.ajax({
                <%--url: "<%=path%>/service/sportRecord/getRecordByTime",--%>
                url: url,
                type: "post",
                data: {userId: USERID, key: key, beginTime: beginTime, timeUnit: timeUnit},
                dataType: "json",
                success: function (data) {
                    if (data.resCode == "000000") {
                        getDayChart(data.data.xTime, data.data.result);
                        getTable(data.data.xTime, data.data.result);
                    }
                }
            });
        }

        function getDayChart(xVal, yVal) {
            var xt = new Array();
            for (var i in xVal) {
                if (yVal[i] == 0) xt[i] = "";
                else xt[i] = new Date(Number(xVal[i])).getMonth() + 1;
            }
            $("#yearChart").remove();
            $("#chartDiv").append('<canvas id="yearChart" height="300" width="750"></canvas>');
            var yearChartCtx = $("#yearChart").get(0).getContext("2d");
            var yearChart = new Chart(yearChartCtx);
            var data = {
                labels: xt,
                datasets: [
                    {
                        fillColor: "rgba(220,220,220,0.5)",
                        strokeColor: "rgba(220,220,220,1)",
                        data: yVal
                    }
                ]
            };
            yearChart.Bar(data, barOption);
        }

        function getTable(xVal, yVal) {
            $(".thUnit").html("数值（" + getUnit("${key}") + "）");
            $("#valTable tbody").html("");
            for (var i in xVal) {
                if (yVal[i] == 0) continue;
                var xt = new Date(Number(xVal[i])).getMonth() + 1 + "月";
                var row = "<tr id=" + xVal[i] + " style='cursor:pointer'><td>" + xt + "</td><td>" + yVal[i] + "</td></tr>";
                $("#valTable tbody").append(row);
            }
        }

        $("#valTable tbody").on("click", "tr", function () {
            var node = $(this).children("td").get(0);
            window.location.href = "<%=path%>/record/recordByMonth.ui?id=" + USERID + "&key=${key}&time=" + $(this).attr("id");
        });
    });
</script>
</body>
</html>
