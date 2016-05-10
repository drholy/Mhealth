package com.mhealth.controller;

import com.mhealth.common.entity.QuickPager;
import com.mhealth.common.entity.Response;
import com.mhealth.common.util.PasswordUtils;
import com.mhealth.common.util.StringUtils;
import com.mhealth.common.util.UUIDUtils;
import com.mhealth.model.Comment;
import com.mhealth.model.Doctor;
import com.mhealth.model.User;
import com.mhealth.service.DoctorService;
import com.mhealth.service.UserService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pengt on 2016.5.9.0009.
 */
@Controller
@RequestMapping("doctor")
public class DoctorController {

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "doctorService")
    private DoctorService doctorService;

    @Resource(name = "multipartResolver")
    private CommonsMultipartResolver multipartResolver;

    /**
     * 医生注册
     *
     * @param docJson
     * @param valid
     * @param request
     * @return
     */
    @RequestMapping(value = "insertDoctor", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String insertDoctor(String docJson, String valid, HttpServletRequest request) {
        if (StringUtils.isEmpty(docJson, valid)) return Response.paramsIsEmpty("注册信息");
        if (!valid.equals(request.getSession().getAttribute("rand"))) return Response.failuer("验证码错误！");
        request.getSession().removeAttribute("rand");

        Doctor doctor = (Doctor) JSONObject.toBean(JSONObject.fromObject(docJson), Doctor.class);
        if (StringUtils.isEmpty(doctor.getLoginName(), doctor.getPassword())) return Response.paramsIsEmpty("注册信息");
        String loginName = doctor.getLoginName();
        if (!doctorService.checkLoginName(loginName)) return Response.isExist("用户已存在！");
        if (doctor.getPassword().length() < 6) return Response.failuer("密码少于6位！");

        doctor.setId(null);
        doctor.setActive("0");
        doctor.setRegTime(System.currentTimeMillis());
        doctor.setStatus("0");
        try {
            doctor.setPassword(PasswordUtils.getEncryptedPwd(doctor.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        multipartResolver.setDefaultEncoding("UTF-8");
        String headName;
        String certName;
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile headImg = multipartRequest.getFile("headImg");
            MultipartFile certificate = multipartRequest.getFile("certificate");
            String headImgPath = "/images/docHeadImgs/";
            String certPath = "/images/docCertImgs/";
            headName = UUIDUtils.getUUID();
            certName = UUIDUtils.getUUID();
            File localHeadImg = new File(headImgPath + headName);
            File localCertificate = new File(certPath + certName);
            try {
                headImg.transferTo(localHeadImg);
                certificate.transferTo(localCertificate);
            } catch (IOException e) {
                e.printStackTrace();
                return Response.failuer("文件上传失败！");
            }
        } else return Response.failuer("无分段上传组件！");

        doctor.setHeadImg(headName);
        doctor.setCertificate(certName);

        if (doctorService.insertDoc(doctor).equals(doctor.getLoginName())) {
            return new Response().addString("loginName", doctor.getLoginName()).setMessage("注册成功").toJson();
        } else return Response.addFailuer("注册失败！");
    }

    /**
     * 医生登录
     *
     * @param loginName
     * @param password
     * @param request
     * @return
     */
    @RequestMapping(value = "login", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String login(String loginName, String password, HttpServletRequest request) {
        if (StringUtils.isEmpty(loginName, password)) return Response.paramsIsEmpty("用户名；密码");
        Doctor dbDoctor = doctorService.getDocByLogin(loginName);
        if (dbDoctor == null) return Response.notExist("用户不存在！");
        try {
            if (!PasswordUtils.validPasswd(password, dbDoctor.getPassword())) return Response.failuer("用户名或密码错误！");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (dbDoctor.getActive().equals("0")) {
            request.getSession().setAttribute("doctorId", dbDoctor.getId());
            request.getSession().setAttribute("loginName", dbDoctor.getLoginName());
            return new Response().setError(Response.DONOT_ACTIVITE, "账户未激活！").toJson();
        }
        if (dbDoctor.getStatus().equals("0"))
            return new Response().setError(Response.FAILURE, "账户异常，请与管理员联系！").toJson();
        request.getSession().setAttribute("doctor", dbDoctor);
        return Response.success("登录成功！");
    }

    /**
     * 分页返回所有医生
     *
     * @param currPage
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "allDoctor", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getAllDoc(String currPage, String pageSize) {
        QuickPager<Doctor> quickPager = new QuickPager<>(currPage, pageSize);
        doctorService.getAllDoc(quickPager);
        return new Response().toPageJson(quickPager);
    }

    /**
     * 选择医生
     *
     * @param userId
     * @param doctorId
     * @return
     */
    @RequestMapping(value = "chooseDoctor", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String chooseDoc(String userId, String doctorId) {
        if (StringUtils.isEmpty(userId, doctorId)) return Response.paramsIsEmpty("用户id，医生id");
        User user = userService.getUserById(userId);
        Doctor doctor = doctorService.getDocById(doctorId);
        if (user == null || doctor == null) return Response.failuer("id未找到");
        if (doctorService.getDocByUser(user.getId()) != null) return Response.failuer("您已选择过医生");
        if (doctor.getUserList().size() >= 20) return Response.failuer("名额已满");
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("loginName", user.getLoginName());
        userMap.put("username", user.getUsername());
        userMap.put("sex", user.getSex());
        userMap.put("birthday", user.getBirthday());
        userMap.put("bloodType", user.getBloodType());
        List<Map<String, Object>> list = doctor.getUserList();
        list.add(userMap);
        doctor.setUserList(list);
        if (doctorService.chooseDoc(doctorId, userMap)) return Response.success("选择成功！");
        else return Response.failuer("数据库错误！");
    }

    /**
     * 取消医生
     *
     * @param userId
     * @param doctorId
     * @return
     */
    @RequestMapping(value = "cancelDoctor", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String cancelDoc(String userId, String doctorId) {
        if (StringUtils.isEmpty(userId, doctorId)) return Response.paramsIsEmpty("用户id，医生id");
        User user = userService.getUserById(userId);
        Doctor doctor = doctorService.getDocById(doctorId);
        if (user == null || doctor == null) return Response.failuer("id未找到");
        if (doctorService.getDocByUser(user.getId()) == null) return Response.failuer("您未选择过医生");
        if (doctorService.cancelDoc(userId, doctorId)) return Response.success("取消成功！");
        else return Response.failuer("数据库错误！");
    }

    /**
     * 返回医生负责的所有用户
     *
     * @param doctorId
     * @return
     */
    @RequestMapping(value = "getUsersByDoc", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getUsersByDoc(String doctorId) {
        if (StringUtils.isEmpty(doctorId)) return Response.paramsIsEmpty("医生id");
        Doctor doctor = doctorService.getDocById(doctorId);
        List<Map<String, Object>> userList = doctor.getUserList();
        return new Response().addList("userList", userList).toJson();
    }

    /**
     * 医生评论用户
     *
     * @param userId
     * @param commentJson
     * @return
     */
    @RequestMapping(value = "commentUser", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String commentUser(String userId, String commentJson) {
        if (StringUtils.isEmpty(userId, commentJson)) return Response.paramsIsEmpty("用户id，评论信息");
        Comment comment = (Comment) JSONObject.toBean(JSONObject.fromObject(commentJson), Comment.class);
        if (StringUtils.isEmpty(comment.getDoctorId(), comment.getDocRealName(), comment.getTitle()
                , comment.getContent(), String.valueOf(comment.getTime()))) return Response.paramsIsEmpty("评论");
        if (userService.addComment(userId, comment)) return Response.success("评论成功！");
        else return Response.failuer("评论失败！");
    }
}
