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
import com.coolplay.system.core.dao.RoleFunctionMapper;
import com.coolplay.system.core.model.RoleFunctionModel;
import com.coolplay.system.core.service.ISystemRoleFunctionService;
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

@Service("systemRoleFunctionService")
public class SystemRoleFunctionServiceImpl extends BaseService<RoleFunctionModel> implements
		ISystemRoleFunctionService {
	@Autowired
	private RoleFunctionMapper roleFunctionMapper;
	

	public List<RoleFunctionModel> find(Map<String, Object> param) {
		return roleFunctionMapper.find(param);
	}

	@Override
	public PageInfo<RoleFunctionModel> selectByFilterAndPage(RoleFunctionModel systemRoleFunctionModel, int pageNum,
		int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<RoleFunctionModel> list = this.selectByFilter(systemRoleFunctionModel);
		return new PageInfo<>(list);
	}

	@Override
	public List<RoleFunctionModel> selectByFilter(RoleFunctionModel systemRoleFunctionModel) {
		Example example = new Example(RoleFunctionModel.class);
		Example.Criteria criteria = example.createCriteria();

		if(StringUtils.isNotEmpty(systemRoleFunctionModel.getSortWithOutOrderBy())) {
			example.setOrderByClause(systemRoleFunctionModel.getSortWithOutOrderBy());
		}
		return getMapper().selectByExample(example);
	}


	public int delByRoleId(int roleId) {
		return roleFunctionMapper.delByRoleId(roleId);
	}
}
