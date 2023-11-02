package com.qnkj.core.webconfigs.aspect;


import com.github.restapi.XN_Rest;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.core.base.modules.settings.operationlog.entity.Operationlog;
import com.qnkj.core.base.modules.settings.operationlog.service.IOperationlogService;
import com.qnkj.core.utils.IpUtil;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.webconfigs.annotation.Log;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@Component
@Aspect
@Slf4j
@Configuration
public class LogAspect extends BaseAspectSupport {

    private final IOperationlogService logService;

    private long currentTime = 0L;

    @Value("${log.enabled}")
    private Boolean enabled;

    public LogAspect(IOperationlogService logService) {
        this.logService = logService;
    }

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.qnkj.core.webconfigs.annotation.Log)")
    public void logPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param joinPoint join point for advice
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        currentTime = System.currentTimeMillis();
        result = joinPoint.proceed();
        if (enabled) {
            long costtime = System.currentTimeMillis() - currentTime;
            Operationlog plog = new Operationlog("INFO",costtime);
            Method method = resolveMethod(joinPoint);
            Log annotation = method.getAnnotation(Log.class);

            plog.setDescription(annotation.value());
            HttpServletRequest request = getHttpServletRequest();
            String ip = IpUtil.getIpAddr(request);
            plog.setIp(ip);
            plog.setProfileid(getCurrentProfileId());
            plog.setBrowser(LogUtils.getBrowser(request));

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 方法路径
            String methodName = joinPoint.getTarget().getClass().getName()+"."+signature.getName()+"()";
            plog.setMethod(methodName);
//            StringBuilder params = new StringBuilder("{");
//            //参数值
//            Object[] argValues = joinPoint.getArgs();
//            //参数名称
//            String[] argNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();
//            if(argValues != null){
//                for (int i = 0; i < argValues.length; i++) {
//                    params.append(argNames[i]).append(": ").append(argValues[i]);
//                }
//            }
            String uri = request.getRequestURI();
            if (request.getQueryString() != null ){
                uri += "?" + request.getQueryString();
            }
            plog.setUri(uri);
            String address = LogUtils.getCityInfo(ip);
            plog.setAddress(address);
            plog.setException_detail("");
            String host = request.getHeader("Host");
            BaseSaasConfig.existDomain(host);
            logService.writeLog(XN_Rest.getApplication(),plog);
        }
        return result;
    }

    private HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * 配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        if (enabled) {
            Operationlog plog = new Operationlog("ERROR",System.currentTimeMillis() - currentTime);
            plog.setDescription("");
            plog.setException_detail(e.getMessage());
            HttpServletRequest request = getHttpServletRequest();
            String ip = IpUtil.getIpAddr(request);
            plog.setIp(ip);
            plog.setProfileid(getCurrentProfileId());
            plog.setBrowser(LogUtils.getBrowser(request));
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String methodName = joinPoint.getTarget().getClass().getName()+"."+signature.getName()+"()";
            plog.setMethod(methodName);
            String uri = request.getRequestURI();
            if (request.getQueryString() != null ){
                uri += "?" + request.getQueryString();
            }
            plog.setUri(uri);
            String host = request.getHeader("Host");
            BaseSaasConfig.existDomain(host);
            logService.writeLog(XN_Rest.getApplication(),plog);
        }
    }

    public String getCurrentProfileId() {
        try {
            return ProfileUtils.getCurrentProfileId();
        }catch (Exception e){
            return "";
        }
    }


}
