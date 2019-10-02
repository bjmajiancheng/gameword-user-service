/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.coolplay.system.core.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.coolplay.system.common.baseservice.impl.BaseService;
import com.coolplay.system.core.dao.UserRoleMapper;
import com.coolplay.system.core.model.UserRoleModel;
import com.coolplay.system.core.service.ISystemUserRoleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * @author  davdian
 * @version 1.0
 * @since 1.0
 */

@Service("systemUserRoleService")
public class SystemUserRoleServiceImpl extends BaseService<UserRoleModel> implements ISystemUserRoleService {
	@Autowired
	private UserRoleMapper userRoleMapper;
	

	public List<UserRoleModel> find(Map<String, Object> param) {
		return userRoleMapper.find(param);
	}

	@Override
	public PageInfo<UserRoleModel> selectByFilterAndPage(UserRoleModel systemUserRoleModel, int pageNum,
		int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<UserRoleModel> list = this.selectByFilter(systemUserRoleModel);
		return new PageInfo<>(list);
	}

	@Override
	public List<UserRoleModel> selectByFilter(UserRoleModel systemUserRoleModel) {
		Example example = new Example(UserRoleModel.class);
		Example.Criteria criteria = example.createCriteria();

		if(StringUtils.isNotEmpty(systemUserRoleModel.getSortWithOutOrderBy())) {
			example.setOrderByClause(systemUserRoleModel.getSortWithOutOrderBy());
		}
		return getMapper().selectByExample(example);
	}


	public int deleteByUserId(int userId) {
		return userRoleMapper.deleteByUserId(userId);
	}
}
