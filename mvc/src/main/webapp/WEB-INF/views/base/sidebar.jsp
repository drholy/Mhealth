<%--
  Created by IntelliJ IDEA.
  User: pengt
  Date: 2016.5.2.0002
  Time: 下午 9:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="col-md-2 sidebar">
    <ul class="nav nav-sidebar">
        <li><a href="<%=request.getContextPath()%>/user/profile.ui">个人资料</a></li>
        <li><a href="<%=request.getContextPath()%>/user/modify.ui">修改资料</a></li>
        <li><a href="<%=request.getContextPath()%>/user/changePasswd.ui">密码修改</a></li>
        <li><a href="<%=request.getContextPath()%>/user/myDoctor.ui">申请评价</a></li>
        <li><a href="<%=request.getContextPath()%>/user/comment.ui">健康建议</a></li>
    </ul>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $("a[href='<%=request.getContextPath()%>" + location.pathname + "']").parent().addClass("active");
    });
</script>
