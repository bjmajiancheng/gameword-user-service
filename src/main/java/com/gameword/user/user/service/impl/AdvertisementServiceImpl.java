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
import com.gameword.user.user.model.AdvertisementModel;
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

@Service("advertisementService")
public class AdvertisementServiceImpl extends BaseService<AdvertisementModel> implements IAdvertisementService{
	@Autowired
	private AdvertisementMapper advertisementMapper;
	
	@Override
	public AdvertisementModel findById(Integer id) {
		if(id == null) {
			return null;
		}
		return advertisementMapper.findById(id);
	}


	public List<AdvertisementModel> find(Map<String, Object> param) {
		return advertisementMapper.find(param);
	}

	@Override
	public PageInfo<AdvertisementModel> selectByFilterAndPage(AdvertisementModel advertisementModel, int pageNum,
		int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<AdvertisementModel> list = this.selectByFilter(advertisementModel);
		return new PageInfo<>(list);
	}

	@Override
	public List<AdvertisementModel> selectByFilter(AdvertisementModel advertisementModel) {
		Example example = new Example(AdvertisementModel.class);
		Example.Criteria criteria = example.createCriteria();

		if(StringUtils.isNotEmpty(advertisementModel.getSortWithOutOrderBy())) {
			example.setOrderByClause(advertisementModel.getSortWithOutOrderBy());
		}
		return getMapper().selectByExample(example);
	}
}
