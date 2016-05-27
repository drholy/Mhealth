<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.5.18.0018
  Time: 下午 12:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>mhealth_管理端</title>
    <%@ include file="/WEB-INF/views/base/head.jsp" %>
</head>
<body class="examDoc">
<%@ include file="/WEB-INF/views/base/admin_nav.jsp" %>

<div class="container">
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <table id="docsTable" class="table table-striped">
                <thead>
                <tr>
                    <th>真实姓名</th>
                    <th>单位</th>
                    <th>部门</th>
                    <th>注册时间</th>
                    <th>查看</th>
                </tr>
                </thead>
                <tfoot></tfoot>
                <tbody></tbody>
            </table>
        </div>
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
                <button id="acc" type="button" class="btn btn-success">通过</button>
                <button id="del" type="button" class="btn btn-danger">删除</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/views/base/footer.jsp" %>
<script type="text/javascript">
    $(document).ready(function () {
        var PAGESIZE = 20;
        getData(1, PAGESIZE);
        $("#acc").click(function () {
            var docId = $(this).data("docId");
            $.ajax({
                url: "<%=path%>/service/adminData/activeDoc",
                type: "post",
                data: {doctorId: docId},
                dataType: "json",
                success: function (data) {
                    if (data.resCode == "000000") {
                        alert("激活成功！");
                        location.reload();
                    } else alert(data.resCode + ":" + data.resMsg);
                }
            });
        });
        $("#del").click(function () {
            var docId = $(this).data("docId");
            $.ajax({
                url: "<%=path%>/service/adminData/delDoc",
                type: "post",
                data: {doctorId: docId},
                dataType: "json",
                success: function (data) {
                    if (data.resCode == "000000") {
                        alert("删除成功！");
                        location.reload();
                    } else alert(data.resCode + ":" + data.resMsg);
                }
            });
        });

        function getData(currPage, pageSize) {
            $.ajax({
                url: "<%=path%>/service/adminData/getApplyDoc",
                type: "post",
                data: {currPage: currPage, pageSize: pageSize},
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
            for (var i in rows) {
                var doctor = rows[i];
                var name = doctor["realName"];
                var org = doctor["organization"];
                var office = doctor["office"];
                var regTime = doctor["regTime"];
                var docContent = "<tr>"
                        + "<td>" + name + "</td>"
                        + "<td>" + org + "</td>"
                        + "<td>" + office + "</td>"
                        + "<td>" + new Date(Number(regTime)).format("yyyy年M月d日") + "</td>"
                        + "<td><button type='button' class='btn btn-default' data-result='" + JSON.stringify(doctor)
                        + "' data-toggle='modal' data-target='#docModal'>查看</button></td>"
                        + "</div>"
                        + "</div>"
                        + "</tr>";
                $("#docsTable").find("tbody").append(docContent);
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
            $("#acc").data("docId", recipient["id"]);
            $("#del").data("docId", recipient["id"]);
        });
    });
</script>
</body>
</html>
