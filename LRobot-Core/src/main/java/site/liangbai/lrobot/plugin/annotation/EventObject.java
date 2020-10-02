package site.liangbai.lrobot.plugin.annotation;

import site.liangbai.lrobot.event.HandlerListType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventObject {
    HandlerListType handler() default HandlerListType.NEW;
}
