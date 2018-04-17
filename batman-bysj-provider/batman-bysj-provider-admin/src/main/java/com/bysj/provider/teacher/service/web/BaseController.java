package com.bysj.provider.teacher.service.web;

import org.apache.poi.util.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * 通用 Controller 的基类
 * Created on 2015-04-29 15:29:52
 *
 * @author Jonas
 * @version 2.0.0
 */
public abstract class BaseController  {

    /**
     * 从指定位置的文件，生成下载内容
     */
    protected ResponseEntity<byte[]> fileToByteArrayResponse(String downloadFileName, String srcFile) {
        try {
            return toByteArrayResponse(downloadFileName, new FileInputStream(srcFile));
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    /**
     * 从字符串生成下载内容
     */
    protected ResponseEntity<byte[]> toByteArrayResponse(String downloadFileName, String content) {
        return toResponse(downloadFileName, content.getBytes());
    }

    /**
     * 从指定的文件流，生成下载内容
     */
    protected ResponseEntity<byte[]> toByteArrayResponse(String downloadFileName, InputStream inputStream) {
        try {
            return toResponse(downloadFileName, IOUtils.toByteArray(inputStream));
        } catch (IOException e) {
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    /**
     * 生成下载内容
     */
    protected ResponseEntity<byte[]> toResponse(String downloadFileName, byte[] bytes) {
        return toResponse(MediaType.APPLICATION_OCTET_STREAM, downloadFileName, bytes);
    }

    /**
     * 生成下载内容
     */
    protected ResponseEntity<byte[]> toResponse(MediaType mediaType, String downloadFileName, byte[] bytes) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        try {
            headers.setContentDispositionFormData("attachment", new String(downloadFileName.getBytes("gb2312"), "iso8859-1"));
        } catch (UnsupportedEncodingException e) {
        }
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }


}