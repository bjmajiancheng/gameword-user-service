/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.gameword.user.core.model;

import com.gameword.user.common.handler.Sortable;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 * @author  shawn
 * @version 1.0
 * @since 1.0
 */
@Table(name = "d_user")
public class UserModel extends Sortable {
	private static final long serialVersionUID = 1L;

	//columns START
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;//"主键"

	@Column(name = "user_name")
	private String userName;//"用户名"

	@Column(name = "password")
	private String password;//"密码"

	@Column(name = "mobile_phone")
	private String mobilePhone;//"手机号"

	@Column(name = "wechat_id")
	private String wechatId;//"微信ID"

	@Column(name = "qq_id")
	private String qqId;//"QQ ID"

	@Column(name = "nick_name")
	private String nickName;//"昵称"

	@Column(name = "user_desc")
	private String userDesc;//"用户简介"

	@Column(name = "head_image")
	private String headImage;//"头图"

	@Column(name = "province_id")
	private Integer provinceId;//"省份ID"

	@Column(name = "province_name")
	private String provinceName;//"省份名称"

	@Column(name = "city_id")
	private Integer cityId;//"城市ID"

	@Column(name = "city_name")
	private String cityName;//"城市名称"

	@Column(name = "birth_year")
	private Integer birthYear;//"出生年"

	@Column(name = "sex")
	private Integer sex;//"性别 1：男，2：女"

	@Column(name = "user_type")
	private Integer userType;//"用户类型 1：管理员，2：评委，3：城市合伙人，4：普通用户"

	@Column(name = "account_non_locked")
	private Boolean accountNonLocked;//"未锁定状态，0=锁定，1=正常"

	@Column(name = "account_non_expired")
	private Boolean accountNonExpired;//"账号过期状态，0=过期，1=正常"

	@Column(name = "credentials_non_expired")
	private Boolean credentialsNonExpired;//"密码失效状态：0：已失效 1：未失效"

	@Column(name = "last_login_ip")
	private String lastLoginIp;//"最后登录IP"

	@Column(name = "last_login_time")
	private Date lastLoginTime;//"最后登录时间"

	@Column(name = "enabled")
	private Boolean enabled;//"状态，0=冻结，1=正常"

	@Column(name = "last_password_reset")
	private Date lastPasswordReset;//"最后重置密码时间"

	@Column(name = "c_time")
	private Date ctime;//"创建时间"

	//columns END

	@Transient
	private String token;

	@Transient
	private String verifyCode;//验证码

	@Transient
	private String queryStr;//查询文本

	@Transient
	private List<Integer> userIds;//用户集合

	@Transient
	private String matchLogo;//赛事LOGO
		
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}
		
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return this.userName;
	}
		
	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}
		
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getMobilePhone() {
		return this.mobilePhone;
	}
		
	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}

	public String getWechatId() {
		return this.wechatId;
	}
		
	public void setQqId(String qqId) {
		this.qqId = qqId;
	}

	public String getQqId() {
		return this.qqId;
	}
		
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickName() {
		return this.nickName;
	}
		
	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}

	public String getUserDesc() {
		return this.userDesc;
	}
		
	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public String getHeadImage() {
		return this.headImage;
	}
		
	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getProvinceId() {
		return this.provinceId;
	}
		
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getProvinceName() {
		return this.provinceName;
	}
		
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getCityId() {
		return this.cityId;
	}
		
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityName() {
		return this.cityName;
	}
		
	public void setBirthYear(Integer birthYear) {
		this.birthYear = birthYear;
	}

	public Integer getBirthYear() {
		return this.birthYear;
	}
		
	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getSex() {
		return this.sex;
	}
		
	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getUserType() {
		return this.userType;
	}
		
	public void setAccountNonLocked(Boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public Boolean getAccountNonLocked() {
		return this.accountNonLocked;
	}
		
	public void setAccountNonExpired(Boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public Boolean getAccountNonExpired() {
		return this.accountNonExpired;
	}
		
	public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public Boolean getCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}
		
	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public String getLastLoginIp() {
		return this.lastLoginIp;
	}
		
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Date getLastLoginTime() {
		return this.lastLoginTime;
	}
		
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getEnabled() {
		return this.enabled;
	}
		
	public void setLastPasswordReset(Date lastPasswordReset) {
		this.lastPasswordReset = lastPasswordReset;
	}

	public Date getLastPasswordReset() {
		return this.lastPasswordReset;
	}
		
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Date getCtime() {
		return this.ctime;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getQueryStr() {
		return queryStr;
	}

	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}

	public List<Integer> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}

	public String getMatchLogo() {
		return matchLogo;
	}

	public void setMatchLogo(String matchLogo) {
		this.matchLogo = matchLogo;
	}
}

