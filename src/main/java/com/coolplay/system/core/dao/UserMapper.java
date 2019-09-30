package com.coolplay.system.core.dao;

import com.coolplay.system.core.model.User;
import com.coolplay.system.security.dto.FunctionDto;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by majiancheng on 2019/9/16.
 */
public interface UserMapper extends Mapper<User> {

    User findUserByLoginName(@Param("loginName") String loginName);

    List<Integer> findUserRoleByUserId(@Param("userId")int userId);

    /**
     * 根据登录名获取用户权限信息
     *
     * @param loginName
     * @return
     */
    public List<FunctionDto> findUserFunctionByLoginName(@Param("loginName")String loginName);

    /**
     * 根据用户ID获取登录名
     *
     * @param userId
     * @return
     */
    public String findLoginNameByUserId(@Param("userId")Integer userId);
}
