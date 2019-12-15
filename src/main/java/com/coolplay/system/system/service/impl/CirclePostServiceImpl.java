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
import com.coolplay.system.system.model.CirclePostModel;
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

@Service("circlePostService")
public class CirclePostServiceImpl extends BaseService<CirclePostModel> implements ICirclePostService{
	@Autowired
	private CirclePostMapper circlePostMapper;
	
	@Override
	public CirclePostModel findById(Integer id) {
		if(id == null) {
			return null;
		}
		return circlePostMapper.findById(id);
	}


	public List<CirclePostModel> find(Map<String, Object> param) {
		return circlePostMapper.find(param);
	}

	@Override
	public PageInfo<CirclePostModel> selectByFilterAndPage(CirclePostModel circlePostModel, int pageNum,
		int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<CirclePostModel> list = this.selectByFilter(circlePostModel);
		return new PageInfo<>(list);
	}

	@Override
	public List<CirclePostModel> selectByFilter(CirclePostModel circlePostModel) {
		Example example = new Example(CirclePostModel.class);
		Example.Criteria criteria = example.createCriteria();

		if(StringUtils.isNotEmpty(circlePostModel.getSortWithOutOrderBy())) {
			example.setOrderByClause(circlePostModel.getSortWithOutOrderBy());
		}
		return getMapper().selectByExample(example);
	}


	public List<CirclePostModel> findByPostId(Integer postId) {
		if(postId == null || postId == 0) {
			return Collections.emptyList();
		}

		return circlePostMapper.findByPostId(postId);
	}

	public int updateTopByCirclePostInfo(List<Integer> circleIds, Integer postId, Integer isTop) {
		if(CollectionUtils.isEmpty(circleIds)) {
			return 0;
		}
		return circlePostMapper.updateTopByCirclePostInfo(circleIds, postId, isTop);
	}
}
