package com.qnkj.core.plugins.file.controller;

import cn.hutool.core.util.ObjectUtil;
import com.github.restapi.XN_Rest;
import com.qnkj.common.utils.ContextUtils;
import com.qnkj.common.utils.DateTimeUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.plugins.file.entity.UploadResult;
import com.qnkj.core.plugins.file.util.FileUtils;
import com.qnkj.core.webconfigs.exception.WebException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * @author oldhand
 * @date 2019-12-16
 */
@Slf4j
@Api(tags = "框架：设置图片存储管理")
@RestController
@RequiredArgsConstructor
public class FileConfigController {

    @ApiOperation("上传设置图片文件")
    @PostMapping("/file/config/upload")
    public WebResponse uploadLocal(@RequestParam(name="file") MultipartFile file,HttpServletRequest request) {
        try{
            String fileext = FileUtils.getFileExt(file).toLowerCase();

            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            String target = httpRequest.getOrDefault("file","").toString();
            if(Utils.isEmpty(target)){
                throw new WebException("file not allow upload!");
            }
            if (ContextUtils.isJar()) {
                throw new WebException("file not allow upload!");
            }
            String filepath = ContextUtils.getLocalFilepath(new String[]{"resources", "static", "images", XN_Rest.getApplication()}, true);
            filepath +=  File.separator;
            String uniquekey = "/file/config/" + XN_Rest.getApplication() + "/";
            Map<String,String> maps = new HashMap<String,String>(){{
                put("favicon","favicon.ico");
                put("login","login.jpg");
                put("register","register.jpg");
                put("login-logo","login-logo.png");
                put("banner","banner.png");
                put("banner-small","banner-small.png");
            }};
            if (maps.containsKey(target)) {
                String filename = maps.get(target);
                if (!filename.endsWith(fileext)) {
                    throw new WebException("." + fileext + " not allow upload!");
                }
                filepath += filename;
                uniquekey += filename;
                uniquekey += "?t=" + DateTimeUtils.gettimeStamp();
            } else {
                throw new WebException("file not allow upload!");
            }
            File dest = FileUtils.upload(file, filepath);
            if(ObjectUtil.isNull(dest)){
                throw new Exception("上传失败");
            } else {
                return new WebResponse().data(new UploadResult("","",fileext,uniquekey,(int)file.getSize()));
            }

        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation("获取设置图片文件")
    @GetMapping("/file/config/{domain}/{file}")
    public void getConfigFile(@PathVariable(value="domain") String domain,@PathVariable(value="file") String file,  HttpServletRequest request, HttpServletResponse response) {
        try {
            byte[] filebuffer = new byte[0];
            if (ContextUtils.isJar()){
                URL jarUrl = null;
                String filePath = "/static/images/" + domain + "/" + file;
                jarUrl = FileConfigController.class.getClassLoader().getResource(filePath);
                if(jarUrl == null){
                     filePath = "/static/images/" + file;
                    jarUrl = FileConfigController.class.getClassLoader().getResource(filePath);
                }
                if(jarUrl != null){
                    filebuffer = FileUtils.readUrlByBytes(jarUrl);
                }
            } else {
                String path = null;
                path = ContextUtils.getLocalFilepath(new String[]{"resources", "static", "images", XN_Rest.getApplication()}, true);
                path +=  File.separator + file;
                if (!new File(path).exists()) {
                    String filePath = "/static/images/" + domain + "/" + file;
                    ClassPathResource resource = new ClassPathResource(filePath);
                    if (resource.exists()){
                        path = resource.getFile().getCanonicalPath();
                    } else {
                        filePath = "/static/images/" + file;
                        resource = new ClassPathResource(filePath);
                        if (resource.exists()){
                            path = resource.getFile().getCanonicalPath();
                        }
                    }
                }
                if(Utils.isNotEmpty(path)){
                    filebuffer = FileUtils.readFileByBytes(path);
                }
            }

            if(filebuffer.length > 0) {
                if (file.endsWith("jpg")) {
                    String contenttype = FileUtils.getFileTypes().get("jpg");
                    response.setContentType(contenttype);
                } else if (file.endsWith("png")) {
                    String contenttype = FileUtils.getFileTypes().get("png");
                    response.setContentType(contenttype);
                } else if (file.endsWith("ico")) {
                    String contenttype = FileUtils.getFileTypes().get("ico");
                    response.setContentType(contenttype);
                } else {
                    response.setContentType("text/plain");
                }
                response.setContentLength(filebuffer.length);
                FileUtils.responseSetHeaders(response,file);
                ServletOutputStream sos = response.getOutputStream();
                sos.write(filebuffer);
                sos.close();
                response.flushBuffer();
            }

        } catch (Exception e) {
            throw new WebException(e.getMessage());
        }
    }

}
