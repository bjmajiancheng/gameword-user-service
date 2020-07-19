/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.gameword.user.user.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gameword.user.common.baseservice.impl.BaseService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gameword.user.user.model.RegionModel;
import tk.mybatis.mapper.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import com.gameword.user.user.dao.*;
import com.gameword.user.user.service.*;

/**
 * @author  davdian
 * @version 1.0
 * @since 1.0
 */

@Service("regionService")
public class RegionServiceImpl extends BaseService<RegionModel> implements IRegionService{
	@Autowired
	private RegionMapper regionMapper;
	
	@Override
	public RegionModel findById(Integer id) {
		if(id == null) {
			return null;
		}
		return regionMapper.findById(id);
	}


	public List<RegionModel> find(Map<String, Object> param) {
		return regionMapper.find(param);
	}

	@Override
	public PageInfo<RegionModel> selectByFilterAndPage(RegionModel regionModel, int pageNum,
		int pageSize) {
		PageHelper.startPage(pageNum, pageSize, true, false, null);
		List<RegionModel> list = this.selectByFilter(regionModel);
		return new PageInfo<>(list);
	}

	@Override
	public List<RegionModel> selectByFilter(RegionModel regionModel) {
		Example example = new Example(RegionModel.class);
		Example.Criteria criteria = example.createCriteria();

		if(StringUtils.isNotEmpty(regionModel.getSortWithOutOrderBy())) {
			example.setOrderByClause(regionModel.getSortWithOutOrderBy());
		}
		return getMapper().selectByExample(example);
	}
}
