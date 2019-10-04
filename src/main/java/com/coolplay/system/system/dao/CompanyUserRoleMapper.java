/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.coolplay.system.system.dao;
import com.coolplay.system.system.model.CompanyUserRoleModel;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.*;
import com.coolplay.system.system.dao.*;
import com.coolplay.system.system.service.*;

/**
 * @author  davdian
 * @version 1.0
 * @since 1.0
 */

public interface CompanyUserRoleMapper extends Mapper<CompanyUserRoleModel> {

	public List<CompanyUserRoleModel> find(Map<String, Object> param);

	public CompanyUserRoleModel findById(@Param("id") Integer id);

	public List<CompanyUserRoleModel> findByUserId(@Param("userId")Integer userId);
}
