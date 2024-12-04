package com.catface_task.common.constants;

import lombok.Getter;

/**
 * @author wy.huang
 * @description 返回状态码
 * @date 2020/5/6 11:40
 */

/**
 * @author : zhangxiaobo
 * @version : v1.0
 * @description : 一句话描述该类的功能
 * @createTime : 2023/4/17 8:03
 */
@Getter
public enum ResultCode {

    /**
     * 默认值
     */
    SUCCESS(200L, "操作成功"),
    FAILED(1001L, "响应失败"),
    VALIDATE_FAILED(1002L, "参数校验失败"),
    ERROR_TIMESTAMP(1003L, "时间戳不合法"),
    ERROR_DECODE(1004L, "加密信息错误"),
    ERROR(5000L, "未知错误"),
    TIMEOUT(408L, "服务器繁忙"),
    SERVER_ERROR(408L, "服务器异常"),

    FEEDBACK_SEND_ERROR(10201L, "反馈发送失败"),
    MISS_CONTENT(10202L, "缺少content"),
    EXCEL_FILE_ERROR(10203L, "表格文件不支持"),
    EXCEL_CONTENT_ERROR(10204L, "表格格式错误，请下载最新的表格模版"),
    DATA_CONVERT_ERROR(10205L, "数据转换失败"),

    /**
     * 数据库操作部分错误码
     * author hong chengzhi
     */
    DATABASE_ERROR(801L, "数据库操作异常"),
    PARAMETER_ERROR(804L, "参数错误"),
    INVALID_PARAMETER(805L, "不合法的参数"),
    MISS_PARAMETER(806L, "缺少参数"),
    REPEAT_RECORD(807L,"重复记录"),


    /**
     * 用户部分错误码
     */
    CAPTCHA_NOT_EXIST(10003L, "验证码不存在"),
    INVALID_CAPTCHA(10004L, "验证码错误或已失效"),
    /* ERROR_PASSWORD(10005L, "密码错误"), */
    EXIST_USER(10006L, "用户已存在"),
    USER_NOT_EXIST(10007L, "用户不存在"),
    ERROR_CAPTCHA(10008L, "验证码错误"),
    EMPTY_CLIENT_AUTH(10014L, "客户端验证信息不能为空"),
    ERROR_SEND_CAPTCHA(10016L, "验证码发送失败"),
    ERROR_MOBILE_NUMBER(10017L, "手机号码有误"),
    ERROR_USER_ROLE(10023L, "用户没有/已有对应角色"),
    TOO_LARGE_SIZE(10024L, "文件太大"),
    TOO_LONG_USERNAME(10028L, "用户名太长"),
    ASSIGN_ACCOUNT_ERROR(10029L,"分配账号出现问题"),
    PASSWORD_DECODE_ERROR(10030L,"密码解密出现错误"),
    // 10031已经有了，不能再往后了

    REQUEST_TOO_FAST_ERROR(10031L,"请求过快，稍等在请求"),


;
    private final Long code;
    private final String msg;

    ResultCode(Long code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
