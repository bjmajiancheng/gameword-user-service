package com.gameword.user.security.security;

import com.gameword.user.security.service.ISecurityService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 工程：os-app 创建人 : ChenGJ 创建时间： 2015/9/4 说明：
 */
public class SecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

    private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

    private static ISecurityService securityService;

    private AntPathRequestMatcher matcher;

    public SecurityMetadataSourceImpl(ISecurityService securityService) {
        this.securityService = securityService;
        this.resourceMap = securityService.getDbResourceMap();
    }

    public static void refreshResourceMap() {
        resourceMap = securityService.getDbResourceMap();
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        final HttpServletRequest request = ((FilterInvocation) o).getRequest();
        Iterator<String> it = securityService.getCacheResourceMap().keySet().iterator();
        while (it.hasNext()) {
            String resURL = it.next();
            matcher = new AntPathRequestMatcher(resURL);
            if (matcher.matches(request)) {
                Collection<ConfigAttribute> returnCollection = securityService.getCacheResourceMap().get(resURL);
                return returnCollection;
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();

        for (Map.Entry<String, Collection<ConfigAttribute>> entry : securityService.getCacheResourceMap().entrySet()) {
            allAttributes.addAll(entry.getValue());
        }

        return allAttributes;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
