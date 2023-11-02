package com.qnkj.core.webconfigs.endpoint;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author Oldhand
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@interface WebEndPoint {
    @AliasFor(annotation = Component.class)
    String value() default "";
}
