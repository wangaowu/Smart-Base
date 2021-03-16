package com.bytemiracle.base.framework.update;

/**
 * 类功能：升级的响应结构
 *
 * @author gwwang
 * @date 2021/3/10 11:18
 */
public class UpdateResponse {
    private String Msg;

    private Data Data;

    private String ErrorCode;

    private String Success;

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public Data getData() {
        return Data;
    }

    public void setData(Data Data) {
        this.Data = Data;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String ErrorCode) {
        this.ErrorCode = ErrorCode;
    }

    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String Success) {
        this.Success = Success;
    }

    @Override
    public String toString() {
        return "ClassPojo [Msg = " + Msg + ", Data = " + Data + ", ErrorCode = " + ErrorCode + ", Success = " + Success + "]";
    }

    public static class Data {
        private String Msg;

        private String VersionCode;

        private String UpdateStatus;

        private String ApkSize;

        private String ApkMd5;

        private String ModifyContent;

        private String VersionName;

        private String Code;

        private String VersionType;

        private String DownloadUrl;

        public String getMsg() {
            return Msg;
        }

        public void setMsg(String Msg) {
            this.Msg = Msg;
        }

        public String getVersionCode() {
            return VersionCode;
        }

        public void setVersionCode(String VersionCode) {
            this.VersionCode = VersionCode;
        }

        public String getUpdateStatus() {
            return UpdateStatus;
        }

        public void setUpdateStatus(String UpdateStatus) {
            this.UpdateStatus = UpdateStatus;
        }

        public String getApkSize() {
            return ApkSize;
        }

        public void setApkSize(String ApkSize) {
            this.ApkSize = ApkSize;
        }

        public String getApkMd5() {
            return ApkMd5;
        }

        public void setApkMd5(String ApkMd5) {
            this.ApkMd5 = ApkMd5;
        }

        public String getModifyContent() {
            return ModifyContent.replace("\t", "").replace(" ", "");
        }

        public void setModifyContent(String ModifyContent) {
            this.ModifyContent = ModifyContent;
        }

        public String getVersionName() {
            return VersionName;
        }

        public void setVersionName(String VersionName) {
            this.VersionName = VersionName;
        }

        public String getCode() {
            return Code;
        }

        public void setCode(String Code) {
            this.Code = Code;
        }

        public String getVersionType() {
            return VersionType;
        }

        public void setVersionType(String VersionType) {
            this.VersionType = VersionType;
        }

        public String getDownloadUrl() {
            return DownloadUrl;
        }

        public void setDownloadUrl(String DownloadUrl) {
            this.DownloadUrl = DownloadUrl;
        }

        @Override
        public String toString() {
            return "ClassPojo [Msg = " + Msg + ", VersionCode = " + VersionCode + ", UpdateStatus = " + UpdateStatus + ", ApkSize = " + ApkSize + ", ApkMd5 = " + ApkMd5 + ", ModifyContent = " + ModifyContent + ", VersionName = " + VersionName + ", Code = " + Code + ", VersionType = " + VersionType + ", DownloadUrl = " + DownloadUrl + "]";
        }
    }

}
