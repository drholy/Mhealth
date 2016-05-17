<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.5.17.0017
  Time: 下午 9:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>mhealth_医生端</title>
    <%@ include file="/WEB-INF/views/base/head.jsp" %>
</head>
<body class="pfbg">
<%@ include file="/WEB-INF/views/base/nav.jsp" %>
<%@ include file="/WEB-INF/views/base/sidebar.jsp" %>

<div class="container">
    <div class="row">
        <div class="col-md-10 col-md-offset-2 pfcontent">
            <div class="page-header">
                <h1>健康建议</h1>
            </div>
            <div class="row">
                <div class="col-md-12" id="commCont">

                </div>
            </div>
            <div class="row">
                <div class="col-md-8 col-md-offset-2">
                    <ul id="pager"></ul>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="docModal" tabindex="-1" role="dialog" aria-labelledby="detailModalTitle">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="detailModalTitle">医生信息</h4>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-md-8">
                            <div class="row">
                                <div id="realNameK" class="col-md-4"><p>姓名：</p></div>
                                <div id="realNameV" class="col-md-8"><p></p></div>
                            </div>
                            <div class="row">
                                <div id="organizationK" class="col-md-4"><p>单位：</p></div>
                                <div id="organizationV" class="col-md-8"><p></p></div>
                            </div>
                            <div class="row">
                                <div id="officeK" class="col-md-4"><p>部门：</p></div>
                                <div id="officeV" class="col-md-8"><p></p></div>
                            </div>
                            <div class="row">
                                <div id="regTimeK" class="col-md-4"><p>加入时间：</p></div>
                                <div id="regTimeV" class="col-md-8"><p></p></div>
                            </div>
                            <div class="row">
                                <div id="mobilePhoneK" class="col-md-4"><p>手机号：</p></div>
                                <div id="mobilePhoneV" class="col-md-8"><p></p></div>
                            </div>
                            <div class="row">
                                <div id="emailK" class="col-md-4"><p>邮箱：</p></div>
                                <div id="emailV" class="col-md-8"><p></p></div>
                            </div>
                            <div class="row">
                                <div id="certificateK" class="col-md-4"><p>证书证明：</p></div>
                            </div>
                            <div class="row">
                                <div id="certificateV" class="col-md-8"><p></p></div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div id="headImgV" class="col-md-12"><p></p></div>
                        </div>
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
        var PAGESIZE = 20;
        getData("${sessionScope.user.id}", 1, PAGESIZE);

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
                var date = new Date(Number(comment["time"])).format('yyyy-M-d hh:mm:ss');
                var row = '<div class="panel panel-default">'
                        + '<div class="panel-heading">'
                        + comment["title"]
                        + '<div class="docInfo">'
                        + '<div class="headImg">'
                        + '<img src="<%=path%>/service/pic/getUserImg/' + comment["docHeadImg"] + '" width="30" height="30" alt="头像"/>'
                        + '</div>'
                        + '<div class="docu"><a href="#" data-toggle="modal" data-target="#docModal" data-docId="' + comment["doctorId"] + '">' + comment["docRealName"] + '</a></div>'
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
                    getData("${sessionScope.user.id}", page, PAGESIZE);
                }
            });
        }

        $('#docModal').on('show.bs.modal', function (event) {

            var dbDate;
            var fmt = "yyyy年M月d日";
            var button = $(event.relatedTarget);

            var doctorId = button.attr("data-docId");
            var modal = $(this);
            $.ajax({
                url: "<%=path%>/service/user/getDocById",
                type: "post",
                data: {doctorId: doctorId},
                dataType: "json",
                success: function (data) {
                    if (data.resCode == "000000") {
                        var recipient = data.data.doctor;

                        modal.find("#realNameV").find("p").text(recipient["realName"]);
                        modal.find("#organizationV").find("p").text(recipient["organization"]);
                        modal.find("#officeV").find("p").text(recipient["office"]);
                        modal.find("#eleV").find("p").text(recipient["elevation"]);

                        dbDate = new Date(Number(recipient["regTime"])).format(fmt);
                        modal.find("#regTimeV").find("p").text(dbDate);

                        modal.find("#mobilePhoneV").find("p").text(recipient["mobilePhone"]);
                        modal.find("#emailV").find("p").text(recipient["email"]);

                        modal.find("#certificateV").find("p").html("<img src='<%=path%>/service/pic/getUserImg/" + recipient["certificate"]
                                + "' width='450' height='200'/>");
                        modal.find("#headImgV").find("p").html("<img src='<%=path%>/service/pic/getUserImg/" + recipient["headImg"]
                                + "' width='90' height='120'/>");
                    } else alert(data.resCode + ":" + data.resMsg);
                }
            });
        });
    });
</script>
</body>
</html>
