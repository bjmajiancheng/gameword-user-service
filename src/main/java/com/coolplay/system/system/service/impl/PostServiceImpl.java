/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.coolplay.system.system.service.impl;

import java.util.List;

import com.coolplay.system.common.baseservice.impl.BaseService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.coolplay.system.system.model.PostModel;
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

@Service("postService")
public class PostServiceImpl extends BaseService<PostModel> implements IPostService{

	@Autowired
	private PostMapper postMapper;

	@Autowired
	private PostLabelMapper postLabelMapper;

	@Autowired
	private AppUserMapper appUserMapper;
	
	@Override
	public PostModel findById(Integer id) {
		if(id == null) {
			return null;
		}
		return postMapper.findById(id);
	}


	public List<PostModel> find(Map<String, Object> param) {
		return postMapper.find(param);
	}

	@Override
	public PageInfo<PostModel> selectByFilterAndPage(PostModel postModel, int pageNum,
		int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<PostModel> list = this.selectByFilter(postModel);
		return new PageInfo<>(list);
	}

	@Override
	public List<PostModel> selectByFilter(PostModel postModel) {
		Example example = new Example(PostModel.class);
		Example.Criteria criteria = example.createCriteria();

		if(StringUtils.isNotEmpty(postModel.getPostTitle())) {
			criteria.andLike("postTitle", "%" + postModel.getPostTitle() + "%");
		}

		if(postModel.getIsTop() != null) {
			criteria.andEqualTo("isTop", postModel.getIsTop());
		}

		if(postModel.getIsDel() != null) {
			criteria.andEqualTo("isDel", postModel.getIsDel());
		}

		if(postModel.getLabelId() != null) {
			List<Integer> postIds = postLabelMapper.getPostIdsByLabelId(postModel.getLabelId());

			criteria.andIn("id", postIds);
		}

		if(StringUtils.isNotEmpty(postModel.getPublicUserName())) {
			List<Integer> userIds = appUserMapper.getIdsByUserName("%"+postModel.getPublicUserName()+"%");
			if(CollectionUtils.isEmpty(userIds)) {
				userIds = Collections.singletonList(-1);
			}

			criteria.andIn("userId", userIds);
		}

		if(StringUtils.isNotEmpty(postModel.getSortWithOutOrderBy())) {
			example.setOrderByClause(postModel.getSortWithOutOrderBy());
		}
		return getMapper().selectByExample(example);
	}
}
