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
@Table(name = "d_region")
public class RegionModel extends Sortable {
	private static final long serialVersionUID = 1L;

	//columns START
	@Column(name = "id")
	private Integer id;//"主键"

	@Column(name = "parent_id")
	private Integer parentId;//"父ID"

	@Column(name = "region_cn_name")
	private String regionCnName;//"中文名称"

	@Column(name = "region_en_name")
	private String regionEnName;//"英文名称"

	@Column(name = "region_type")
	private Boolean regionType;//"regionType"

	//columns END
		
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}
		
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getParentId() {
		return this.parentId;
	}
		
	public void setRegionCnName(String regionCnName) {
		this.regionCnName = regionCnName;
	}

	public String getRegionCnName() {
		return this.regionCnName;
	}
		
	public void setRegionEnName(String regionEnName) {
		this.regionEnName = regionEnName;
	}

	public String getRegionEnName() {
		return this.regionEnName;
	}
		
	public void setRegionType(Boolean regionType) {
		this.regionType = regionType;
	}

	public Boolean getRegionType() {
		return this.regionType;
	}

}

