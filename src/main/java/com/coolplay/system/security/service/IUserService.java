package com.coolplay.system.security.service;

import com.coolplay.system.common.baseservice.IBaseService;
import com.coolplay.system.core.model.User;
import com.coolplay.system.security.dto.FunctionDto;

import java.util.List;

/**
 * Created by majiancheng on 2019/9/16.
 */
public interface IUserService extends IBaseService<User> {

    /**
     * 根据登录名获取用户信息
     *
     * @param loginName
     * @return
     */
    public User findUserByLoginName(String loginName);

    /**
     * 根据用户ID获取用户角色
     *
     * @param userId
     * @return
     */
    List<Integer> findUserRoleByUserId(int userId);

    /**
     * 根据登录名获取用户权限信息
     *
     * @param loginName
     * @return
     */
    public List<FunctionDto> findUserFunctionByLoginName(String loginName);

    /**
     * 根据用户ID获取登录名
     *
     * @param userId
     * @return
     */
    public String findLoginNameByUserId(Integer userId);
}
