package com.cwt.common.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
@Component
public @interface PRCServer {
    Class<?> cls();
}
