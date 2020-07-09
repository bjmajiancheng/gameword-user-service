package com.gameword.user.user.dto;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import java.io.Serializable;

/**
 * @author: majiancheng
 * @create: 2020-07-05 21:41
 **/
public class ChatroomUserDto implements Serializable {

    private String noteName;

    private Integer userId;

    private String nickName;

    private String headImage;

    private Integer sex;

    private String agencyName;

    private String userDesc;

    private String cityCnName;

    private String cityEnName;

    private String countryCnName;

    private String countryEnName;

    private String countryFlag;

    private int isFriend;

    private int isBlock;

    private int isAdmin;

    public String getNoteName() {
        return noteName;
    }

    public ChatroomUserDto setNoteName(String noteName) {
        this.noteName = noteName;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public ChatroomUserDto setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public ChatroomUserDto setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getHeadImage() {
        return headImage;
    }

    public ChatroomUserDto setHeadImage(String headImage) {
        this.headImage = headImage;
        return this;
    }

    public Integer getSex() {
        return sex;
    }

    public ChatroomUserDto setSex(Integer sex) {
        this.sex = sex;
        return this;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public ChatroomUserDto setAgencyName(String agencyName) {
        this.agencyName = agencyName;
        return this;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public ChatroomUserDto setUserDesc(String userDesc) {
        this.userDesc = userDesc;
        return this;
    }

    public String getCityCnName() {
        return cityCnName;
    }

    public ChatroomUserDto setCityCnName(String cityCnName) {
        this.cityCnName = cityCnName;
        return this;
    }

    public String getCityEnName() {
        return cityEnName;
    }

    public ChatroomUserDto setCityEnName(String cityEnName) {
        this.cityEnName = cityEnName;
        return this;
    }

    public String getCountryCnName() {
        return countryCnName;
    }

    public ChatroomUserDto setCountryCnName(String countryCnName) {
        this.countryCnName = countryCnName;
        return this;
    }

    public String getCountryEnName() {
        return countryEnName;
    }

    public ChatroomUserDto setCountryEnName(String countryEnName) {
        this.countryEnName = countryEnName;
        return this;
    }

    public String getCountryFlag() {
        return countryFlag;
    }

    public ChatroomUserDto setCountryFlag(String countryFlag) {
        this.countryFlag = countryFlag;
        return this;
    }

    public Integer getIsFriend() {
        return isFriend;
    }

    public ChatroomUserDto setIsFriend(int isFriend) {
        this.isFriend = isFriend;
        return this;
    }

    public Integer getIsBlock() {
        return isBlock;
    }

    public ChatroomUserDto setIsBlock(int isBlock) {
        this.isBlock = isBlock;
        return this;
    }

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public ChatroomUserDto setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
        return this;
    }
}
