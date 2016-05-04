<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.4.13.0013
  Time: 下午 3:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>mhealth</title>
    <%@ include file="/WEB-INF/views/base/head.jsp" %>
</head>
<body class="bgable">
<%@ include file="/WEB-INF/views/base/nav.jsp" %>

<div class="container">
    <div class="starter-template">
        <h1>体征值概览</h1>
        <p class="lead">以下内容为基本体征信息的概览图</p>
    </div>
    <div class="row">
        <div class="col-md-3 col-md-offset-9" style="margin-bottom: 10px;">
            <div id="timeGroup" class="btn-group" role="group">
                <button id="day" type="button" class="btn btn-default active">日</button>
                <button id="week" type="button" class="btn btn-default">周</button>
                <button id="month" type="button" class="btn btn-default">月</button>
                <button id="year" type="button" class="btn btn-default">年</button>
            </div>
        </div>
    </div>
    <div class="row barChartRow">
        <div class="col-md-6">
            <div id="heartRate">
                <p>运动心率（次/秒）</p>
                <a href="<%=path%>/record/recordByDay.ui?key=sport_heartRate">
                    <canvas id="heartRateChart"></canvas>
                </a>
            </div>
        </div>
        <div class="col-md-6">
            <div id="stepCount">
                <p>步数（步）</p>
                <a href="<%=path%>/record/recordByDay.ui?key=stepCount">
                    <canvas id="stepChart"></canvas>
                </a>
            </div>
        </div>
        <div class="col-md-6">
            <div id="distance">
                <p>距离（千米）</p>
                <a href="<%=path%>/record/recordByDay.ui?key=distance">
                    <canvas id="distanceChart"></canvas>
                </a>
            </div>
        </div>
        <div class="col-md-6">
            <div id="elevation">
                <p>海拔（米）</p>
                <a href="<%=path%>/record/recordByDay.ui?key=distance">
                    <canvas id="eleChart"></canvas>
                </a>
            </div>
        </div>
    </div>
