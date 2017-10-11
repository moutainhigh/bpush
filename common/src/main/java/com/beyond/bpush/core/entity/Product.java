package com.beyond.bpush.core.entity;

import org.msgpack.annotation.MessagePackMessage;

/**
 * 定义应用产品.
 * Created by admin on 14-8-6.
 */
@MessagePackMessage
public class Product {

    /**
     * 产品应用唯一标示
     */
    private Integer id;
    /**
     * 产品显示名称
     */
    private String title;
    /**
     * 产品客户端唯一码
     */
    private String appKey;
    /**
     * 产品客户端信息加密串
     */
    private String secret;
    /**
     * 客户端类型
     */
    private Integer clientTypeid;
    /**
     * iOS APNS生产证书路径
     */
    private String certPath;
    private String certPass;
    /**
     * iOS APNS开发证书路径
     */
    private String devCertPath;
    private String devCertPass;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getDevCertPath() {
        return devCertPath;
    }

    public void setDevCertPath(String devCertPath) {
        this.devCertPath = devCertPath;
    }

    public Integer getClientTypeid() {
        return clientTypeid;
    }

    /**
     * @see com.beyond.bpush.core.entity.ClientType
     * @param clientTypeid
     */
    public void setClientTypeid(Integer clientTypeid) {
        this.clientTypeid = clientTypeid;
    }

    public String getCertPass() {
        return certPass;
    }

    public void setCertPass(String certPass) {
        this.certPass = certPass;
    }

    public String getDevCertPass() {
        return devCertPass;
    }

    public void setDevCertPass(String devCertPass) {
        this.devCertPass = devCertPass;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof Product){
            Product o = (Product)obj;
            return o.getId().equals(this.getId());
        }
        return false;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
