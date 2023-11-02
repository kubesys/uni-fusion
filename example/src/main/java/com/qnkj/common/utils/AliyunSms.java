package com.qnkj.common.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.github.restapi.models.WebException;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


/**
 * Created on 17/6/7.
 * 短信API产品的DEMO程序,工程中包含了一个SmsDemo类，直接通过
 * 执行main函数即可体验短信产品API功能(只需要将AK替换成开通了云通信-短信产品功能的AK即可)
 * 工程依赖了2个jar包(存放在工程的libs目录下)
 * 1:aliyun-java-sdk-core.jar
 * 2:aliyun-java-sdk-dysmsapi.jar
 *
 * 备注:Demo工程编码采用UTF-8
 * 国际短信发送请勿参照此DEMO
 * @author oldhand
 */
@Component
public class AliyunSms {

    /**产品名称:云通信短信API产品,开发者无需替换*/
    static final String PRODUCT = "Dysmsapi";
    /**产品域名,开发者无需替换*/
    static final String DOMAIN = "dysmsapi.aliyuncs.com";
    static final String TIMEOUT = "10000";

    private AliyunSms() {}

    public static SendSmsResponse send(String mobile,String regioncode,String templatecode,String templateparam) throws Exception {
        SaaSUtils saas = new SaaSUtils();
        String accessKeyId = saas.getSMSAccessID();
        String accessKeySecret = saas.getSMSSecret();
        String signname = saas.getSMSSignName();
        if (Utils.isEmpty(accessKeyId)) {
            throw new WebException("accessKeyId is empty");
        }
        if (Utils.isEmpty(accessKeySecret)) {
            throw new WebException("accessKeySecret is empty");
        }
        if (Utils.isEmpty(signname)) {
            throw new WebException("signname is empty");
        }
        if (Utils.isEmpty(templatecode)) {
            throw new WebException("templatecode is empty");
        }
        try {
            //可自助调整超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", TIMEOUT);
            System.setProperty("sun.net.client.defaultReadTimeout", TIMEOUT);

            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            //组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            //必填:待发送手机号
            request.setPhoneNumbers(mobile);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(signname);
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(templatecode);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam(templateparam);

            /**选填-上行短信扩展码(无特殊需求用户请忽略此字段)
            /*request.setSmsUpExtendCode("90997");*/

            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId(MD5Util.get(MD5Util.get(UUID.randomUUID().toString())));

            //hint 此处可能会抛出异常，注意catch

            return acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            throw new WebException(e.getMessage());
        }
    }

    public static SendSmsResponse send(String mobile,String regioncode,String verifycode) throws Exception {
        SaaSUtils saas = new SaaSUtils();
        String accessKeyId = saas.getSMSAccessID();
        String accessKeySecret = saas.getSMSSecret();
        String signname = saas.getSMSSignName();
        String templatecode = saas.getSMSTemplateCode();
        if (Utils.isEmpty(accessKeyId)) {
            throw new WebException("accessKeyId is empty");
        }
        if (Utils.isEmpty(accessKeySecret)) {
            throw new WebException("accessKeySecret is empty");
        }
        if (Utils.isEmpty(signname)) {
            throw new WebException("signname is empty");
        }
        if (Utils.isEmpty(templatecode)) {
            throw new WebException("templatecode is empty");
        }

        try {
            //可自助调整超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", TIMEOUT);
            System.setProperty("sun.net.client.defaultReadTimeout", TIMEOUT);

            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            //组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            //必填:待发送手机号
            request.setPhoneNumbers(mobile);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(signname);
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(templatecode);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam("{\"code\":\"" + verifycode + "\"}");

            /**选填-上行短信扩展码(无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");*/

            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId(MD5Util.get(MD5Util.get(UUID.randomUUID().toString())));

            //hint 此处可能会抛出异常，注意catch

            return acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            throw new WebException(e.getMessage());
        }
    }


    public static QuerySendDetailsResponse querySendDetails(String bizId) throws Exception {

        SaaSUtils saas = new SaaSUtils();
        String accessKeyId = saas.getSMSAccessID();
        String accessKeySecret = saas.getSMSSecret();
        String signname = saas.getSMSSignName();
        String templatecode = saas.getSMSTemplateCode();
        if (Utils.isEmpty(accessKeyId)) {
            throw new WebException("accessKeyId is empty");
        }
        if (Utils.isEmpty(accessKeySecret)) {
            throw new WebException("accessKeySecret is empty");
        }
        if (Utils.isEmpty(signname)) {
            throw new WebException("signname is empty");
        }
        if (Utils.isEmpty(templatecode)) {
            throw new WebException("templatecode is empty");
        }
        try {
            //可自助调整超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", TIMEOUT);
            System.setProperty("sun.net.client.defaultReadTimeout", TIMEOUT);

            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            //组装请求对象
            QuerySendDetailsRequest request = new QuerySendDetailsRequest();
            //必填-号码
            request.setPhoneNumber("15000000000");
            //可选-流水号
            request.setBizId(bizId);
            //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
            SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
            request.setSendDate(ft.format(new Date()));
            //必填-页大小
            request.setPageSize(10L);
            //必填-当前页码从1开始计数
            request.setCurrentPage(1L);

            //hint 此处可能会抛出异常，注意catch

            return acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            throw new WebException(e.getMessage());
        }
    }
}
