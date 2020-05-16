/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.gameword.user.user.model;

import com.gameword.user.common.handler.Sortable;
import org.springframework.beans.factory.annotation.Autowired;

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
@Table(name = "d_company")
public class CompanyModel extends Sortable {
	private static final long serialVersionUID = 1L;

	//columns START
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;//"id"

	@Column(name = "city_id")
	private Integer cityId;//"城市ID"

	@Column(name = "company_logo")
	private String companyLogo;//"企业logo图片"

	@Column(name = "cn_name")
	private String cnName;//"中文名称"

	@Column(name = "en_name")
	private String enName;//"英文名称"

	@Column(name = "cn_desc")
	private String cnDesc;//"中文简介"

	@Column(name = "en_desc")
	private String enDesc;//"英文简介"

	@Column(name = "c_time")
	private Date ctime;//"创建时间"

	@Column(name = "u_time")
	private Date utime;//"更新时间"

	//columns END

	@Autowired
	private List<String> cnLabelNames;

	@Autowired
	private List<String> enLabelNames;

	@Autowired
	private String cityCnName;

	@Autowired
	private String cityEnName;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}
		
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getCityId() {
		return this.cityId;
	}
		
	public void setCompanyLogo(String companyLogo) {
		this.companyLogo = companyLogo;
	}

	public String getCompanyLogo() {
		return this.companyLogo;
	}
		
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getCnName() {
		return this.cnName;
	}
		
	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getEnName() {
		return this.enName;
	}
		
	public void setCnDesc(String cnDesc) {
		this.cnDesc = cnDesc;
	}

	public String getCnDesc() {
		return this.cnDesc;
	}
		
	public void setEnDesc(String enDesc) {
		this.enDesc = enDesc;
	}

	public String getEnDesc() {
		return this.enDesc;
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

	public List<String> getCnLabelNames() {
		return cnLabelNames;
	}

	public void setCnLabelNames(List<String> cnLabelNames) {
		this.cnLabelNames = cnLabelNames;
	}

	public List<String> getEnLabelNames() {
		return enLabelNames;
	}

	public void setEnLabelNames(List<String> enLabelNames) {
		this.enLabelNames = enLabelNames;
	}

	public void setCityCnName(String cityCnName) {
		this.cityCnName = cityCnName;
	}

	public String getCityCnName() {
		return cityCnName;
	}

	public void setCityEnName(String cityEnName) {
		this.cityEnName = cityEnName;
	}

	public String getCityEnName() {
		return cityEnName;
	}
}

