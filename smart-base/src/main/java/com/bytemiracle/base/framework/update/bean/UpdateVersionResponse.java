package com.bytemiracle.base.framework.update.bean;

/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/3/15 11:09
 */
public class UpdateVersionResponse {

    /**
     * Data : {"VersionName":null,"VersionCode":0}
     * Success : true
     * ErrorCode : 0
     * Msg : 当前系统版本已是最新
     */

    private DataDTO Data;
    private boolean Success;
    private int ErrorCode;
    private String Msg;

    public DataDTO getData() {
        return Data;
    }

    public void setData(DataDTO Data) {
        this.Data = Data;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int ErrorCode) {
        this.ErrorCode = ErrorCode;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public static class DataDTO {
        /**
         * VersionName : null
         * VersionCode : 0.0
         */

        private String VersionName;
        private int VersionCode;

        public String getVersionName() {
            return VersionName;
        }

        public void setVersionName(String VersionName) {
            this.VersionName = VersionName;
        }

        public int getVersionCode() {
            return VersionCode;
        }

        public void setVersionCode(int VersionCode) {
            this.VersionCode = VersionCode;
        }
    }
}
