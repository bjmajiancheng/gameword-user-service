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
@Table(name = "d_advertisement")
public class AdvertisementModel extends Sortable {
	private static final long serialVersionUID = 1L;

	//columns START
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private java.lang.Integer id;//"主键"

	@Column(name = "platform")
	private java.lang.Integer platform;//"平台（全部、安卓、IOS）"

	@Column(name = "image")
	private java.lang.String image;//"开屏图片"

	@Column(name = "link")
	private java.lang.String link;//"链接"

	@Column(name = "status")
	private java.lang.Integer status;//"是否生效"

	@Column(name = "user_id")
	private java.lang.Integer userId;//"创建人"

	@Column(name = "c_time")
	private java.util.Date ctime;//"创建时间"

	@Column(name = "u_time")
	private java.util.Date utime;//"更新时间"

	//columns END
		
	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.Integer getId() {
		return this.id;
	}
		
	public void setPlatform(java.lang.Integer platform) {
		this.platform = platform;
	}

	public java.lang.Integer getPlatform() {
		return this.platform;
	}
		
	public void setImage(java.lang.String image) {
		this.image = image;
	}

	public java.lang.String getImage() {
		return this.image;
	}
		
	public void setLink(java.lang.String link) {
		this.link = link;
	}

	public java.lang.String getLink() {
		return this.link;
	}
		
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}

	public java.lang.Integer getStatus() {
		return this.status;
	}
		
	public void setUserId(java.lang.Integer userId) {
		this.userId = userId;
	}

	public java.lang.Integer getUserId() {
		return this.userId;
	}
		
	public void setCtime(java.util.Date ctime) {
		this.ctime = ctime;
	}

	public java.util.Date getCtime() {
		return this.ctime;
	}
		
	public void setUtime(java.util.Date utime) {
		this.utime = utime;
	}

	public java.util.Date getUtime() {
		return this.utime;
	}

}

