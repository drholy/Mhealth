<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.5.23.0023
  Time: 下午 1:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="container-fluid footer">
    <div class="row">
        <div class="col-md-12">
            <p>Copyright ©2016</p>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $("body").css("min-height", $(window).height() + "px");
        //注销
        $("#logout").click(function () {
            $.ajax({
                url: "<%=path%>/service/user/logout",
                type: "post",
                dataType: "json",
                success: function (data) {
                    if (data.resCode == "000000") {
                        location.href = "<%=path%>/";
                    } else if (data.resCode == "100105" || data.resCode == "100106") {
                        location.href = "<%=path%>/";
                    } else swal({
                        title: "错误",
                        text: data.resCode + ":" + data.resMsg,
                        type: "error",
                        confirmButtonText: "确定"
                    });
                }
            });
        });

        $("#docLogout").click(function () {
            $.ajax({
                url: "<%=path%>/service/doctorData/logout",
                type: "post",
                dataType: "json",
                success: function (data) {
                    if (data.resCode == "000000") {
                        location.href = "<%=path%>/doctor/login.ui";
                    } else swal({
                        title: "错误",
                        text: data.resCode + ":" + data.resMsg,
                        type: "error",
                        confirmButtonText: "确定"
                    });
                }
            });
        });

        $("#adminLogout").click(function () {
            $.ajax({
                url: "<%=path%>/service/adminData/logout",
                type: "post",
                dataType: "json",
                success: function (data) {
                    if (data.resCode == "000000") {
                        location.href = "<%=path%>/admin/login.ui";
                    } else swal({
                        title: "错误",
                        text: data.resCode + ":" + data.resMsg,
                        type: "error",
                        confirmButtonText: "确定"
                    });
                }
            });
        });
    });
</script>
