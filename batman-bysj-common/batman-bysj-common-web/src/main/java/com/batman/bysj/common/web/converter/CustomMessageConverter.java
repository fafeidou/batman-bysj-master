package com.batman.bysj.common.web.converter;

import com.alibaba.fastjson.JSON;
import com.batman.bysj.common.web.domin.WebApiResponse;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author victor.qin
 * @date 2018/5/25 12:32
 */
public class CustomMessageConverter implements HttpMessageConverter {
    @Override
    public boolean canRead(Class clazz, MediaType mediaType) {
        return true;
    }

    @Override
    public boolean canWrite(Class clazz, MediaType mediaType) {
        return true;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Arrays.asList(MediaType.APPLICATION_JSON, new MediaType("application", "*+json"));
    }

    @Override
    public Object read(Class clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    public void write(Object o, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        outputMessage.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        outputMessage.getBody().write(JSON.toJSONBytes(WebApiResponse.success(o)));
    }

//    /**
//     * Construct a new {@link CustomMessageConverter} using default configuration
//     * provided by {@link Jackson2ObjectMapperBuilder}.
//    public CustomMessageConverter() {
//        this(Jackson2ObjectMapperBuilder.json().build());
//    }
//
//    protected CustomMessageConverter(ObjectMapper objectMapper) {
//        super(objectMapper);
//    }
//
//
//    @Override
//    protected void writePrefix(JsonGenerator generator, Object object) throws IOException {
//        object = WebApiResponse.success(object);
//        super.writePrefix(generator, object);
//    }
//
//    @Override
//    protected void writeSuffix(JsonGenerator generator, Object object) throws IOException {
//        object = WebApiResponse.success(object);
//        super.writeSuffix(generator, object);
//    }*/
}




