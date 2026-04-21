package com.app.aop.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//어디에 붙일 수 있는 어노테이션 인가
@Target(ElementType.METHOD)
// 어느 시점에 실행 시킬거냐?(주입)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogStatus {;}
