 package com.qnkj.common.utils;

 import com.alibaba.fastjson.JSON;
 import com.github.restapi.models.WebException;
 import com.qnkj.common.entitys.BankInfo;
 import lombok.extern.slf4j.Slf4j;
 import org.springframework.core.io.ClassPathResource;

 import java.io.BufferedReader;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.List;

 @Slf4j
 public class BankUtils {

    private static List<BankInfo> banks = Collections.emptyList();

    static {
        try {
            String filePath = "banks.json";
            ClassPathResource resource = new ClassPathResource(filePath);
            if (!resource.exists()) {
                throw new WebException(filePath + " does not exist!");
            }
            InputStream inputStream = BankUtils.class.getClassLoader().getResourceAsStream(filePath);
            if(inputStream != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                banks = JSON.parseArray(sb.toString(), BankInfo.class);
            }
        } catch (Exception e) {
            log.error(" Exception : {}",e.getMessage());
        }
    }
    private BankUtils() {}

    /**
     * 根据银行关键字查银行联行号
     *
     * @param keyword
     * @return
     */
    public static List<BankInfo> getBanksByName(String keyword) {
        List<BankInfo> result = new ArrayList<>();
        for(BankInfo bankInfo : banks){
            if (bankInfo.getBankName().contains(keyword)) {
                result.add(bankInfo);
            }
        }
        return result;
    }
}
