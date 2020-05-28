package com.wym;

import com.wym.common.response.Response;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.lang.reflect.Type;


public class JackHttpMessageConvert extends MappingJackson2HttpMessageConverter {

    @Override
    protected boolean supports(Class<?> clazz) {
        if (clazz.isAssignableFrom(Response.class)) {
            return true;
        }
        return super.supports(clazz);
    }

    @Override
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        if (mediaType.includes(MediaType.APPLICATION_JSON)) {
            return true;
        }
        return super.canRead(type, contextClass, mediaType);
    }
}
