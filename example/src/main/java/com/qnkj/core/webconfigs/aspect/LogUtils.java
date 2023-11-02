package com.qnkj.core.webconfigs.aspect;

import cn.hutool.core.io.resource.ClassPathResource;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class LogUtils {

    /**
     * 用于IP定位转换
     */
    static final String REGION = "内网IP|内网IP";

    private static DbSearcher searcher;

    private static Map<String,String> cache = new HashMap<>();

    static {
        try {
            String path = "ip2region/ip2region.db";
            String name = "ip2region.db";
            DbConfig config = new DbConfig();
            File file = inputStreamToFile(new ClassPathResource(path).getStream(), name);
            searcher = new DbSearcher(config, file.getPath());

        } catch (Exception e) {
            System.out.println(" Exception :" + e.getMessage());
        }
    }

    public static String getBrowser(HttpServletRequest request){
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        Browser browser = userAgent.getBrowser();
        return browser.getName();
    }

    /**
     * 根据ip获取详细地址
     */
    public static String getCityInfo(String ip) {
        try {
            if (cache.containsKey(ip)) {
                return cache.get(ip);
            }
            DataBlock dataBlock = searcher.btreeSearch(ip);
            String address = dataBlock.getRegion().replace("0|","");
            if(address.charAt(address.length()-1) == '|'){
                address = address.substring(0,address.length() - 1);
            }
            String desc = address.equals(LogUtils.REGION)?"内网IP":address;
            cache.put(ip,desc);
            return desc;
        } catch (Exception ignored) {
        }
        return "";
    }

    /**
     * inputStream 转 File
     */
    static File inputStreamToFile(InputStream ins, String name) throws Exception{
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + name);
        if (file.exists()) {
            return file;
        }
        try(OutputStream os = new FileOutputStream(file)) {
            int bytesRead;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            ins.close();
        }
        return file;
    }
}
