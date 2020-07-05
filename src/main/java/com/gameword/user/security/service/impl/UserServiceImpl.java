package com.gameword.user.security.service.impl;

import com.alibaba.fastjson.JSON;
import com.gameword.user.common.baseservice.impl.BaseService;
import com.gameword.user.common.utils.RongyunUtil;
import com.gameword.user.core.dao.UserMapper;
import com.gameword.user.core.model.UserModel;
import com.gameword.user.security.api.TokenController;
import com.gameword.user.security.service.IUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.rong.models.response.TokenResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * Created by majiancheng on 2019/9/16.
 */
@Service("userService")
public class UserServiceImpl extends BaseService<UserModel> implements IUserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RongyunUtil rongyunUtil;

    /**
     * 根据登录名获取用户信息
     *
     * @param loginName
     * @return
     */
    public UserModel findUserByLoginName(String loginName) {
        return userMapper.findUserByLoginName(loginName);
    }

    /**
     * 根据用户ID获取登录名
     *
     * @param userId
     * @return
     */
    public String findLoginNameByUserId(Integer userId) {
        return userMapper.findLoginNameByUserId(userId);
    }

    public void updateLastLoginInfoByUserName(String username, Date date, String remoteAddr) {
        userMapper.updateLastLoginInfoByUserName(username, date, remoteAddr);
    }

    @Override
    public PageInfo<UserModel> selectByFilterAndPage(UserModel userModel, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize, true, false, null);
        List<UserModel> list = selectByFilter(userModel);
        if(CollectionUtils.isNotEmpty(list)) {
            for (UserModel user : list) {
                user.setPassword("");
            }
        }
        return new PageInfo<>(list);
    }


    @Override
    public PageInfo<UserModel> selectByUserIds(List<Integer> userIds, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, true, false, null);
        Example example = new Example(UserModel.class);
        Example.Criteria criteria = example.createCriteria();

        if(CollectionUtils.isNotEmpty(userIds)) {
            criteria.andIn("id", userIds);
        }
        criteria.andEqualTo("enabled", 1);

        List<UserModel> list = getMapper().selectByExample(example);

        if(CollectionUtils.isNotEmpty(list)) {
            for (UserModel user : list) {
                user.setPassword("");
            }
        }
        return new PageInfo<>(list);
    }

    @Override
    public List<UserModel> selectByFilter(UserModel userModel) {
        Example example = new Example(UserModel.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(userModel.getMobilePhone())) {
            criteria.andEqualTo("mobilePhone", userModel.getMobilePhone());
        }
        if (StringUtils.isNotEmpty(userModel.getNickName())) {
            criteria.andLike("nickName", "%" + userModel.getNickName() + "%");
        }
        if (StringUtils.isNotEmpty(userModel.getUserName())) {
            criteria.andLike("userName", "%" + userModel.getUserName() + "%");
        }
        if (CollectionUtils.isNotEmpty(userModel.getUserIds())) {
            criteria.andIn("id", userModel.getUserIds());
        }
        if (StringUtils.isNotEmpty(userModel.getSortWithOutOrderBy())) {
            example.setOrderByClause(userModel.getSortWithOutOrderBy());
        }
        return getMapper().selectByExample(example);
    }

    @Override
    public UserModel findUserByUserId(int userId) {
        UserModel userModel = userMapper.findUserByUserId(userId);
        return userModel;
    }

    @Override
    public Map<Integer, UserModel> findUserMapByUserIds(List<Integer> userIds) {
        if(CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyMap();
        }
        List<UserModel> userModels = userMapper.find(Collections.singletonMap("userIds", userIds));
        if(CollectionUtils.isEmpty(userModels)) {
            return Collections.emptyMap();
        }

        Map<Integer, UserModel> userModelMap = new HashMap<Integer, UserModel>();
        for(UserModel userModel : userModels) {
            userModelMap.put(userModel.getId(), userModel);
        }

        return userModelMap;
    }

    public List<UserModel> findByUserIds(List<Integer> userIds) {
        if(CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        return userMapper.find(Collections.singletonMap("userIds", userIds));
    }


    public List<UserModel> find(Map<String, Object> param) {
        return userMapper.find(param);
    }


    public UserModel findUserByMobilePhone(String mobilePhone) {
        if(StringUtils.isEmpty(mobilePhone)) {
            return null;
        }

        return userMapper.findUserByMobilePhone(mobilePhone);
    }


    public UserModel findUserByEmail(String email) {
        if(StringUtils.isEmpty(email)) {
            return null;
        }

        return userMapper.findUserByEmail(email);
    }

    /**
     * 生成融云Token
     *
     * @param userId
     * @param nickName
     * @param headImage
     * @return
     */
    public String generRongyunToken(Integer userId, String nickName, String headImage) {

        //生成融云token
        TokenResult tokenResult = rongyunUtil.register(userId, nickName, headImage);

        System.out.println(String.format("tokenResult: %s.", JSON.toJSONString(tokenResult)));
        if(tokenResult.getCode() == 200) {
            UserModel userModel = new UserModel();
            userModel.setId(userId);
            userModel.setRongyunToken(tokenResult.getToken());

            this.updateNotNull(userModel);

            logger.info("生成融云token: userId:{}, nickName:{}, headImage:{}, rongyunToken:{}.", userId, nickName,
                    headImage, tokenResult.getToken());

            return tokenResult.getToken();
        } else {
            System.out.println(String.format("生成融云token异常: userId:%d, nickName:%s, headImage:%s, tokenResult:%s.", userId, nickName,
                    headImage, JSON.toJSONString(tokenResult)));
        }

        return "";
    }


    public UserModel findUserByThirdInfo(String thirdId, Integer thirdType) {
        if(StringUtils.isEmpty(thirdId) || thirdType == null) {
            return null;
        }
        String columnName = "";
        if(thirdType == 1) {
            columnName = "wechat_id";
        } else if(thirdType == 2) {
            columnName = "qq_id";
        }

        if(StringUtils.isEmpty(columnName)) {
            return null;
        }

        return userMapper.findUserByThirdInfo(thirdId, columnName);
    }


    public UserModel findById(Integer id) {
        return userMapper.findById(id);
    }


    public List<Integer> findByLabelName(String labelName) {
        return userMapper.findByLabelName(labelName);
    }

    public List<Integer> findByNickName(String nickName) {
        return userMapper.findByNickName(nickName);
    }
}
