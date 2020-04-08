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
@Table(name = "d_station")
public class StationModel extends Sortable {
	private static final long serialVersionUID = 1L;

	//columns START
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;//"主键"

	@Column(name = "country_id")
	private Integer countryId;//"国家ID"

	@Column(name = "city_id")
	private Integer cityId;//"城市ID"

	@Column(name = "status")
	private Integer status;//"状态"

	@Column(name = "cn_city_info")
	private String cnCityInfo;//"中文城市信息"

	@Column(name = "en_city_info")
	private String enCityInfo;//"英文城市信息"

	@Column(name = "cn_business_cooperation")
	private String cnBusinessCooperation;//"中文商务合作"

	@Column(name = "en_business_cooperation")
	private String enBusinessCooperation;//"英文商务合作"

	@Column(name = "create_user_id")
	private Integer createUserId;//"创建人"

	@Column(name = "update_user_id")
	private Integer updateUserId;//"最后修改人"

	@Column(name = "is_del")
	private Integer isDel;

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
		
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public Integer getCountryId() {
		return this.countryId;
	}
		
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getCityId() {
		return this.cityId;
	}
		
	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}
		
	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public Integer getCreateUserId() {
		return this.createUserId;
	}
		
	public void setUpdateUserId(Integer updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Integer getUpdateUserId() {
		return this.updateUserId;
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

	public String getCnCityInfo() {
		return cnCityInfo;
	}

	public void setCnCityInfo(String cnCityInfo) {
		this.cnCityInfo = cnCityInfo;
	}

	public String getEnCityInfo() {
		return enCityInfo;
	}

	public void setEnCityInfo(String enCityInfo) {
		this.enCityInfo = enCityInfo;
	}

	public String getCnBusinessCooperation() {
		return cnBusinessCooperation;
	}

	public void setCnBusinessCooperation(String cnBusinessCooperation) {
		this.cnBusinessCooperation = cnBusinessCooperation;
	}

	public String getEnBusinessCooperation() {
		return enBusinessCooperation;
	}

	public void setEnBusinessCooperation(String enBusinessCooperation) {
		this.enBusinessCooperation = enBusinessCooperation;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}
}

