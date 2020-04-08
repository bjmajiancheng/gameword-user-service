/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.gameword.user.core.dao;
import com.gameword.user.core.model.UserModel;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.*;

/**
 * @author  davdian
 * @version 1.0
 * @since 1.0
 */

public interface UserMapper extends Mapper<UserModel> {

	public List<UserModel> find(Map<String, Object> param);

	public UserModel findById(@Param("id") Integer id);

	public UserModel findUserByLoginName(@Param("userName")String loginName);

	public String findLoginNameByUserId(@Param("userId")Integer userId);

	public void updateLastLoginInfoByUserName(@Param("userName")String username, @Param("lastLoginDate")Date date, @Param("remoteAddr")String remoteAddr);

	public UserModel findUserByUserId(@Param("userId")int userId);

	public UserModel findUserByMobilePhone(@Param("mobilePhone")String mobilePhone);

	public UserModel findUserByThirdInfo(@Param("thirdId")String thirdId, @Param("columnName")String columnName);

	public List<Integer> findByLabelName(@Param("labelName")String labelName);

	public List<Integer> findByNickName(@Param("nickName")String nickName);

	public UserModel findUserByEmail(@Param("email")String email);
}
