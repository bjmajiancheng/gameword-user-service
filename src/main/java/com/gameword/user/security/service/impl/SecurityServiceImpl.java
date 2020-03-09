package com.gameword.user.security.service.impl;

import com.gameword.user.common.tools.RedisCache;
import com.gameword.user.core.model.UserModel;
import com.gameword.user.security.constants.SecurityConstant;
import com.gameword.user.security.security.SecurityUser;
import com.gameword.user.security.service.ISecurityService;
import com.gameword.user.security.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by majiancheng on 2019/9/16.
 */
@Service("securityService")
public class SecurityServiceImpl implements ISecurityService {

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 根据登录名称获取用户信息
     *
     * @param loginName
     * @return
     */
    public SecurityUser loadSecurityUserByLoginName(String loginName) {
        UserModel user = userService.findUserByLoginName(loginName);
        if (user == null) {
            return null;
        }
        Collection<GrantedAuthority> userGrantedAuthorities = new ArrayList<GrantedAuthority>();
        /*List<Integer> grantedAuthorities = userService.findUserRoleByUserId(user.getId());
        if (grantedAuthorities != null && grantedAuthorities.size() > 0) {
            for (Integer grantedAuthority : grantedAuthorities) {
                GrantedAuthority ga = new SimpleGrantedAuthority(String.valueOf(grantedAuthority));
                userGrantedAuthorities.add(ga);
            }
        }*/
        SecurityUser securityUser = new SecurityUser(user, userGrantedAuthorities);
        return securityUser;
    }

    @Override
    public Map<String, Collection<ConfigAttribute>> getCacheResourceMap() {
        List<Map> roleFunctions = (List<Map>) redisCache.get(SecurityConstant.RESOURCE_MAP);
        if (roleFunctions == null) {
            return getDbResourceMap();
        }
        return getResourceMap(roleFunctions);
    }

    @Override
    public Map<String, Collection<ConfigAttribute>> getDbResourceMap() {
        /*List<Map> roleFunctions = roleService.findRoleMatchUpFunctions();
        redisCache.set(SecurityConstant.RESOURCE_MAP, roleFunctions);
        return getResourceMap(roleFunctions);*/
        return Collections.emptyMap();
    }

    private Map<String, Collection<ConfigAttribute>> getResourceMap(List<Map> roleFunctions) {
        Map<String, Collection<ConfigAttribute>> resourceMap = new HashMap<>();
        if (roleFunctions != null && roleFunctions.size() > 0) {
            for (Map roleFunction : roleFunctions) {
                String url = (String) roleFunction.get("function");
                Integer role = (Integer) roleFunction.get("role");
                Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
                if (!resourceMap.containsKey(url)) {
                    configAttributes.add(new SecurityConfig(String.valueOf(role)));
                    resourceMap.put(url, configAttributes);
                } else {
                    ConfigAttribute configAttribute = new SecurityConfig(String.valueOf(role));
                    configAttributes = resourceMap.get(url);
                    configAttributes.add(configAttribute);
                    resourceMap.put(url, configAttributes);
                }
            }
        }
        return resourceMap;
    }
}
