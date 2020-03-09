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
@Table(name = "d_city")
public class CityModel extends Sortable {
	private static final long serialVersionUID = 1L;

	//columns START
	@Column(name = "id")
	private Integer id;//"主键"

	@Column(name = "country_id")
	private Integer countryId;//"国家ID"

	@Column(name = "city_cn")
	private String cityCn;//"城市中文名称"

	@Column(name = "city_en")
	private String cityEn;//"城市英文名称"

	@Column(name = "code")
	private String code;//"城市代码值"

	@Column(name = "contact")
	private String contact;//"联系人名称"

	@Column(name = "email")
	private String email;//"email"

	@Column(name = "create_user_id")
	private Integer createUserId;//"创建人"

	@Column(name = "update_user_id")
	private Integer updateUserId;//"最后修改人"

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
		
	public void setCityCn(String cityCn) {
		this.cityCn = cityCn;
	}

	public String getCityCn() {
		return this.cityCn;
	}
		
	public void setCityEn(String cityEn) {
		this.cityEn = cityEn;
	}

	public String getCityEn() {
		return this.cityEn;
	}
		
	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
		
	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContact() {
		return this.contact;
	}
		
	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
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

}

