package com.gameword.user.common.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 公用查询类
 * Created by majiancheng on 2019/12/22.
 */
public class QueryDto implements Serializable {

    private static final long serialVersionUID = 5152769885254938227L;

    private Integer id;//业务ID

    private Integer userId;//用户ID

    private String queryStr;//查询字符串

    private String nickName;//用户昵称

    private Integer sex;//性别

    private Integer cityId;//城市ID

    private String userDesc;//用户简介

    private Integer birthYear;//出生年

    private String mobilePhone;//手机号码

    private String password;//密码

    private String headImage;//个人照片

    private String content;//内容

    private Integer type;//类型

    private String verifyCode;//验证码

    private String originPassword;//原始密码

    private Integer language;//语种 1：中文，2：英文

    private String title;

    private String userName;

    private String email;

    private String other;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getQueryStr() {
        return queryStr;
    }

    public void setQueryStr(String queryStr) {
        this.queryStr = queryStr;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getOriginPassword() {
        return originPassword;
    }

    public void setOriginPassword(String originPassword) {
        this.originPassword = originPassword;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
