package com.batman.bysj.common.web.aspect;

import com.batman.bysj.common.web.domin.WebApiResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用一个切面来统一返回前端的JSON格式
 *
 * @author victor.qin
 * @date 2018/6/4 20:52
 */
//@Aspect
//@Component
//public class ResponseAspect {
//    @Resource
//    private MappingJackson2HttpMessageConverter converter;
//
//    @Resource
//    private HttpServletResponse response;
//
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    private HttpOutputMessage outputMessage = null;
//
//    @Pointcut("execution(* com.batman.bysj.common.web.controller.*.*(..)) && @annotation(org.springframework.web.bind.annotation.ResponseBody)")
//    public void responseBodyPointCut() {
//
//    }
//
//    @Around(value = "responseBodyPointCut()")
//    @ResponseBody
//    public void formatResult2JSON(ProceedingJoinPoint pjp) throws Throwable {
//        Object ret = pjp.proceed();
//        WebApiResponse responseBase = new WebApiResponse();
//        responseBase.setData(ret);
//        outputMessage = getHttpOutputMessage(response);
//        converter.write(responseBase, MediaType.APPLICATION_JSON, outputMessage);
//        shutdownResponse(response);
//    }
//
//
//    @AfterThrowing(pointcut = "responseBodyPointCut()", throwing = "error")
//    @ResponseBody
//    public void handleForException(JoinPoint jp, Throwable error) throws Throwable {
//        WebApiResponse responseBase = new WebApiResponse();
//        responseBase.setCode(1);
//        responseBase.setError(error.getMessage());
//        logger.error(jp.getSignature().getName() + "-error!", error);
//        outputMessage = getHttpOutputMessage(response);
//        List<MediaType> list = new ArrayList<>();
//        list.add(MediaType.APPLICATION_JSON_UTF8);
//        converter.setSupportedMediaTypes(list);
//        converter.write(responseBase, MediaType.APPLICATION_JSON, outputMessage);
//        shutdownResponse(response);
//    }
//
//    private void shutdownResponse(HttpServletResponse response) throws IOException {
//        response.getOutputStream().close();
//    }
//
//    @Bean
//    private HttpOutputMessage getHttpOutputMessage(HttpServletResponse response) {
//        if (outputMessage == null) {
//            return new ServletServerHttpResponse(response);
//        }
//        return outputMessage;
//    }
//
//}
