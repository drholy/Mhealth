<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.5.11.0011
  Time: 下午 3:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>mhealth</title>
    <%@ include file="/WEB-INF/views/base/nav.jsp" %>
</head>
<body class="bgable">
<%@ include file="/WEB-INF/views/base/head.jsp" %>

<div class="container">
    <div id="allDocs" class="row">
    </div>
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <ul id="pager"></ul>
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
                <button id="choose" type="button" class="btn btn-primary">选择</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/base/footer.jsp" %>
<script type="text/javascript">
    $(document).ready(function () {
        var PAGESIZE = 16;
        getRemote(1, PAGESIZE);
        $('#docModal').on('show.bs.modal', function (event) {
            var dbDate;
            var fmt = "yyyy年M月d日";
            var button = $(event.relatedTarget);
            var recipient = button.data('result');
            var modal = $(this);

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
            $("#choose").data("docId", recipient["id"]);
        });
        getDocByUser("${sessionScope.user.id}");//根据用户是否选择医生决定按钮
        $("#choose").click(function () {
            var doctorId = $(this).data("docId");
            $.ajax({
                url: "<%=path%>/service/user/chooseDoctor",
                type: "post",
                data: {userId: "${sessionScope.user.id}", doctorId: doctorId},
                dataType: "json",
                success: function (data) {
                    if (data.resCode == "000000") {
                        swal({
                            title: "成功",
                            text: "选择成功！",
                            type: "success",
                            confirmButtonText: "确定"
                        }, function () {
                            location.reload();
                        });
                    } else swal({
                        title: "错误",
                        text: data.resCode + ":" + data.resMsg,
                        type: "error",
                        confirmButtonText: "确定"
                    });
                }
            });
        });

        function getRemote(currPage, pageSize) {
            $.ajax({
                url: "<%=path%>/service/user/allDoctor",
                type: "post",
                data: {currPage: currPage, pageSize: pageSize},
                dataType: "json",
                success: function (data) {
                    if (data.resCode == "000000") {
                        showData(data.data.rows);
                        showPager(data.data);
                    } else swal({
                        title: "错误",
                        text: data.resCode + ":" + data.resMsg,
                        type: "error",
                        confirmButtonText: "确定"
                    });
                }
            });
        }

        function showData(rows) {
            for (var i in rows) {
                var doctor = rows[i];
                var name = doctor["realName"];
                var org = doctor["organization"];
                var office = doctor["office"];
                var headImg = doctor["headImg"];
                var users = doctor["userList"];
                var docContent = "<div class='col-md-3' data-result='" + JSON.stringify(doctor)
                        + "' data-toggle='modal' data-target='#docModal'>"
                        + "<div style='cursor:pointer;background:rgba(255,255,255,0.7);overflow:hidden;padding:0 10px;'>"
                        + "<div style='text-align:center;padding-top:10px;'>"
                        + "<img src='<%=path%>/service/pic/getUserImg/" + headImg + "' width='120' height='160'/>"
                        + "</div>"
                        + "<p>用户名：" + doctor["loginName"] + "</p>"
                        + "<p>姓名：" + name + "</p>"
                        + "<p>单位：" + org + "</p>"
                        + "<p>部门：" + office + "</p>"
                        + "<p>监护人数：" + users.length + "</p>"
                        + "</div>"
                        + "</div>";
                $("#allDocs").append(docContent);
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
                    showData(page, PAGESIZE);
                }
            });
        }

        function getDocByUser(userId) {
            $.ajax({
                url: "<%=path%>/service/user/getDocByUser",
                type: "post",
                data: {userId: userId},
                dataType: "json",
                success: function (data) {
                    if (data.resCode == "000000") {
                        $("#choose").attr("disabled", "disabled");
                    } else {
                        $("#choose").removeAttr("disabled");
                    }
                }
            });
        }
    });
</script>
</body>
</html>
