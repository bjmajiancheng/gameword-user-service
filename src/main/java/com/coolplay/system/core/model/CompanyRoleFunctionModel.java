/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.coolplay.system.core.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * @author  shawn
 * @version 1.0
 * @since 1.0
 */
@Table(name = "d_company_role_function")
public class CompanyRoleFunctionModel implements Serializable {
	private static final long serialVersionUID = 1L;

	//columns START
	@Column(name = "role_id")
	private Integer roleId;//"角色ID"

	@Column(name = "funcation_id")
	private Integer funcationId;//"菜单ID"
	//columns END
		
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getRoleId() {
		return this.roleId;
	}
		
	public void setFuncationId(Integer funcationId) {
		this.funcationId = funcationId;
	}

	public Integer getFuncationId() {
		return this.funcationId;
	}

}

