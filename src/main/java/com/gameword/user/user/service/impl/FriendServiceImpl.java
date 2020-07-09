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
import com.gameword.user.common.utils.Pinyin4jUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gameword.user.user.model.FriendModel;
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

@Service("friendService")
public class FriendServiceImpl extends BaseService<FriendModel> implements IFriendService{
	@Autowired
	private FriendMapper friendMapper;
	
	@Override
	public FriendModel findById(Integer id) {
		if(id == null) {
			return null;
		}
		return friendMapper.findById(id);
	}


	public List<FriendModel> find(Map<String, Object> param) {
		return friendMapper.find(param);
	}

	@Override
	public PageInfo<FriendModel> selectByFilterAndPage(FriendModel friendModel, int pageNum,
		int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<FriendModel> list = this.selectByFilter(friendModel);
		return new PageInfo<>(list);
	}

	@Override
	public List<FriendModel> selectByFilter(FriendModel friendModel) {
		Example example = new Example(FriendModel.class);
		Example.Criteria criteria = example.createCriteria();

		if(friendModel.getUserId() != null) {
			criteria.andEqualTo("userId", friendModel.getUserId());
		}

		if(friendModel.getFriendUserId() != null) {
			criteria.andEqualTo("friendUserId", friendModel.getFriendUserId());
		}

		if(StringUtils.isNotEmpty(friendModel.getSortWithOutOrderBy())) {
			example.setOrderByClause(friendModel.getSortWithOutOrderBy());
		}
		return getMapper().selectByExample(example);
	}


	public Map<String, List<FriendModel>> generKeyWordFriendMap(List<FriendModel> friendModels) {

		if(CollectionUtils.isEmpty(friendModels)) {
			return Collections.emptyMap();
		}

		Collections.sort(friendModels, (o1, o2) -> o1.getFriendNickName().toUpperCase().compareTo(o2.getFriendNickName().toUpperCase()));

		Map<String, List<FriendModel>> friendMap = new LinkedHashMap<String, List<FriendModel>>();
		for(FriendModel tmpFriend : friendModels) {
			String keyword = Pinyin4jUtil.getPinYinFirstChar(tmpFriend.getFriendNickName());

			List<FriendModel> tmpFriends = friendMap.getOrDefault(keyword, new ArrayList<FriendModel>());
			tmpFriends.add(tmpFriend);

			friendMap.put(keyword, tmpFriends);
 		}

		return friendMap;
	}


	public int updateNoteName(Integer userId, Integer friendUserId, String noteName) {
		if(userId == null || friendUserId == null) {
			return 0;
		}

		return friendMapper.updateNoteName(userId, friendUserId, noteName);
	}

	public Map<Integer, FriendModel> findMapByUserId(Integer userId) {
		if(userId == null) {
			return Collections.emptyMap();
		}

		Map<Integer, FriendModel> friendModelMap = new HashMap<>();
		List<FriendModel> friendModels = friendMapper.find(Collections.singletonMap("userId", userId));
		if (CollectionUtils.isNotEmpty(friendModels)) {
			friendModels.forEach(e -> friendModelMap.put(e.getFriendUserId(), e));
		}

		return friendModelMap;
	}
}
