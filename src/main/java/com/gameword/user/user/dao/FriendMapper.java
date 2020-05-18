/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.gameword.user.user.dao;
import com.gameword.user.user.model.FriendModel;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.*;
import com.gameword.user.user.dao.*;
import com.gameword.user.user.service.*;

/**
 * @author  davdian
 * @version 1.0
 * @since 1.0
 */

public interface FriendMapper extends Mapper<FriendModel> {

	public List<FriendModel> find(Map<String, Object> param);

	public FriendModel findById(@Param("id") Integer id);

	public int updateNoteName(@Param("userId")Integer userId, @Param("friendUserId")Integer friendUserId,
			@Param("noteName")String noteName);
}
