 package com.qnkj.common.utils;

 import com.alibaba.fastjson.JSON;
 import com.qnkj.common.entitys.ChinaArea;
 import lombok.extern.slf4j.Slf4j;
 import org.springframework.core.io.ClassPathResource;

 import java.io.BufferedReader;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;

 /**
  * @author oldhand
  */
 @Slf4j
 public class ChinaAreaUtils {
    private static List<ChinaArea> chinaareas = new ArrayList<>(1);

    private ChinaAreaUtils() {}

    static {
        try {
            String filePath = "chinaArea.json";
            ClassPathResource resource = new ClassPathResource(filePath);
            if (!resource.exists()) {
                log.error("{} does not exist!",filePath);
            }else {
                InputStream inputStream = ChinaAreaUtils.class.getClassLoader().getResourceAsStream(filePath);
                if (inputStream != null) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    chinaareas = JSON.parseArray(sb.toString(), ChinaArea.class);
                } else {
                    log.error("{} Exception : open file error",filePath);
                }
            }
        } catch (Exception e) {
            log.error("Exception : {}",e.getMessage());
        }
    }
    /**
     * 根据关键字查行政区划
     *
     * @param keyword
     * @return
     */
    public static Map<String,Object> search(String keyword) {
        String province = "";
        String city = "";
        String district = "";
        for (ChinaArea info : chinaareas) {
            if (info.getName().equals(keyword)) {
                switch (info.getLevel()) {
                    case "3":
                        district = info.getName();
                        ChinaArea cityArea = getChinaArea(info.getParent());
                        if (cityArea != null) {
                            ChinaArea provinceArea = getChinaArea(cityArea.getParent());
                            city = cityArea.getName();
                            if (provinceArea != null) {
                                province = provinceArea.getName();
                            }
                        }
                        break;
                    case "2":
                        district = "";
                        ChinaArea provinceArea = getChinaArea(info.getParent());
                        city = info.getName();
                        if (provinceArea != null) {
                            province = provinceArea.getName();
                        }
                        break;
                    case "1":
                        district = "";
                        city = "";
                        province = info.getName();
                        break;
                    default:
                        break;
                }
                break;
            }
        }
        Map<String,Object> result = new HashMap<>(1);
        result.put("province", province);
        result.put("city", city);
        result.put("district", district);
        return result;
    }

    private static ChinaArea getChinaArea(String code) {
        for (ChinaArea info : chinaareas) {
            if (info.getCode().contains(code)) {
                return info;
            }
        }
        return null;
    }
}
