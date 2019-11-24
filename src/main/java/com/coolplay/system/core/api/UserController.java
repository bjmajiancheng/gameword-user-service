package com.coolplay.system.core.api;

import com.coolplay.system.common.utils.PageConvertUtil;
import com.coolplay.system.common.utils.ResponseUtil;
import com.coolplay.system.common.utils.Result;
import com.coolplay.system.core.model.RoleModel;
import com.coolplay.system.core.model.UserModel;
import com.coolplay.system.core.model.UserRoleModel;
import com.coolplay.system.core.service.ISystemUserRoleService;
import com.coolplay.system.security.security.CoolplayUserCache;
import com.coolplay.system.security.security.SecurityUser;
import com.coolplay.system.security.service.IRoleService;
import com.coolplay.system.security.service.IUserService;
import com.coolplay.system.security.utils.SecurityUtil;
import com.coolplay.system.system.model.UserPassMappingModel;
import com.coolplay.system.system.service.IUserPassMappingService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by majiancheng on 2019/9/30.
 */
@Controller
@RequestMapping("/api/core/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private CoolplayUserCache coolplayUserCache;

    @Autowired
    private ISystemUserRoleService systemUserRoleService;

    @Autowired
    private IUserPassMappingService userPassMappingService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map list(UserModel userModel,
            @RequestParam(value = "page", required = false, defaultValue = "1") int pageNum,
            @RequestParam(value = "rows", required = false, defaultValue = "15") int pageSize) {

        PageInfo<UserModel> pageInfo = userService.selectByFilterAndPage(userModel, pageNum, pageSize);
        if(CollectionUtils.isNotEmpty(pageInfo.getList())) {
            List<Integer> userIds = new ArrayList<Integer>(pageInfo.getList().size());
            for(UserModel tmpUserModel : pageInfo.getList()) {
                userIds.add(tmpUserModel.getId());
            }

            Map<Integer, List<UserRoleModel>> userRoleModelMap = roleService.findUserRoles(userIds);
            for(UserModel tmpUserModel : pageInfo.getList()) {
                List<UserRoleModel> userRoleModels = userRoleModelMap.get(tmpUserModel.getId());
                StringBuffer sb = new StringBuffer();
                if(CollectionUtils.isNotEmpty(userRoleModels)) {
                    for(UserRoleModel userRoleModel : userRoleModels) {
                        if(sb.length() > 0) {
                            sb.append("、");
                        }
                        sb.append(userRoleModel.getRoleName());
                    }
                }

                tmpUserModel.setRoleName(sb.toString());
            }
        }


        return PageConvertUtil.grid(pageInfo);
    }

    @ResponseBody
    @RequestMapping(value="/getUser", method = RequestMethod.GET)
    public Result getUser(@RequestParam("userId") int userId) {
        UserModel userModel = userService.findUserByUserId(userId);
        List<UserRoleModel> userRoleModels = userService.selectUserRoleByUserId(userId);

        if(StringUtils.isNotEmpty(userModel.getPassword())) {
            UserPassMappingModel passMapping = userPassMappingService.findByPasswordEncode(userModel.getPassword());
            if(passMapping != null) {
                userModel.setPassword(passMapping.getPassword());
            }
        }

        if(CollectionUtils.isNotEmpty(userRoleModels)) {
            List<Integer> roleIds = new ArrayList<Integer>();
            StringBuffer sb = new StringBuffer();
            for(UserRoleModel userRoleModel : userRoleModels) {
                roleIds.add(userRoleModel.getRoleId());

                if(sb.length() > 0) {
                    sb.append("、");
                }
                RoleModel roleModel = roleService.selectById(userRoleModel.getRoleId());
                if(roleModel != null) {
                    sb.append(roleModel.getRoleName());
                }
            }
            userModel.setRoleIds(roleIds);
            userModel.setRoleName(sb.toString());
        }

        return ResponseUtil.success(userModel);
    }

    @ResponseBody
    @RequestMapping(value="/updateUserEnabled", method = RequestMethod.GET)
    public Result updateUserEnabled(@RequestParam("userId") int userId, @RequestParam("enabled") int enabled) {
        UserModel userModel = new UserModel();
        userModel.setId(userId);
        userModel.setEnabled(enabled);
        int updateCnt = userService.updateNotNull(userModel);

        coolplayUserCache.removeUserFromCacheByUserId(userId);

        return ResponseUtil.success();
    }

    @ResponseBody
    @RequestMapping(value="/updateUser", method = RequestMethod.POST)
    public Result updateUser(UserModel userModel) {

        if(StringUtils.isNotEmpty(userModel.getPassword())) {
            String passwordEncode = SecurityUtil.encodeString(userModel.getPassword());

            UserPassMappingModel userPassMappingModel = new UserPassMappingModel();
            userPassMappingModel.setPassword(userModel.getPassword());
            userPassMappingModel.setPasswordEncode(passwordEncode);
            userPassMappingService.saveNotNull(userPassMappingModel);
            userModel.setPassword(passwordEncode);
        }

        int updateCnt = userService.updateNotNull(userModel);

        int delCnt = systemUserRoleService.deleteByUserId(userModel.getId());
        if(CollectionUtils.isNotEmpty(userModel.getRoleIds())) {
            for(Integer roleId : userModel.getRoleIds()) {
                UserRoleModel userRoleModel = new UserRoleModel();
                userRoleModel.setUserId(userModel.getId());
                userRoleModel.setRoleId(roleId);
                systemUserRoleService.saveNotNull(userRoleModel);
            }
        }

        coolplayUserCache.removeUserFromCacheByUserId(userModel.getId());

        return ResponseUtil.success();
    }

    @ResponseBody
    @RequestMapping(value="/saveUser", method = RequestMethod.POST)
    public Result saveUser(UserModel userModel) {
        try{
            if(StringUtils.isNotEmpty(userModel.getPassword())) {
                String passwordEncode = SecurityUtil.encodeString(userModel.getPassword());

                UserPassMappingModel userPassMappingModel = new UserPassMappingModel();
                userPassMappingModel.setPassword(userModel.getPassword());
                userPassMappingModel.setPasswordEncode(passwordEncode);
                userPassMappingService.saveNotNull(userPassMappingModel);
                userModel.setPassword(passwordEncode);
            }

            int saveCnt = userService.saveNotNull(userModel);

            if(CollectionUtils.isNotEmpty(userModel.getRoleIds())) {
                for(Integer roleId : userModel.getRoleIds()) {
                    UserRoleModel userRoleModel = new UserRoleModel();
                    userRoleModel.setUserId(userModel.getId());
                    userRoleModel.setRoleId(roleId);
                    systemUserRoleService.saveNotNull(userRoleModel);
                }
            }
        } catch(DuplicateKeyException e) {
            e.printStackTrace();
            return ResponseUtil.error("用户名已占用, 请更换其他用户名!!");
        } catch(Exception e) {
            return ResponseUtil.error("系统异常, 请稍后重试!!");
        }

        return ResponseUtil.success();
    }

    /**
     * 获取用户角色信息
     *
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getUserRoles", method = RequestMethod.GET)
    public Result getUserRoles(@RequestParam("userId") int userId) {
        Map<Integer, List<UserRoleModel>> userRoleMap = roleService.findUserRoles(Collections.singletonList(userId));

        return ResponseUtil.success(userRoleMap.get(userId));
    }

    @ResponseBody
    @RequestMapping(value = "/getCurrUserRoles", method = RequestMethod.GET)
    public Result getCurrUserRoles() {
        RoleModel roleModel = new RoleModel();
        roleModel.setStatus(1);
        List<RoleModel> roleModels = roleService.selectByFilter(roleModel);

        return ResponseUtil.success(PageConvertUtil.grid(roleModels));
    }

}
