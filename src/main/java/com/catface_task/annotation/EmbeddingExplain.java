package com.catface_task.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author catface
 * @description: 用于标注需要嵌入的文本，帮助 bge 理解文本，得到更好的 embedding。
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EmbeddingExplain {
    String value() default "";
}
