package com.bytemiracle.base.framework.utils;

public interface ConstantsUtil {
    String APP_ASSISTANT_PASSWORD = "app_password";
    String APP_ASSISTANT_STATUS = "app_status";
    /**
     * 每个部门必须最少有一个负责人角色用户
     */
    String APP_USER_REQUIRED_ROLE = "负责人";
    /**
     * 部门type为1
     */
    String DEPT_TYPE_ONE = "1";
    /**
     * 部门type为2
     */
    String DEPT_TYPE_TWO = "2";
    /**
     * 行政区划字典
     */
    String APP_DIVISION_DICT = "app_division_dict";
    /**
     * 部门人员信息字典
     */
    String APP_DEPT_USER_INFO = "app_dept_user_info";

    /**
     * 绑定用户状态
     */
    String APP_THIS_MACHINE_BIND_USER_STATUS = "machine_bind_user_status";

    /**
     * 绑定用户状态
     */
    String APP_THIS_MACHINE_BIND_USER_INFO = "machine_bind_user_info";

    /**
     * token
     */
    String APP_THIS_MACHINE_TOKEN = "app_this_machine_token";

    /**
     * 本设备关联用户id
     */
    String APP_THIS_MACHINE_BIND_USER_ID = "machine_bind_user_id";

    /**
     * 本设备关联用户电话
     */
    String APP_THIS_MACHINE_BIND_USER_MOBILE = "machine_bind_user_mobile";

    /**
     * 本设备关联用户真实名称
     */
    String APP_THIS_MACHINE_BIND_USER_NICKNAME = "machine_bind_user_nickname";

    /**
     * 本设备关联账号信息
     */
    String THIS_MACHINE_BIND_ACCOUNT_INFO = "machine_bind_account_info";
    /**
     * rabbitMq hostIp地址,当本地测试时请改为 192.168.5.91
     */
    //String RABBIT_MQ_host_IP = "192.168.5.91";
    String RABBIT_MQ_host_IP = "8.136.105.51";

    /**
     * android 调用服务端ip地址,当为本地测试时请改为http://192.168.5.91:8880/api
     */
    // String SERVER_ADDRESS = "http://192.168.5.91:8880/api/";
    String SERVER_ADDRESS = "http://smart.bytemiracle.com:8081/api/";


    /**
     * 设备注册地址
     */
    String DEVICEMANAGER_IP = "http://8.136.105.51:5000";
}
