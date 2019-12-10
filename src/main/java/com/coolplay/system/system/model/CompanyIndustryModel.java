/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.coolplay.system.system.model;

import com.coolplay.system.common.handler.Sortable;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author  shawn
 * @version 1.0
 * @since 1.0
 */
@Table(name = "d_company_industry")
public class CompanyIndustryModel extends Sortable {
	private static final long serialVersionUID = 1L;

	//columns START
	@Column(name = "id")
	private Integer id;//"id"

	@Column(name = "industry_id")
	private Integer industryId;//"行业ID"

	@Column(name = "company_id")
	private Integer companyId;//"公司ID"

	@Column(name = "c_time")
	private Date ctime;//"创建时间"

	//columns END
		
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}
		
	public void setIndustryId(Integer industryId) {
		this.industryId = industryId;
	}

	public Integer getIndustryId() {
		return this.industryId;
	}
		
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getCompanyId() {
		return this.companyId;
	}
		
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Date getCtime() {
		return this.ctime;
	}

}