</div><!-- /.container -->
<script type="text/javascript">
    $(document).ready(function () {
        var dateExp = "h";
        getOverview("0");

        $("#timeGroup button").click(function () {
            $("#timeGroup").children("button").removeClass("active");
            $(this).addClass("active");
            var atr = $(this).attr("id");
            switch (atr) {
                case "day":
                    getOverview("0");
                    $("#heartRate a").attr("href", "<%=path%>/record/recordByDay.ui?key=sport_heartRate");
                    $("#stepCount a").attr("href", "<%=path%>/record/recordByDay.ui?key=stepCount");
                    $("#distance a").attr("href", "<%=path%>/record/recordByDay.ui?key=distance");
                    $("#elevation a").attr("href", "<%=path%>/record/recordByDay.ui?key=elevation");
                    dateExp = "h";
                    break;
                case "week":
                    getOverview("1");
                    $("#heartRate a").attr("href", "<%=path%>/record/recordByWeek.ui?key=sport_heartRate");
                    $("#stepCount a").attr("href", "<%=path%>/record/recordByWeek.ui?key=stepCount");
                    $("#distance a").attr("href", "<%=path%>/record/recordByWeek.ui?key=distance");
                    $("#elevation a").attr("href", "<%=path%>/record/recordByWeek.ui?key=elevation");
                    dateExp = "d";
                    break;
                case "month":
                    getOverview("2");
                    $("#heartRate a").attr("href", "<%=path%>/record/recordByMonth.ui?key=sport_heartRate");
                    $("#stepCount a").attr("href", "<%=path%>/record/recordByMonth.ui?key=stepCount");
                    $("#distance a").attr("href", "<%=path%>/record/recordByMonth.ui?key=distance");
                    $("#elevation a").attr("href", "<%=path%>/record/recordByMonth.ui?key=elevation");
                    dateExp = "d";
                    break;
                case "year":
                    getOverview("3");
                    $("#heartRate a").attr("href", "<%=path%>/record/recordByYear.ui?key=sport_heartRate");
                    $("#stepCount a").attr("href", "<%=path%>/record/recordByYear.ui?key=stepCount");
                    $("#distance a").attr("href", "<%=path%>/record/recordByYear.ui?key=distance");
                    $("#elevation a").attr("href", "<%=path%>/record/recordByYear.ui?key=elevation");
                    dateExp = "M";
                    break;
            }
        });
//        $("#heartRate, #stepCount, #distance, #elevation").css("background", "rgba(255, 255, 255, 0.9)");
//        $("#heartRate, #stepCount, #distance, #elevation").css("text-align", "center");

        function getOverview(timeCycle) {
            $.ajax({
                url: "<%=path%>/service/sportRecord/getAvgVal",
                data: {userId: "${sessionScope.user.id}", key: "sport_heartRate", beginTime: "", timeUnit: timeCycle},
                dataType: "json",
                type: "post",
                success: function (data) {
                    if (data.resCode == "000000") {
                        getheartValue(data.data.xTime, data.data.result);
                    }
                }
            });
            $.ajax({
                url: "<%=path%>/service/sportRecord/getSumVal",
                data: {userId: "${sessionScope.user.id}", key: "stepCount", beginTime: "", timeUnit: timeCycle},
                dataType: "json",
                type: "post",
                success: function (data) {
                    if (data.resCode == "000000") {
                        getStepValue(data.data.xTime, data.data.result);
                    }
                }
            });
            $.ajax({
                url: "<%=path%>/service/sportRecord/getSumVal",
                data: {userId: "${sessionScope.user.id}", key: "distance", beginTime: "", timeUnit: timeCycle},
                dataType: "json",
                type: "post",
                success: function (data) {
                    if (data.resCode == "000000") {
                        getDistanceValue(data.data.xTime, data.data.result);
                    }
                }
            });
            $.ajax({
                url: "<%=path%>/service/sportRecord/getSumVal",
                data: {userId: "${sessionScope.user.id}", key: "elevation", beginTime: "", timeUnit: timeCycle},
                dataType: "json",
                type: "post",
                success: function (data) {
                    if (data.resCode == "000000") {
                        getEleValue(data.data.xTime, data.data.result);
                    }
                }
            });
        }

        function getheartValue(xVal, yVal) {
            var xt = new Array();
            for (var i in xVal) {
                if (yVal[i] == 0) xt[i] = "";
                else xt[i] = new Date(Number(xVal[i])).format(dateExp);
            }
            $("#heartRateChart").remove();
            $("#heartRate a").append('<canvas id="heartRateChart"></canvas>');
            var heartRateCtx = $("#heartRateChart").get(0).getContext("2d");
            var heartRateChart = new Chart(heartRateCtx);
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
            heartRateChart.Bar(data, barOption);
        }

        function getStepValue(xVal, yVal) {
            var xt = new Array();
            for (var i in xVal) {
                if (yVal[i] == 0) xt[i] = "";
                else xt[i] = new Date(Number(xVal[i])).format(dateExp);
            }
            $("#stepChart").remove();
            $("#stepCount a").append('<canvas id="stepChart"></canvas>');
            var stepCtx = $("#stepChart").get(0).getContext("2d");
            var stepChart = new Chart(stepCtx);
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
            stepChart.Bar(data, barOption);
        }

        function getDistanceValue(xVal, yVal) {
            var xt = new Array();
            for (var i in xVal) {
                if (yVal[i] == 0) xt[i] = "";
                else xt[i] = new Date(Number(xVal[i])).format(dateExp);
            }
            $("#distanceChart").remove();
            $("#distance a").append('<canvas id="distanceChart"></canvas>');
            var distanceCtx = $("#distanceChart").get(0).getContext("2d");
            var distanceChart = new Chart(distanceCtx);
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
            distanceChart.Bar(data, barOption);
        }

        function getEleValue(xVal, yVal) {
            var xt = new Array();
            for (var i in xVal) {
                if (yVal[i] == 0) xt[i] = "";
                else xt[i] = new Date(Number(xVal[i])).format(dateExp);
            }
            $("#eleChart").remove();
            $("#elevation a").append('<canvas id="eleChart"></canvas>');
            var eleCtx = $("#eleChart").get(0).getContext("2d");
            var eleChart = new Chart(eleCtx);
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
            eleChart.Bar(data, barOption);
        }
    });
</script>
</body>
</html>
