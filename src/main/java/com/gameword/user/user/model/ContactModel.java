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
import java.util.Map;
import javax.persistence.*;

/**
 * @author  shawn
 * @version 1.0
 * @since 1.0
 */
@Table(name = "d_contact")
public class ContactModel extends Sortable {
	private static final long serialVersionUID = 1L;

	//columns START
	@Column(name = "id")
	private Integer id;//"主键"

	@Column(name = "title")
	private String title;//"标题"

	@Column(name = "content")
	private String content;//"内容"

	@Column(name = "user_name")
	private String userName;//"本人姓名"

	@Column(name = "mobile_phone")
	private String mobilePhone;//"电话"

	@Column(name = "email")
	private String email;//"email"

	@Column(name = "other")
	private String other;//"其他"

	@Column(name = "user_id")
	private Integer userId;//"提交人"

	@Column(name = "c_time")
	private Date ctime;//"创建时间"

	@Column(name = "u_time")
	private Date utime;//"更新时间"

	//columns END
		
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}
		
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}
		
	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}
		
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return this.userName;
	}
		
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getMobilePhone() {
		return this.mobilePhone;
	}
		
	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}
		
	public void setOther(String other) {
		this.other = other;
	}

	public String getOther() {
		return this.other;
	}
		
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserId() {
		return this.userId;
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

}

