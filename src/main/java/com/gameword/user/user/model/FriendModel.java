/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.gameword.user.user.model;

import com.gameword.user.common.handler.Sortable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.*;

/**
 * @author  shawn
 * @version 1.0
 * @since 1.0
 */
@Table(name = "d_friend")
public class FriendModel extends Sortable {
	private static final long serialVersionUID = 1L;

	//columns START
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;//"主键"

	@Column(name = "user_id")
	private Integer userId;//"用户ID"

	@Column(name = "friend_user_id")
	private Integer friendUserId;//"好友ID"

	@Column(name = "note_name")
	private String noteName;//"备注名称"

	@Column(name = "c_time")
	private Date ctime;//"创建时间"

	@Column(name = "u_time")
	private Date utime;//"更新时间"
	//columns END

	@Transient
	private String friendNickName = "";//朋友昵称

	@Transient
	private String friendHeadImage = "";//朋友头图

	@Transient
	private String friendCityCnName = "";//城市中文名称

	@Transient
	private String friendCityEnName = "";//城市英文名称

	@Transient
	private String friendCountryCnName = "";//国家中文名称

	@Transient
	private String friendCountryEnName = "";//国家英文名称

	@Transient
	private String firstCharPinyin = "";//首字母

	@Transient
	private String friendCountryFlag = "";//国旗

	@Transient
	private String friendUserDesc = "";//个人简介

	@Transient
	private Integer friendSex = 0;//性别

	@Transient
	private String friendAgencyName = "";//学校机构

	@Transient
	private List<Integer> friendUserIds;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}
		
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserId() {
		return this.userId;
	}
		
	public void setFriendUserId(Integer friendUserId) {
		this.friendUserId = friendUserId;
	}

	public Integer getFriendUserId() {
		return this.friendUserId;
	}

	public String getNoteName() {
		return noteName;
	}

	public void setNoteName(String noteName) {
		this.noteName = noteName;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Date getCtime() {
		return this.ctime;
	}
		
	public void setUtime(Date utime) {
		this.utime = utime;
	}

	public Date getUtime() {
		return this.utime;
	}

	public String getFriendNickName() {
		return friendNickName;
	}

	public void setFriendNickName(String friendNickName) {
		this.friendNickName = friendNickName;
	}

	public String getFriendHeadImage() {
		return friendHeadImage;
	}

	public void setFriendHeadImage(String friendHeadImage) {
		this.friendHeadImage = friendHeadImage;
	}

	public String getFriendCityCnName() {
		return friendCityCnName;
	}

	public void setFriendCityCnName(String friendCityCnName) {
		this.friendCityCnName = friendCityCnName;
	}

	public String getFriendCityEnName() {
		return friendCityEnName;
	}

	public void setFriendCityEnName(String friendCityEnName) {
		this.friendCityEnName = friendCityEnName;
	}

	public String getFriendCountryCnName() {
		return friendCountryCnName;
	}

	public void setFriendCountryCnName(String friendCountryCnName) {
		this.friendCountryCnName = friendCountryCnName;
	}

	public String getFriendCountryEnName() {
		return friendCountryEnName;
	}

	public void setFriendCountryEnName(String friendCountryEnName) {
		this.friendCountryEnName = friendCountryEnName;
	}

	public String getFirstCharPinyin() {
		return firstCharPinyin;
	}

	public void setFirstCharPinyin(String firstCharPinyin) {
		this.firstCharPinyin = firstCharPinyin;
	}

	public String getFriendCountryFlag() {
		return friendCountryFlag;
	}

	public void setFriendCountryFlag(String friendCountryFlag) {
		this.friendCountryFlag = friendCountryFlag;
	}

	public String getFriendUserDesc() {
		return friendUserDesc;
	}

	public void setFriendUserDesc(String friendUserDesc) {
		this.friendUserDesc = friendUserDesc;
	}

	public Integer getFriendSex() {
		return friendSex;
	}

	public void setFriendSex(Integer friendSex) {
		this.friendSex = friendSex;
	}

	public String getFriendAgencyName() {
		return friendAgencyName;
	}

	public void setFriendAgencyName(String friendAgencyName) {
		this.friendAgencyName = friendAgencyName;
	}

	public void setFriendUserIds(List<Integer> friendUserIds) {
		this.friendUserIds = friendUserIds;
	}

	public List<Integer> getFriendUserIds() {
		return friendUserIds;
	}
}

