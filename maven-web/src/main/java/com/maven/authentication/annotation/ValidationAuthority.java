package com.maven.authentication.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 验证权限
 * 
 * @author chenjian
 * @createDate 2019-09-09
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidationAuthority {

	boolean validationToken() default false;

	String[] value() default {};
}