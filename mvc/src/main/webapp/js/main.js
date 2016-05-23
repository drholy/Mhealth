/**
 * Created by pengt on 2016.4.25.0025.
 */
$(document).ready(function () {
    $("body").css("min-height", $(window).height() + "px");
    //注销
    $("#logout").click(function () {
        $.ajax({
            url: "/service/user/logout",
            type: "post",
            dataType: "json",
            success: function (data) {
                if (data.resCode == "000000") {
                    location.href = "../../../";
                } else if (data.resCode == "100105" || data.resCode == "100106") {
                    location.href = "/";
                } else alert(data.resCode + ":" + data.resMsg)
            }
        });
    });

    $("#docLogout").click(function () {
        $.ajax({
            url: "/service/doctorData/logout",
            type: "post",
            dataType: "json",
            success: function (data) {
                if (data.resCode == "000000") {
                    location.href = "/doctor/login.ui";
                } else alert(data.resCode + ":" + data.resMsg)
            }
        });
    });

    $("#adminLogout").click(function () {
        $.ajax({
            url: "/service/adminData/logout",
            type: "post",
            dataType: "json",
            success: function (data) {
                if (data.resCode == "000000") {
                    location.href = "/admin/login.ui";
                } else alert(data.resCode + ":" + data.resMsg)
            }
        });
    });
});
function showTitle(key) {
    var title;
    switch (key) {
        case "sport_heartRate":
            title = "运动心率";
            break;
        case "stepCount":
            title = "步数";
            break;
        case "distance":
            title = "距离";
            break;
        case "elevation":
            title = "海拔";
            break;
        default:
            break;
    }
    $(".starter-template h1").html(title);
}
function getUnit(key) {
    var unit;
    switch (key) {
        case "sport_heartRate":
            unit = "次/分";
            break;
        case "stepCount":
            unit = "步";
            break;
        case "distance":
            unit = "千米";
            break;
        case "elevation":
            unit = "米";
            break;
        default:
            break;
    }
    return unit;
}
Date.prototype.format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
barOption = {
    //String - Colour of the scale line
    scaleLineColor: "rgba(255,255,255,1)",
    //Number - Pixel width of the scale line	
    scaleLineWidth: 2,
    //String - Scale label font colour
    scaleFontColor: "rgba(255,255,255,1)",
    //String - Colour of the grid lines
    scaleGridLineColor: "rgba(255,255,255,0.6)",
    //Number - Width of the grid lines
    scaleGridLineWidth: 1
}
