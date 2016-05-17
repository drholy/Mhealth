package com.mhealth.controller;

import com.mhealth.common.entity.QuickPager;
import com.mhealth.common.entity.Response;
import com.mhealth.common.util.PasswordUtils;
import com.mhealth.common.util.StringUtils;
import com.mhealth.common.util.UUIDUtils;
import com.mhealth.model.*;
import com.mhealth.service.DeviceService;
import com.mhealth.service.DoctorService;
import com.mhealth.service.TokenService;
import com.mhealth.service.UserService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;

/**
 * Created by pengt on 2016.4.6.0006.
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "doctorService")
    private DoctorService doctorService;

    @Resource(name = "deviceService")
    private DeviceService deviceService;

    @Resource(name = "tokenService")
    private TokenService tokenService;

    /**
     * 用户注册
     *
     * @param userJson
     * @return
     */
    @RequestMapping(value = "insertUser", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String insertUser(String userJson, String valid, HttpServletRequest request) {
        if (StringUtils.isEmpty(userJson, valid)) return Response.paramsIsEmpty("注册信息");
        if (!valid.equals(request.getSession().getAttribute("rand"))) return Response.failuer("验证码错误！");
        request.getSession().removeAttribute("rand");
        User user = (User) JSONObject.toBean(JSONObject.fromObject(userJson), User.class);
        if (StringUtils.isEmpty(user.getLoginName(), user.getPassword())) return Response.paramsIsEmpty("注册信息");
        String loginName = user.getLoginName();
        if (!userService.checkUserByLn(loginName)) return Response.isExist("用户已存在！");
        if (user.getPassword().length() < 6) return Response.failuer("密码少于6位！");

        user.setId(null);
        user.setActive("0");
        user.setRegTime(System.currentTimeMillis());
        user.setStatus("0");
        try {
            user.setPassword(PasswordUtils.getEncryptedPwd(user.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (userService.insertUser(user).equals(user.getLoginName())) {
            return new Response().addString("loginName", user.getLoginName()).setMessage("注册成功").toJson();
        } else return Response.addFailuer("注册失败！");
    }

    /**
     * 移动端注册
     *
     * @param userJson
     * @return
     */
    @RequestMapping(value = "mobileRegister", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String mobileRegister(String userJson) {
        if (StringUtils.isEmpty(userJson)) return Response.paramsIsEmpty("注册信息");
        User user = (User) JSONObject.toBean(JSONObject.fromObject(userJson), User.class);
        if (StringUtils.isEmpty(user.getLoginName(), user.getPassword())) return Response.paramsIsEmpty("注册信息");
        String loginName = user.getLoginName();
        if (!userService.checkUserByLn(loginName)) return Response.isExist("用户已存在！");
        if (user.getPassword().length() < 6) return Response.failuer("密码少于6位！");

        user.setId(null);
        user.setActive("0");
        user.setRegTime(System.currentTimeMillis());
        user.setStatus("0");
        try {
            user.setPassword(PasswordUtils.getEncryptedPwd(user.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (userService.insertUser(user).equals(user.getLoginName())) {
            return new Response().addString("loginName", user.getLoginName()).setMessage("注册成功").toJson();
        } else return Response.addFailuer("注册失败！");
    }

    /**
     * 根据用户名返回用户
     *
     * @param loginName
     * @return
     */
    @RequestMapping(value = "getUser", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getUser(String loginName) {
        if (StringUtils.isEmpty(loginName)) return Response.paramsIsEmpty("登录名");
        return new Response().addObject("user", userService.getUser(loginName)).toJson();
    }

    /**
     * 根据id返回用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "getUserById", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getUserById(String id) {
        if (StringUtils.isEmpty(id)) return Response.paramsIsEmpty("id");
        return new Response().addObject("user", userService.getUserById(id)).toJson();
    }

    /**
     * 登录
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
        User dbUser = userService.getUser(loginName);
        if (dbUser == null) return Response.notExist("用户不存在！");
        try {
            if (!PasswordUtils.validPasswd(password, dbUser.getPassword())) return Response.failuer("用户名或密码错误！");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (dbUser.getActive().equals("0")) {
            request.getSession().setAttribute("userId", dbUser.getId());
            request.getSession().setAttribute("loginName", dbUser.getLoginName());
            return new Response().setError(Response.DONOT_ACTIVITE, "账户未激活！").toJson();
        }
        if (dbUser.getStatus().equals("0")) return new Response().setError(Response.FAILURE, "账户异常，请与管理员联系！").toJson();
        request.getSession().setAttribute("user", dbUser);
        return Response.success("登录成功！");
    }

    /**
     * 客户端登录接口
     *
     * @param loginName
     * @param password
     * @param deviceJson
     * @return
     */
    @RequestMapping(value = "mobileLogin", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String mobileLogin(String loginName, String password, String deviceJson) {
        if (StringUtils.isEmpty(loginName, password, deviceJson)) return Response.paramsIsEmpty("用户名、密码、设备");
        User dbUser = userService.getUser(loginName);
        if (dbUser == null) return Response.notExist("用户不存在！");
        try {
            if (!PasswordUtils.validPasswd(password, dbUser.getPassword())) return Response.failuer("用户名或密码错误！");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (dbUser.getActive().equals("0")) {
            return new Response().addString("userId", dbUser.getId()).addString("loginName", dbUser.getLoginName())
                    .setError(Response.DONOT_ACTIVITE, "账户未激活！").toJson();
        }
        if (dbUser.getStatus().equals("0"))
            return new Response().setError(Response.FAILURE, "账户异常，请与管理员联系！")
                    .addString("id", dbUser.getId()).addString("loginName", dbUser.getLoginName()).toJson();

        Token token = tokenService.getTokenByUser(dbUser.getId());
        if (token == null) token = new Token();

        //设置token过期时间30天
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.DAY_OF_MONTH, 30);

        token.setUserId(dbUser.getId());
        token.setAccess_token(UUIDUtils.getUUID());
        token.setExpire(cal.getTimeInMillis());

        tokenService.upsertToken(token);

        Device device = (Device) JSONObject.toBean(JSONObject.fromObject(deviceJson), Device.class);
        if (StringUtils.isEmpty(device.getId(), device.getName(), device.getBrand(), device.getModel(), device.getOs(), device.getType()))
            return Response.paramsIsEmpty("设备信息");
        device.setUserId(dbUser.getId());
        device.setStatus("1");
        Device dbDevice = deviceService.getDevice(device.getId());
        if (dbDevice == null) {
            deviceService.insertDevice(device);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("userId", dbUser.getId());
        map.put("loginName", dbUser.getLoginName());
        map.put("access_token", token.getAccess_token());

        return new Response().addMap(map).setMessage("登陆成功！").toJson();
    }

    /**
     * 注销
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "logout", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String logout(HttpServletRequest request, String access_token) {
        if (StringUtils.isEmpty(access_token)) {
            request.getSession().removeAttribute("user");
            request.getSession().removeAttribute("device");
            return Response.success("注销成功！");
        } else {
            String userId;
            try {
                userId = tokenService.getTokenByAcc(access_token).getUserId();
            } catch (Exception e) {
                e.printStackTrace();
                return Response.notExist("token不存在！");
            }
            if (tokenService.delTokenByUser(userId)) {
                return Response.success("注销成功！");
            } else return Response.failuer("数据库错误！");
        }
    }

    /**
     * 账户激活
     *
     * @param dataJson
     * @param deviceJson
     * @param request
     * @return
     */
    @RequestMapping(value = "active", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String active(String dataJson, String deviceJson, HttpServletRequest request) {
        if (StringUtils.isEmpty(dataJson)) return Response.paramsIsEmpty("用户信息");
        User user = (User) JSONObject.toBean(JSONObject.fromObject(dataJson), User.class);
        if (StringUtils.isEmpty(user.getId(), user.getUsername(), user.getSex(), String.valueOf(user.getBirthday()), user.getBloodType()))
            return Response.paramsIsEmpty("用户信息");
        if (!StringUtils.isEmpty(user.getMobilePhone()) && !StringUtils.isPhone(user.getMobilePhone()))
            return Response.paramsCheckError("手机号格式有误！");
        if (!StringUtils.isEmpty(user.getEmail()) && !StringUtils.isEmail(user.getEmail()))
            return Response.paramsCheckError("email格式有误！");
        user.setActive("1");
        user.setStatus("1");
        if (userService.active(user)) {
            request.getSession().removeAttribute("loginName");
            request.getSession().removeAttribute("userId");
            request.getSession().setAttribute("user", user);
            if (!StringUtils.isEmpty(deviceJson)) {
                Device device = (Device) JSONObject.toBean(JSONObject.fromObject(deviceJson), Device.class);
                if (StringUtils.isEmpty(device.getId(), device.getName(), device.getBrand(), device.getModel(), device.getOs(), device.getType()))
                    return Response.paramsIsEmpty("设备信息");
                device.setUserId(user.getId());
                device.setStatus("1");
                if (!deviceService.insertDevice(device).equals(device.getId())) return Response.failuer("激活失败！");
                request.getSession().setAttribute("device", device);

                Token token = new Token();

                //设置token过期时间30天
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                cal.add(Calendar.DAY_OF_MONTH, 30);

                token.setUserId(user.getId());
                token.setAccess_token(UUIDUtils.getUUID());
                token.setExpire(cal.getTimeInMillis());

                tokenService.upsertToken(token);

                Map<String, Object> map = new HashMap<>();
                map.put("userId", user.getId());
                map.put("loginName", user.getLoginName());
                map.put("access_token", token.getAccess_token());
                return new Response().addMap(map).setMessage("激活成功！").toJson();
            }
            return Response.success("激活成功！");
        } else {
            return Response.failuer("激活失败！");
        }
    }

    /**
     * 验证码
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "validCode", produces = {"image/jpeg;charset=UTF-8"})
    public void validCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //声明字符串型的arrNumber用于保存产生随机数的数字,其中包括26个英文字母(大写小写)和0-9的数字
        String arrNumber = "0123456789";
        //设置页面不缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        // 在内存中创建图象
        int width = 60, height = 20;
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics g = image.getGraphics();
        //生成随机类
        Random random = new Random();
        // 设定背景色
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        //设定字体
        g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

    /* 取随机产生的认证码(4位数字)*/
        String sRand = "";
        for (int i = 0; i < 4; i++) {
            int rand = random.nextInt(10);
            sRand += String.valueOf(arrNumber.charAt(rand));

            // 将认证码显示到图象中
            g.setColor(new Color(20 + random.nextInt(110), 20 + random
                    .nextInt(110), 20 + random.nextInt(110)));
            //调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
            g.drawString(String.valueOf(arrNumber.charAt(rand)), 13 * i + 6, 16);
            //g.drawString(rand, 13 * i + 6, 16);
        }
        // 将认证码存入SESSION
        request.getSession().setAttribute("rand", sRand);
        // 图象生效
        g.dispose();
        // 输出图象到页面
        ImageIO.write(image, "JPEG", response.getOutputStream());
    }

    //给定范围获得随机颜色
    public Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    /**
     * 资料修改
     *
     * @param dataJson
     * @param request
     * @return
     */
    @RequestMapping(value = "modify", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String modify(String dataJson, HttpServletRequest request) {
        if (StringUtils.isEmpty(dataJson)) return Response.paramsIsEmpty("用户数据");
        User user = (User) JSONObject.toBean(JSONObject.fromObject(dataJson), User.class);
        if (StringUtils.isEmpty(user.getId(), user.getUsername(), user.getSex(), String.valueOf(user.getBirthday()), user.getBloodType()))
            return Response.paramsIsEmpty("用户信息");
        if (!StringUtils.isEmpty(user.getMobilePhone()) && !StringUtils.isPhone(user.getMobilePhone()))
            return Response.paramsCheckError("手机号格式有误！");
        if (!StringUtils.isEmpty(user.getEmail()) && !StringUtils.isEmail(user.getEmail()))
            return Response.paramsCheckError("email格式有误！");
        if (userService.modify(user)) {
            request.getSession().setAttribute("user", user);
            return Response.success("修改成功！");
        } else return Response.failuer("修改失败！");
    }

    /**
     * 修改密码
     *
     * @param passwdJson
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
        String userId;
        try {
            userId = (String) passwdMap.get("id");
            oldPassword = (String) passwdMap.get("oldPassword");
            newPassword = (String) passwdMap.get("newPassword");
            againPassword = (String) passwdMap.get("againPassword");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.paramsIsEmpty("密码信息");
        }
        if (!newPassword.equals(againPassword)) return Response.failuer("两次密码不一致！");
        User user = userService.getUserById(userId);
        if (user == null) Response.notExist("用户不存在！");
        if (!PasswordUtils.validPasswd(oldPassword, user.getPassword()))
            return Response.failuer("密码错误！");
        user.setPassword(PasswordUtils.getEncryptedPwd(newPassword));
        if (userService.changePasswd(user)) {
            request.getSession().setAttribute("user", user);
            return Response.success("修改成功！");
        } else return Response.failuer("数据库错误！");
    }

    /**
     * 检验同户名是否可用
     *
     * @param loginName
     * @return
     */
    @RequestMapping(value = "checkLoginName", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String checkLoginName(String loginName) {
        if (StringUtils.isEmpty(loginName)) return Response.paramsIsEmpty("用户名");
        User user = userService.getUser(loginName);
        Map<String, Object> map = new HashMap<>();
        if (user == null) map.put("valid", true);
        else map.put("valid", false);
        return JSONObject.fromObject(map).toString();
    }

    /**
     * 检验验证码
     *
     * @param request
     * @param valid
     * @return
     */
    @RequestMapping(value = "checkValid", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String checkValid(HttpServletRequest request, String valid) {
        if (StringUtils.isEmpty(valid)) return Response.paramsIsEmpty("验证码");
        String sesseionValid = (String) request.getSession().getAttribute("rand");
        Map<String, Object> map = new HashMap<>();
        if (valid.equals(sesseionValid)) map.put("valid", true);
        else map.put("valid", false);
        return JSONObject.fromObject(map).toString();
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
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("loginName", user.getLoginName());
        userMap.put("username", user.getUsername());
        userMap.put("sex", user.getSex());
        userMap.put("birthday", user.getBirthday());
        userMap.put("bloodType", user.getBloodType());
        userMap.put("headImg", user.getHeadImg());
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
     * 返回当前用户选择的医生
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "getDocByUser", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getDocByUser(String userId) {
        if (StringUtils.isEmpty(userId)) return Response.paramsIsEmpty("用户id");
        Doctor doctor = doctorService.getDocByUser(userId);
        if (doctor != null) return new Response().addObject("doctor", doctor).toJson();
        else return Response.failuer("未选择医生！");
    }

    /**
     * 分页返回建议
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "getComments", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getComments(String userId, String currPage, String pageSize) {
        if (StringUtils.isEmpty(userId)) return Response.paramsIsEmpty("userId");
        QuickPager<Comment> quickPager = new QuickPager<>(currPage, pageSize);
        userService.getComments(quickPager, userId);
        return new Response().toPageJson(quickPager);
    }

    /**
     * 返回指定的医生
     *
     * @param doctorId
     * @return
     */
    @RequestMapping(value = "getDocById", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getDoctorById(String doctorId) {
        if (StringUtils.isEmpty(doctorId)) return Response.paramsIsEmpty("doctorId");
        Doctor doctor = doctorService.getDocById(doctorId);
        return new Response().addObject("doctor", doctor).toJson();
    }
}
