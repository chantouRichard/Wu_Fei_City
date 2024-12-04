package com.catface_task.controller;


import com.catface_task.common.vo.response.ResponseBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className: BaseCtroller
 * @author: Havoc.
 * @date: 2024/12/4 18:31
 * @version: 0.1
 * @description:
 */
@RestController
@RequestMapping
public class BaseController {

    /**
     * 用于在高资源消耗任务前，确定此服务的可用性。
     */
    @GetMapping("link_try")
    public ResponseBean linkTry() {
        return ResponseBean.success();
    }

}
