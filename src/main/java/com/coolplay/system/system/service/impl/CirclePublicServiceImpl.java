/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.coolplay.system.system.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.coolplay.system.common.baseservice.impl.BaseService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.coolplay.system.system.model.CirclePublicModel;
import tk.mybatis.mapper.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import com.coolplay.system.system.dao.*;
import com.coolplay.system.system.service.*;

/**
 * @author  davdian
 * @version 1.0
 * @since 1.0
 */

@Service("circlePublicService")
public class CirclePublicServiceImpl extends BaseService<CirclePublicModel> implements ICirclePublicService{
	@Autowired
	private CirclePublicMapper circlePublicMapper;
	
	@Override
	public CirclePublicModel findById(Integer id) {
		if(id == null) {
			return null;
		}
		return circlePublicMapper.findById(id);
	}


	public List<CirclePublicModel> find(Map<String, Object> param) {
		return circlePublicMapper.find(param);
	}

	@Override
	public PageInfo<CirclePublicModel> selectByFilterAndPage(CirclePublicModel circlePublicModel, int pageNum,
		int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<CirclePublicModel> list = this.selectByFilter(circlePublicModel);
		return new PageInfo<>(list);
	}

	@Override
	public List<CirclePublicModel> selectByFilter(CirclePublicModel circlePublicModel) {
		Example example = new Example(CirclePublicModel.class);
		Example.Criteria criteria = example.createCriteria();

		if(StringUtils.isNotEmpty(circlePublicModel.getSortWithOutOrderBy())) {
			example.setOrderByClause(circlePublicModel.getSortWithOutOrderBy());
		}
		return getMapper().selectByExample(example);
	}
}
