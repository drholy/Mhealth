<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.4.17.0017
  Time: 下午 6:35
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
    <div class="row">
        <div class="col-md-4 col-md-offset-4">
            <div class="form-group">
                <label for="day" class="control-label">选择日期：</label>
                <div id="dayCal" class="input-group date form_datetime" data-date="" data-date-format="yyyy-mm-dd"
                     data-link-field="day">
                    <input id="dayVal" class="form-control" size="16" type="text" value="" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
                </div>
                <input type="hidden" id="day" value=""/><br/>
            </div>
        </div>
    </div>
    <div class="row">
        <div id="chartDiv" class="col-md-12" style="text-align: center;">
            <canvas id="dayChart" height="300" width="1000"></canvas>
        </div>
    </div>
    <div class="row"><!--table-->
        <div class="col-md-8 col-md-offset-2">
            <table id="valTable" class="table table-striped table-hover">
                <thead>
                <tr>
                    <th>时间</th>
                    <th>数值</th>
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
        $("#dayCal").datetimepicker({
            language: 'zh-CN',
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,//开始视图：月内
            forceParse: 0,
            showMeridian: 1,
            pickerPosition: "bottom-left",
            minView: 4
        });

        var date = new Date(Number("${day}"));
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var day = date.getDate();
        $("#dayVal").val(year + "-" + month + "-" + day);

        getValue("${key}", "${day}", "0");

        $("#dayCal").datetimepicker().on("changeDate", function (ev) {
            getValue("${key}", ev.date.valueOf(), "0");
        });

        function getValue(key, beginTime, timeUnit) {
            if (beginTime == null || beginTime == "") beginTime = new Date().getTime();
            $.ajax({
                url: "<%=path%>/service/sportRecord/getRecordByTime",
                type: "post",
                data: {userId: "9677167136687", key: key, beginTime: beginTime, timeUnit: timeUnit},
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
            var xt=new Array();
            for(var i in xVal){
                xt[i]=new Date(Number(xVal[i])).getHours();
            }
            $("#dayChart").remove();
            $("#chartDiv").append('<canvas id="dayChart" height="300" width="1000"></canvas>');
            var dayChartCtx = $("#dayChart").get(0).getContext("2d");
            var dayChart = new Chart(dayChartCtx);
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
            dayChart.Bar(data);
        }

        function getTable(xVal, yVal) {
            var xt=new Array();
            for(var i in xVal){
                xt[i]=new Date(Number(xVal[i])).getHours();
            }
            $("#valTable tbody").html("");
            for (var i in xVal) {
                if (yVal[i] == 0) continue;
                var row = "<tr id="+xVal[i]+" style='cursor:pointer'><td>" + xt[i] + "</td><td>" + yVal[i] + "</td></tr>";
                $("#valTable tbody").append(row);
            }
        }

        $("#valTable tbody").on("click", "tr", function () {
            var node = $(this).children("td").get(0);
            window.location.href = "#&time="+$(this).attr("id");
        })
    });
</script>
</body>
</html>