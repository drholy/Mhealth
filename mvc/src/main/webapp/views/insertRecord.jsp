<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.4.12.0012
  Time: 下午 4:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%String path=request.getContextPath();%>
<html>
<head>
    <script type="text/javascript" src="<%=path%>/js/jquery-1.11.3.min.js"></script>
    <title>insert</title>
</head>
<body>
    <script type="text/javascript">
        $(document).ready(function(){
            $("#insert").click(function(){
                var dataList=new Array();
                var temp=1;
                for(var i=0;i<1000;i++){
                    if(i%10==0) temp=parseInt(Math.random()*10000000000000);
                    var now=new Date().getTime();
                    var beginTime=Math.random()*(365*24*60*60*1000)+now-(365*24*60*60*1000);
                    var record={
                        userId:"9677167136687",
                        deviceId:"111111",
                        sport_heartRate:parseInt(Math.random()*90+60),
                        distance:Math.random()*20,
                        stepCount:Math.random()*3000+5000,
                        elevation:Math.random()*5,
                        beginTime:beginTime,
                        endTime:beginTime+parseInt(Math.random()*60*60*1000),
                        uploadTime:now
                    };
                    dataList[i]=record;
                }
                var datas=JSON.stringify(dataList);
                $.ajax({
                    url:"<%=path%>/service/sportRecord/insertRecord",
                    data:{dataList:datas},
                    type:"post",
                    dataType:"json",
                    success:function(data){
                        //alert(data.resCode+":"+data.resMsg);
                        $("#res").append(data.resCode+":"+data.resMsg);
                    }
                });
            });
        });
    </script>
    <h1>插入数据</h1>
    <button id="insert">插入</button>
    <div id="res"></div>
</body>
</html>
