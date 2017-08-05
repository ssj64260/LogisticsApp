package com.msqsoft.aboutapp.model;

import java.io.Serializable;

/**
 * {
 * "name": "A+物流",
 * "version": "48",
 * "changelog": "",
 * "updated_at": 1493369773,
 * "versionShort": "0.0.4",
 * "build": "48",
 * "installUrl": "http://download.fir.im/v2/app/install/5881b8c0959d691f5c00044c?download_token=d5677d075089d9b7381578dd37d6bae1&source=update",
 * "install_url": "http://download.fir.im/v2/app/install/5881b8c0959d691f5c00044c?download_token=d5677d075089d9b7381578dd37d6bae1&source=update",
 * "direct_install_url": "http://download.fir.im/v2/app/install/5881b8c0959d691f5c00044c?download_token=d5677d075089d9b7381578dd37d6bae1&source=update",
 * "update_url": "http://fir.im/aboutapp",
 * "binary": {
 * "fsize": 6507664
 * }
 * }
 */

public class FirImBean implements Serializable {

    private String name;//APP名称
    private String version;//版本号
    private String changelog;//更改备注
    private long updated_at;//更新时间戳
    private String versionShort;//分级版本号
    private String build;
    private String installUrl;//更新url
    private String install_url;//更新url
    private String direct_install_url;//更新url
    private String update_url;//app fir url
    private Binary binary;//APP大小

    public class Binary implements Serializable {
        private long fsize;

        public long getFsize() {
            return fsize;
        }

        public void setFsize(long fsize) {
            this.fsize = fsize;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    public String getVersionShort() {
        return versionShort;
    }

    public void setVersionShort(String versionShort) {
        this.versionShort = versionShort;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getInstallUrl() {
        return installUrl;
    }

    public void setInstallUrl(String installUrl) {
        this.installUrl = installUrl;
    }

    public String getInstall_url() {
        return install_url;
    }

    public void setInstall_url(String install_url) {
        this.install_url = install_url;
    }

    public String getDirect_install_url() {
        return direct_install_url;
    }

    public void setDirect_install_url(String direct_install_url) {
        this.direct_install_url = direct_install_url;
    }

    public String getUpdate_url() {
        return update_url;
    }

    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }

    public Binary getBinary() {
        return binary;
    }

    public void setBinary(Binary binary) {
        this.binary = binary;
    }
}
