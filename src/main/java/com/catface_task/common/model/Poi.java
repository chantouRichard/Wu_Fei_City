package com.catface_task.common.model;


import lombok.Data;

/**
 * @className: Poi
 * @author: Havoc.
 * @date: 2024/11/30 10:20
 * @version: 0.1
 * @description: // INFO 别扭，Java 如何像 Go 一样的 “组合概念”，还能适配 ORM；
 */
@Data
public class Poi {
    private Float latitude;
    private Float longitude;

    public enum DepartmentWHU {
        IT("信部", "it"),
        MEDICAL("医学部", "medical"),
        ENGINE("工部", "engine"),
        ART("文理", "art"),
        LAKE("湖滨", "lake"),
        MAPLE("枫园", "maple"),
        NET("网安", "net"),
        OTHER("其他", "other");

        private final String zh;
        private final String en;

        DepartmentWHU(String zh, String en) {
            this.zh = zh;
            this.en = en;
        }

        public String getZh() {
            return zh;
        }

        public String getEn() {
            return en;
        }
    }
}
