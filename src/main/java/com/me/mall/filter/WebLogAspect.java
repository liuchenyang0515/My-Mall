package com.me.mall.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 描述：打印请求和响应信息
 */
@Aspect
@Component
public class WebLogAspect {
    private final Logger log = LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("execution(public * com.me.mall.controller.*.*(..))")
    public void webLog() {

    }

    /**
     * [02:17 13:19:41.482] [INFO] [com.me.mall.filter.WebLogAspect] - URL: http://127.0.0.1:8083/test
     * [02:17 13:19:41.483] [INFO] [com.me.mall.filter.WebLogAspect] - HTTP_METHOD: GET
     * [02:17 13:19:41.483] [INFO] [com.me.mall.filter.WebLogAspect] - IP: 127.0.0.1
     * [02:17 13:19:41.485] [INFO] [com.me.mall.filter.WebLogAspect] - CLASS_METHOD: com.me.mall.controller.UserController.personalPage
     * [02:17 13:19:41.485] [INFO] [com.me.mall.filter.WebLogAspect] - ARGS: []
     * [02:17 13:19:41.521] [INFO] [com.zaxxer.hikari.HikariDataSource] - me_mall_datasource - Starting...
     * [02:17 13:19:41.699] [INFO] [com.zaxxer.hikari.HikariDataSource] - me_mall_datasource - Start completed.
     * [02:17 13:19:41.781] [INFO] [com.me.mall.filter.WebLogAspect] - RESPONSE: {"id":2,"username":"xiaomu","password":"AWRuqaxc6iryhHuA4OnFag==",
     * "personalizedSignature":"更新了我的签名","role":2,"createTime":1576566692000,"updateTime":1581299532000}
     * @param joinPoint
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        // 收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        log.info("URL: " + request.getRequestURL().toString());
        log.info("HTTP_METHOD: " + request.getMethod());
        log.info("IP: " + request.getRemoteAddr());
        log.info("CLASS_METHOD: " + joinPoint.getSignature().getDeclaringTypeName()
                + "." + joinPoint.getSignature().getName()); // 获得类信息
        log.info("ARGS: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "res", pointcut = "webLog()")
    public void doAfterReturning(Object res) throws JsonProcessingException {
        // 处理完成，返回内容
        log.info("RESPONSE: " + new ObjectMapper().writeValueAsString(res));
    }
}