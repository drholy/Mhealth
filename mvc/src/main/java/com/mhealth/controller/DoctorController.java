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
import javax.print.Doc;
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
@RequestMapping("doctorData")
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
//        if (StringUtils.isEmpty(doctor.getLoginName(), doctor.getPassword())) return Response.paramsIsEmpty("注册信息");
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

//            String headImgPath = request.getSession().getServletContext().getRealPath("/images/docHeadImgs");
//            String certPath = request.getSession().getServletContext().getRealPath("/images/docCertImgs");

            String filePath = "D:\\images\\userImgs";

            headName = UUIDUtils.getUUID();
            certName = UUIDUtils.getUUID();

            File localHeadImg = new File(filePath, headName);
            File localCertificate = new File(filePath, certName);

            try {
                headImg.transferTo(localHeadImg);
                certificate.transferTo(localCertificate);

                System.out.println(localHeadImg.getAbsolutePath());
                System.out.println(localCertificate.getAbsoluteFile());
            } catch (IOException e) {
                e.printStackTrace();
                return Response.failuer("文件上传失败！");
            }
        } else return Response.failuer("无分段上传组件！");

        doctor.setHeadImg(headName);
        doctor.setCertificate(certName);

        if (doctorService.insertDoc(doctor) != null) {
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
     * 医生注销
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "logout", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("doctor");
        return Response.success("注销成功!");
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

    /**
     * 医生修改密码
     *
     * @param passwdJson
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    @RequestMapping(value = "changePasswd", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String changePasswd(String passwdJson, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (StringUtils.isEmpty(passwdJson)) return Response.paramsIsEmpty("password json信息");
        Map passwdMap = (Map) JSONObject.toBean(JSONObject.fromObject(passwdJson), Map.class);
        String oldPassword;
        String newPassword;
        String againPassword;
        String doctorId;
        try {
            doctorId = (String) passwdMap.get("id");
            oldPassword = (String) passwdMap.get("oldPassword");
            newPassword = (String) passwdMap.get("newPassword");
            againPassword = (String) passwdMap.get("againPassword");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.paramsIsEmpty("密码信息");
        }
        if (!newPassword.equals(againPassword)) return Response.failuer("两次密码不一致！");
        Doctor doctor = doctorService.getDocById(doctorId);
        if (doctor == null) Response.notExist("医生不存在！");
        if (!PasswordUtils.validPasswd(oldPassword, doctor.getPassword()))
            return Response.failuer("密码错误！");
        doctor.setPassword(PasswordUtils.getEncryptedPwd(newPassword));
        if (doctorService.changePasswd(doctor)) {
            request.getSession().setAttribute("doctor", doctor);
            return Response.success("修改成功！");
        } else return Response.failuer("数据库错误！");
    }

    /**
     * 检查医生登录名
     *
     * @param loginName
     * @return
     */
    @RequestMapping(value = "checkLoginName", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String checkLoginName(String loginName) {
        if (StringUtils.isEmpty(loginName)) return Response.paramsIsEmpty("用户名");
        Doctor doctor = doctorService.getDocByLogin(loginName);
        Map<String, Object> map = new HashMap<>();
        if (doctor == null) map.put("valid", true);
        else map.put("valid", false);
        return JSONObject.fromObject(map).toString();
    }

    /**
     * 根据id返回医生信息
     *
     * @param doctorId
     * @return
     */
    @RequestMapping(value = "getDocById", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getDocById(String doctorId) {
        if (StringUtils.isEmpty(doctorId)) return Response.paramsIsEmpty("医生id");
        Doctor doctor = doctorService.getDocById(doctorId);
        JSONObject docJson = JSONObject.fromObject(doctor);
        docJson.remove("password");
        return new Response().addObject("doctor", docJson).toJson();
    }

    /**
     * 医生修改资料
     *
     * @param docJson
     * @param request
     * @return
     */
    @RequestMapping(value = "modify", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String modify(String docJson, HttpServletRequest request) {
        if (StringUtils.isEmpty(docJson)) return Response.paramsIsEmpty("注册信息");

        Doctor doctor = (Doctor) JSONObject.toBean(JSONObject.fromObject(docJson), Doctor.class);
        String loginName = doctor.getLoginName();
        if (!doctorService.checkLoginName(loginName)) return Response.isExist("用户已存在！");
        if (doctor.getPassword().length() < 6) return Response.failuer("密码少于6位！");

        doctor.setActive("0");
        doctor.setStatus("0");

        multipartResolver.setDefaultEncoding("UTF-8");
        String headName;
        String certName;
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile headImg = multipartRequest.getFile("headImg");
            MultipartFile certificate = multipartRequest.getFile("certificate");

            String filePath = "D:\\images\\userImgs";

            headName = UUIDUtils.getUUID();
            certName = UUIDUtils.getUUID();

            File localHeadImg = new File(filePath, headName);
            File localCertificate = new File(filePath, certName);

            try {
                headImg.transferTo(localHeadImg);
                certificate.transferTo(localCertificate);

                System.out.println(localHeadImg.getAbsolutePath());
                System.out.println(localCertificate.getAbsoluteFile());
            } catch (IOException e) {
                e.printStackTrace();
                return Response.failuer("文件上传失败！");
            }
        } else return Response.failuer("无分段上传组件！");

        doctor.setHeadImg(headName);
        doctor.setCertificate(certName);

        if (doctorService.modify(doctor)) {
            request.getSession().removeAttribute("doctor");
            return Response.success("修改成功！");
        } else return Response.addFailuer("修改失败！");
    }
}
