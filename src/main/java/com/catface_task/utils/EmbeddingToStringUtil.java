package com.catface_task.utils;

import com.catface_task.annotation.EmbeddingExplain;

import java.lang.reflect.Field;

public class EmbeddingToStringUtil {

    /**
     * 相当于特化的 toString() 的效果。适配 bge-embedding 对中文文本的理解。
     * @param obj
     * @return
     */
    public static String toEmbeddingString(Object obj) {
        StringBuilder sb = new StringBuilder();
        sb.append(obj.getClass().getSimpleName()).append("{");
        // 遍历对象的所有字段，包括私有字段
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(EmbeddingExplain.class)) {
                try {
                    field.setAccessible(true); // 允许访问私有字段

                    EmbeddingExplain annotation = field.getAnnotation(EmbeddingExplain.class);
                    String fieldName = annotation.value().isEmpty() ? field.getName() : annotation.value();
                    Object value = field.get(obj);

                    // 检查字段是否为 String[]  // UPDATE 兼容更多数据类型
                    if (value instanceof String[]) {
                        String[] arrayValue = (String[]) value;
                        String arrayStr = String.join(", ", arrayValue);
                        sb.append(fieldName).append("：").append(arrayStr).append(", ");
                    } else {
                        sb.append(fieldName).append("：").append(value).append(", ");
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Remove the last ", "
        }
        sb.append("}");
        return sb.toString();
    }
}
