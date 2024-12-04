package com.catface_task.common.vo.request;


import lombok.Data;

/**
 * @className: TaskSelectRecentVo
 * @author: Havoc.
 * @date: 2024/12/2 10:35
 * @version: 0.1
 * @description:
 */
@Data
public class TaskSelectRecentVo {
    // 基本查询状态信息。
    private Integer num;
    private Integer skip;

    /**
     * 搜索模式;
     */
    private String mode = "";

    /**
     * POI
     */
    private Float latitude;
    private Float longitude;
    private Integer radius = 1000;  // 默认搜索半径

    /**
     * Department
     */
    private String department;

    /**
     * keywords
     */
    private String keywords;
}
