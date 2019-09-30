/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.coolplay.system.core.dao;
import com.coolplay.system.core.model.CompanyUserRoleModel;

import java.util.*;

/**
 * @author  davdian
 * @version 1.0
 * @since 1.0
 */

public interface CompanyUserRoleMapper {

	public List<CompanyUserRoleModel> find(Map<String, Object> param);

}
