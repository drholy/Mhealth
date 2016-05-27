package com.mhealth.controller;

import com.mhealth.common.util.StringUtils;
import com.mhealth.openstack.jclouds.JCloudsSwift;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by pengt on 2016.5.11.0011.
 */
@Controller
@RequestMapping("pic")
public class PictureController {

    @Resource(name = "jCloudsSwift")
    private JCloudsSwift jCloudsSwift;

    @RequestMapping(value = "getUserImg/{fileName}", produces = {"image/jpeg;charset=UTF-8"})
    public void getUserImg(@PathVariable String fileName, HttpServletResponse response) {
        InputStream in = null;
        OutputStream out = null;
        try {
            if (StringUtils.isEmpty(fileName)) response.getWriter().println("文件名不能为空");
//            File file = new File("D:\\images\\userImgs", fileName);
//            in = new FileInputStream(file);
            in = jCloudsSwift.getObject(fileName);
            out = response.getOutputStream();
            int temp;
            while ((temp = in.read()) != -1) {
                out.write(temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
