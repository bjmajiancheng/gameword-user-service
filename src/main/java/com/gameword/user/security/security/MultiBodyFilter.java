package com.gameword.user.security.security;

import com.alibaba.fastjson.JSON;
import com.gameword.user.security.utils.BaseWebUtils;
import com.gameword.user.common.utils.DateStyle;
import com.gameword.user.common.utils.DateUtil;
import org.springframework.http.HttpMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * Created by majiancheng on 2019/12/15.
 */
public class MultiBodyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String parameters = "";

        if (BaseWebUtils.isJsonPost(httpServletRequest)) {
            httpServletRequest = new MultiReadHttpServletRequest(httpServletRequest);

            parameters = BaseWebUtils.getParameters(httpServletRequest);
        }

        System.out.println(String.format("前端 请求时间:[%s], 请求链接:[%s], 请求参数:%s, 请求body: %s.",
                DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS), httpServletRequest.getRequestURL(),
                JSON.toJSONString(httpServletRequest.getParameterMap()), parameters));

        chain.doFilter(httpServletRequest, response);
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    public static boolean isPost(HttpServletRequest request) {
        return  HttpMethod.POST.matches(request.getMethod());
    }
}
