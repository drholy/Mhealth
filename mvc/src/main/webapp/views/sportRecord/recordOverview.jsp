<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.4.13.0013
  Time: 下午 3:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%String path = request.getContextPath();%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>mhealth</title>
    <link href="<%=path%>/css/bootstrap.css" rel="stylesheet">
    <link href="<%=path%>/css/bootstrap-theme.css" rel="stylesheet">
    <script src="<%=path%>/js/jquery-1.11.3.min.js"></script>
    <script src="<%=path%>/js/bootstrap.js"></script>
    <script src="<%=path%>/js/Chart.min.js"></script>
    <style type="text/css">
        body {
            padding-top: 70px;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">mhealth</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="#">首页</a></li>
                <li><a href="#about">所有数据</a></li>
            </ul>
            <p class="navbar-text navbar-right">欢迎，xx用户</p>
        </div><!--/.nav-collapse -->
    </div>
</nav>

<div class="container">

    <div class="starter-template">
        <h1>体征值概览</h1>
        <p class="lead">以下内容为基本体征信息的概览图</p>
    </div>
    <div class="row">
        <div class="col-md-3 col-md-offset-9" style="margin-bottom: 10px;">
            <div id="timeGroup" class="btn-group" role="group">
                <button id="day" type="button" class="btn btn-default active">24时</button>
                <button id="week" type="button" class="btn btn-default">7天</button>
                <button id="month" type="button" class="btn btn-default">31天</button>
                <button id="year" type="button" class="btn btn-default">12月</button>
            </div>
        </div>
    </div>
    <div class="row">
        <div id="heartRate" class="col-md-6">
            <p>运动心率</p>
            <a href="#">
                <canvas id="heartRateChart"></canvas>
            </a>
        </div>
        <div id="stepCount" class="col-md-6">
            <p>步数</p>
            <a>
                <canvas id="stepChart"></canvas>
            </a>
        </div>
        <div id="distance" class="col-md-6">
            <p>距离</p>
            <a>
                <canvas id="distanceChart"></canvas>
            </a>
        </div>
        <div id="elevation" class="col-md-6">
            <p>海拔</p>
            <a>
                <canvas id="eleChart"></canvas>
            </a>
        </div>
    </div>
</div><!-- /.container -->
<script type="text/javascript">
    $(document).ready(function () {
        getOverview("0");

        $("#timeGroup button").click(function(){
            $("#timeGroup").children("button").removeClass("active");
            $(this).addClass("active");
            var atr=$(this).attr("id");
            switch (atr){
                case "day":
                    getOverview("0");
                    break;
                case "week":
                    getOverview("1");
                    break;
                case "month":
                    getOverview("2");
                    break;
                case "year":
                    getOverview("3");
                    break;
            }
        });

        function getOverview(timeCycle) {
            $.ajax({
                url: "<%=path%>/service/sportRecord/getAllByTime",
                data: {userId: "9677167136687", timeUnit: timeCycle},
                dataType: "json",
                type: "post",
                success: function (data) {
                    if (data.resCode == "000000") {
                        getheartValue(data.data.xTime, data.data.avgHeart);
                        getStepValue(data.data.xTime, data.data.sumStep);
                        getDistanceValue(data.data.xTime, data.data.sumDistance);
                        getEleValue(data.data.xTime, data.data.sumELe);
                    }
                }
            });
        }

        function getheartValue(xVal, yVal) {
            var heartRateCtx = $("#heartRateChart").get(0).getContext("2d");
            var heartRateChart = new Chart(heartRateCtx);
            var data = {
                labels: xVal,
                datasets: [
                    {
                        fillColor: "rgba(220,220,220,0.5)",
                        strokeColor: "rgba(220,220,220,1)",
                        data: yVal
                    }
                ]
            };
            heartRateChart.Bar(data);
        }

        function getStepValue(xVal, yVal) {
            var stepCtx = $("#stepChart").get(0).getContext("2d");
            var stepChart = new Chart(stepCtx);
            var data = {
                labels: xVal,
                datasets: [
                    {
                        fillColor: "rgba(220,220,220,0.5)",
                        strokeColor: "rgba(220,220,220,1)",
                        data: yVal
                    }
                ]
            };
            stepChart.Bar(data);
        }

        function getDistanceValue(xVal, yVal) {
            var distanceCtx = $("#distanceChart").get(0).getContext("2d");
            var distanceChart = new Chart(distanceCtx);
            var data = {
                labels: xVal,
                datasets: [
                    {
                        fillColor: "rgba(220,220,220,0.5)",
                        strokeColor: "rgba(220,220,220,1)",
                        data: yVal
                    }
                ]
            };
            distanceChart.Bar(data);
        }

        function getEleValue(xVal, yVal) {
            var eleCtx = $("#eleChart").get(0).getContext("2d");
            var eleChart = new Chart(eleCtx);
            var data = {
                labels: xVal,
                datasets: [
                    {
                        fillColor: "rgba(220,220,220,0.5)",
                        strokeColor: "rgba(220,220,220,1)",
                        data: yVal
                    }
                ]
            };
            eleChart.Bar(data);
        }
    });
</script>
</body>
</html>
