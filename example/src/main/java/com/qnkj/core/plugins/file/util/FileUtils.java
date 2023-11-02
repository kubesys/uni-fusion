package com.qnkj.core.plugins.file.util;

import com.github.restapi.utils.Md5Util;
import com.qnkj.core.plugins.file.entity.UploadResult;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private static Map<String, String> filetypes = new HashMap<>(1);

    static {
        filetypes.put("png", "image/png");
        filetypes.put("jpg", "image/jpg");
        filetypes.put("jpeg", "image/jpeg");
        filetypes.put("gif", "image/gif");
        filetypes.put("ico", "image/x-icon");
        filetypes.put("doc", "application/msword");
        filetypes.put("docx", "application/msword");
        filetypes.put("xls", "application/x-xls");
        filetypes.put("xlsx", "application/x-xls");
        filetypes.put("ppt", "application/ppt");
        filetypes.put("pdf", "application/pdf");
        filetypes.put("zip", "application/octet-stream");
        filetypes.put("rar", "application/octet-stream");
        filetypes.put("gz", "application/octet-stream");
        filetypes.put("7z", "application/octet-stream");
        filetypes.put("mp3", "audio/mp3");
        filetypes.put("xml", "application/xml");
        filetypes.put("json", "application/json");
        filetypes.put("htm", "text/html");
        filetypes.put("html", "text/html");
    }
    public static Map<String, String> getFileTypes() {
        return filetypes;
    }

    public static String getFileExt(MultipartFile multipartFile) throws Exception {
        String fileName = multipartFile.getOriginalFilename();
        if(fileName != null) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return "";
    }

    public static UploadResult upload(MultipartFile multipartFile) throws Exception {
        String url;
        String fileName = multipartFile.getOriginalFilename();
        if(fileName != null) {
            try(InputStream inputStream = multipartFile.getInputStream()){
                String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
                byte[] buffer;
                int len = inputStream.available();
                buffer = new byte[len];
                len = inputStream.read(buffer);
                url = MinIOUtils.uploadMinio(buffer,fileName);
                return new UploadResult(url, fileName, ext, "", len);
            }
        }
        return null;
    }


    /**
     * 将文件名解析成文件的上传路径
     */
    public static File upload(MultipartFile file, String filePath, String md5) throws Exception {
        String suffix = getFileExt(file);
        String fileName = md5 + "." + suffix;
        String path = filePath + fileName;
        // getCanonicalFile 可解析正确各种路径
        File dest = new File(path).getCanonicalFile();
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        // 文件写入
        file.transferTo(dest);
        return dest;
    }
    public static File upload(MultipartFile file, String filePath) throws Exception {
        // getCanonicalFile 可解析正确各种路径
        File dest = new File(filePath).getCanonicalFile();
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        // 文件写入
        file.transferTo(dest);
        return dest;
    }

    public static String getMD5(MultipartFile multipartFile) throws Exception {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            byte[] buffer;
            int len = inputStream.available();
            buffer = new byte[len];
            len = inputStream.read(buffer);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(buffer, 0, len);
            return new String(Hex.encodeHex(md5.digest()));
        } catch (Exception e) {
            logger.error("upload file Exception:", e);
            throw e;
        }
    }

    /**
     * 获取文件扩展名，不带 .
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * Java文件操作 获取不带扩展名的文件名
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 以byte[]方式读取文件
     *
     * @param fileName 文件名
     * @return
     * @throws IOException
     */
    public static byte[] readFileByBytes(String fileName) throws IOException {
        try (InputStream in = new BufferedInputStream(new FileInputStream(fileName));
             ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            byte[] tempbytes = new byte[in.available()];
            for (int i = 0; (i = in.read(tempbytes)) != -1; ) {
                out.write(tempbytes, 0, i);
            }
            return out.toByteArray();
        }
    }

    public static byte[] readUrlByBytes(URL url) throws IOException {
        try (InputStream in = url.openStream();
             ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            byte[] tempbytes = new byte[in.available()];
            for (int i = 0; (i = in.read(tempbytes)) != -1; ) {
                out.write(tempbytes, 0, i);
            }
            return out.toByteArray();
        }
    }
    /**
     * 向文件写入byte[]
     *
     * @param fileName 文件名
     * @param bytes    字节内容
     * @param append   是否追加
     * @throws IOException
     */
    public static void writeFileByBytes(String fileName, byte[] bytes, boolean append) throws IOException {
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(fileName, append))) {
            out.write(bytes);
        }
    }

    public static UploadResult upload(byte[] buffer, String fileName) throws Exception {
        String url = "";
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        int len = 0;
        try {
            url = MinIOUtils.uploadMinio(buffer,fileName);
        } catch (Exception e) {
            logger.error("upload file Exception:", e);
            throw e;
        } finally {

        }
        return new UploadResult(url, fileName, ext, "", len);
    }

    public static String getMD5(byte[] buffer) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(buffer, 0, buffer.length);
        return new String(Hex.encodeHex(md5.digest()));
    }

    public static final InputStream byte2Input(byte[] buf) {
        return new ByteArrayInputStream(buf);
    }

    public static final byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    public static final String getCacheFileName(String fileid) {
        try {
            String basePath = System.getProperty("user.dir");
            File pathfile = new File(basePath + File.separator + "cache");
            if (!pathfile.exists()) {
                pathfile.mkdirs();
            }
            String md5 = Md5Util.get(fileid);
            String suffix = FileUtils.getExtensionName(fileid);
            String fullpath = pathfile + File.separator + md5 + "." + suffix;
            if (new File(fullpath).exists()) {
                return fullpath;
            }
        } catch (Exception ignored) {  }
        return "";
    }

    public static void saveCacheFileName(String fileid,byte [] filebuffer) {
        try {
            String basePath = System.getProperty("user.dir");
            File pathfile = new File(basePath + File.separator + "cache");
            if (!pathfile.exists()) {
                if(!pathfile.mkdirs()){
                    throw new Exception("创建目录失败");
                }
            }
            String md5 = Md5Util.get(fileid);
            String suffix = FileUtils.getExtensionName(fileid);
            String fullpath =  pathfile + File.separator + md5 + "." + suffix;
            File file = new File(fullpath);
            if (file.exists()) {
                if(!file.delete()){
                    throw new Exception("删除文件失败");
                }
            }
            FileUtils.writeFileByBytes(fullpath,filebuffer,false);
        } catch (Exception ignored) {  }
    }
    public static void responseSetHeaders(HttpServletResponse response, String filename) {
        int seconds = 30 * 24 * 60 * 60;
        response.setHeader("Pragma", "max-age=" + seconds);
        // 时间是从1970开始 设置当前时间+缓存的时间 时间单位为毫秒 如下缓存时间1分钟
        response.setDateHeader("expires", System.currentTimeMillis() + 1000 * seconds);
        // 设置缓存时间为30天 时间单位为秒 30 * 24 * 60 * 60
        response.addHeader("Cache-Control", "max-age=" + seconds);
        response.addHeader("Content-Disposition", "inline;fileName=\"" + filename + "\"");
    }
}
