package com.xlw.babyshop.model;

/**
 * Created by xinliwei on 2015/7/17.
 */
public class VersionInfoModel {
    private boolean isnew;  // 是否是新版本
    private String version; // 版本名称
    private boolean force;
    private String url;     // url

    public VersionInfoModel() {
    }

    public VersionInfoModel(boolean isnew, String version, boolean force, String url) {
        super();
        this.isnew = isnew;
        this.version = version;
        this.force = force;
        this.url = url;
    }

    public boolean isIsnew() {
        return isnew;
    }

    public void setIsnew(boolean isnew) {
        this.isnew = isnew;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
