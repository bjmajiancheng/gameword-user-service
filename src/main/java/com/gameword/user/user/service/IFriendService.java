/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.gameword.user.user.service;

import com.gameword.user.common.baseservice.IBaseService;
import com.gameword.user.user.model.FriendModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.*;
import com.gameword.user.user.dao.*;
import com.gameword.user.user.service.*;

/**
 * @author  davdian
 * @version 1.0
 * @since 1.0
 */

public interface IFriendService extends IBaseService<FriendModel> {

	public FriendModel findById(Integer id);

	public List<FriendModel> find(Map<String, Object> param);

	public PageInfo<FriendModel> selectByFilterAndPage(FriendModel friendModel, int pageNum, int pageSize);

	public List<FriendModel> selectByFilter(FriendModel friendModel);

	public Map<String, List<FriendModel>> generKeyWordFriendMap(List<FriendModel> friendModels);

	public int updateNoteName(Integer userId, Integer friendUserId, String noteName);

    public Map<Integer, FriendModel> findMapByUserId(Integer userId);
}
