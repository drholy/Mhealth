<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.5.17.0017
  Time: 下午 4:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>mhealth_医生端</title>
    <%@ include file="/WEB-INF/views/base/head.jsp" %>
</head>
<body>
<%@ include file="/WEB-INF/views/base/doctor_nav.jsp" %>

<div class="container">
    <div class="row">
        <div class="col-md-8 col-md-offset-2" id="commCont">
        </div>
    </div>
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <ul id="pager"></ul>
        </div>
    </div>
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <form id="commForm">
                <div class="form-group">
                    <label for="title">标题</label>
                    <input type="text" class="form-control" id="title" name="title" placeholder="标题">
                </div>
                <div class="form-group">
                    <label for="content">内容</label>
                    <textarea class="form-control" id="content" name="content" rows="3"
                              style="resize: none;"></textarea>
                </div>
                <button type="submit" class="btn btn-primary">提交</button>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        var PAGESIZE = 10;
        getData("${userId}", 1, PAGESIZE);

        $("#commForm").bootstrapValidator({
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            trigger: "blur",
            fields: {
                title: {
                    enabled: true,
                    validators: {
                        notEmpty: {
                            message: "此项为必填项"
                        }
                    }
                },
                content: {
                    enabled: true,
                    validators: {
                        notEmpty: {
                            message: "此项为必填项"
                        }
                    }
                }
            }
        }).on('success.form.bv', function (e) {
            e.preventDefault();
            var comment = {};
            comment["userId"] = "${userId}";
            comment["doctorId"] = "${sessionScope.doctor.id}";
            comment["docRealName"] = "${sessionScope.doctor.realName}";
            comment["title"] = $("#title").val();
            comment["content"] = $("#content").val();
            comment = JSON.stringify(comment);

            $.ajax({
                url: "<%=path%>/service/doctorData/comment",
                type: "post",
                data: {commentJson: comment},
                dataType: "json",
                success: function (data) {
                    if (data.resCode == "000000") {
                        alert("提交成功！");
                        location.href = "<%=path%>/doctor/getUsers.ui"
                    } else alert(data.resCode + ":" + data.resMsg);
                }
            });
        });

        function getData(userId, currPage, pageSize) {
            $.ajax({
                url: "<%=path%>/service/user/getComments",
                type: "post",
                data: {userId: userId, currPage: currPage, pageSize: pageSize},
                dataType: "json",
                success: function (data) {
                    if (data.resCode == "000000") {
                        showData(data.data.rows);
                        showPager(data.data);
                    } else alert(data.resCode + ":" + data.resMsg);
                }
            });
        }

        function showData(rows) {
            $("#commCont").html("");
            for (var i in rows) {
                var comment = rows[i];
                var date = new Date(Number(comment["time"])).format('yyyy-M-d');
                var row = '<div class="panel panel-default">'
                        + '<div class="panel-heading">'
                        + comment["title"]
                        + '<div class="docInfo">'
                        + '<div class="headImg">'
                        + '<img src="<%=path%>/service/pic/getUserImg/' + comment["docHeadImg"] + '" width="30" height="30" alt="头像"/>'
                        + '</div>'
                        + '<div class="docu">' + comment["docRealName"] + '</div>'
                        + '</div>'
                        + '</div>'
                        + '<div class="panel-body">'
                        + comment["content"]
                        + '</div>'
                        + '<div class="panel-footer">' + date + '</div>'
                        + '</div>';
                $("#commCont").append(row);
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
                    getData("${userId}", page, PAGESIZE);
                }
            });
        }
    });
</script>
</body>
</html>
