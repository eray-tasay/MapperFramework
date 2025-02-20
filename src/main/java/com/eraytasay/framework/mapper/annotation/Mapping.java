package com.eraytasay.framework.mapper.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Mappings.class)
@Target(ElementType.METHOD)
public @interface Mapping {
    String target();
    String source() default "";
    String fieldMapper() default "";
    boolean ignore() default false;
}
