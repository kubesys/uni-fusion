package com.qnkj.core.plugins.file.controller;

import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.plugins.file.entity.UploadFile;
import com.qnkj.core.plugins.file.entity.UploadResult;
import com.qnkj.core.plugins.file.service.IUploadFileService;
import com.qnkj.core.plugins.file.util.FileUtils;
import com.qnkj.core.plugins.file.util.ImageUtils;
import com.qnkj.core.plugins.file.util.MinIOUtils;
import com.qnkj.core.webconfigs.exception.WebException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;


/**
 * @author oldhand
 * @date 2019-12-16
 */
@Slf4j
@Api(tags = "框架：文件对象存储管理")
@RestController
@RequiredArgsConstructor
public class MinIOFileController {

    private final IUploadFileService uploadFileService;

    private final List<String> phototypes = Arrays.asList("png", "jpg", "jpeg", "gif", "ico");

    @ApiOperation("上传文件")
    @PostMapping("/file/upload")
    public WebResponse upload(@RequestParam(name="file") MultipartFile file) {
        try{
            return new WebResponse().data(uploadFile(file));
        }catch (WebException e){
            throw e;
        }
    }

    @ApiOperation("获取文件")
    @GetMapping("/file/{bucket}/**")
    public void getStorage(@PathVariable(value="bucket") String bucket, HttpServletRequest request, HttpServletResponse response) {
        try {
            String uri = request.getRequestURI();
            log.info("download uri: {}" , uri);
            String url = uri.replaceAll("/file", "");
            url = URLDecoder.decode(url,"UTF-8");
            UploadFile uploadFile = uploadFileService.loadByUrl(url);
            if (uploadFile == null) {
                throw new WebException("数据库中文件不存在");
            }
            if (phototypes.contains(uploadFile.filetype.toLowerCase())) {
                String cacheFile = FileUtils.getCacheFileName(uri);
                byte[] filebuffer;
                if (cacheFile.isEmpty()) {
                    String  objName = url.replaceAll("/" + bucket, "");
                    filebuffer = MinIOUtils.downloadMinio(bucket,objName);
                    FileUtils.saveCacheFileName(url,filebuffer);
                } else {
                    filebuffer = FileUtils.readFileByBytes(cacheFile);
                }
                byte [] processBuffer = processImage( url,  request,  response, filebuffer);
                if (FileUtils.getFileTypes().containsKey(uploadFile.filetype.toLowerCase())) {
                    String contenttype = FileUtils.getFileTypes().get(uploadFile.filetype.toLowerCase());
                    response.setContentType(contenttype);
                } else {
                    response.setContentType("text/plain");
                }
                response.setContentLength(processBuffer.length);
                ServletOutputStream sos = response.getOutputStream();
                sos.write(processBuffer);
                sos.close();
                response.flushBuffer();
            } else {
                String cacheFile = FileUtils.getCacheFileName(url);
                byte[] filebuffer;
                if (cacheFile.isEmpty()) {
                    String  objName = url.replaceAll("/" + bucket, "");
                    filebuffer = MinIOUtils.downloadMinio(bucket,objName);
                    FileUtils.saveCacheFileName(url,filebuffer);
                } else {
                    filebuffer = FileUtils.readFileByBytes(cacheFile);
                }

                if (FileUtils.getFileTypes().containsKey(uploadFile.filetype.toLowerCase())) {
                    String contenttype = FileUtils.getFileTypes().get(uploadFile.filetype.toLowerCase());
                    response.setContentType(contenttype);
                } else {
                    response.setContentType("text/plain");
                }
                response.setContentLength(filebuffer.length);
                ServletOutputStream sos = response.getOutputStream();
                sos.write(filebuffer);
                sos.close();
                response.flushBuffer();
            }

        } catch (Exception e) {
            throw new WebException(e.getMessage());
        }
    }

    /**
     * rotate:?rotate=90
     * crop:?crop=100x200 100:width 200:height
     * resize:?resize=200x400 200:width 400:height
     * width:?width=200 200:width
     * format:?fmt=png
     * quality:?quality=70 quality:1-100
     */
    private byte[] processImage(String fileid, HttpServletRequest request, HttpServletResponse response, byte[] filebuffer) throws Exception {
        try {
            String querystring = request.getQueryString();
            if (Utils.isEmpty(querystring)) {
                return filebuffer;
            }
            log.info("processImage querystring: {}" , querystring);
            int rotateValue = 0;
            int resizeWidth = 0;
            int resizeHeight = 0;
            int cropWidth = 0;
            int cropHeight = 0;
            int qualityValue = 100;

            String rotate = request.getParameter("rotate");
            String resize = request.getParameter("resize");
            String width = request.getParameter("width");
            String quality = request.getParameter("quality");
            String crop = request.getParameter("crop");
            String fmt = request.getParameter("fmt");

            if(fmt!=null){
                int idx = fmt.lastIndexOf('.');
                if(idx>0){
                    fmt=fmt.substring(idx);
                }
                fmt = fmt.toLowerCase();
            }
            if(rotate!=null){
                rotateValue = Integer.parseInt(rotate);
            }

            if(quality!=null){
                qualityValue = Integer.parseInt(quality);
            }

            if(resize!=null){
                String[] rewh = resize.split("x");
                if(rewh.length==2){
                    resizeWidth = Integer.parseInt(rewh[0]);
                    resizeHeight = Integer.parseInt(rewh[1]);
                } else {
                    resizeHeight = resizeWidth = Integer.parseInt(resize);
                }
            }

            if(width!=null){
                resizeWidth = Integer.parseInt(width);
                resizeHeight = 0;
            }

            if(crop!=null){
                String[] crwh = crop.split("x");
                if(crwh.length==2){
                    cropWidth = Integer.parseInt(crwh[0]);
                    cropHeight = Integer.parseInt(crwh[1]);
                } else {
                    cropHeight = cropWidth = Integer.parseInt(crop);
                }
            }
            return ImageUtils.processImage(fileid,filebuffer, rotateValue, qualityValue, resizeWidth, resizeHeight, cropWidth, cropHeight,fmt);

        } catch (Exception e) {
            log.error("processImage Exception : {}",e.getMessage());
            throw e;
        }
    }

    @ApiOperation("编辑器上传文件")
    @PostMapping("/file/edit/upload")
    public WebResponse editUpload(@RequestParam(name="edit") MultipartFile file) {
        try {
            return new WebResponse().code(0).data("/file" + uploadFile(file).url);
        } catch (Exception e) {
            throw new WebException(e.getMessage());
        }
    }

    private UploadFile uploadFile(MultipartFile file) throws WebException {
        if (file.isEmpty()) {
            throw new WebException("上传文件为空！");
        }
        try {
            String fileext = FileUtils.getFileExt(file).toLowerCase();
            if (!FileUtils.getFileTypes().containsKey(fileext)) {
                throw new WebException("." + fileext + " not allow upload!");
            }
            String md5 = FileUtils.getMD5(file);
            UploadFile uploadFile = uploadFileService.load(md5);
            if (uploadFile == null) {
                UploadResult uploadResult = FileUtils.upload(file);
                uploadResult.uniquekey = md5;
                uploadFile = uploadFileService.create(uploadResult);
            }
            return uploadFile;
        } catch (Exception e) {
            throw new WebException(e.getMessage());
        }
    }
}
