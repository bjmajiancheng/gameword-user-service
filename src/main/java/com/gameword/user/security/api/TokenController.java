package com.gameword.user.security.api;

import com.gameword.user.common.tools.RedisCache;
import com.gameword.user.common.utils.HttpResponseUtil;
import com.gameword.user.common.utils.RequestUtil;
import com.gameword.user.security.constants.SecurityConstant;
import com.gameword.user.security.security.AuthenticationRequest;
import com.gameword.user.security.security.HttpAuthenticationDetails;
import com.gameword.user.security.service.IUserService;
import com.gameword.user.security.utils.TokenUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by majiancheng on 2019/9/15.
 */
@Controller
@RequestMapping("/api/token")
public class TokenController {

    private final static Logger logger = LoggerFactory.getLogger(TokenController.class);

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> authenticationRequest(HttpServletRequest request,
            @RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {
        if (StringUtils.isEmpty(authenticationRequest.getVcode()) || StringUtils
                .isEmpty(authenticationRequest.getVkey())) {
            return ResponseEntity.ok(HttpResponseUtil.error("请输入验证码"));
        }
        if (StringUtils.isNotEmpty((String) redisCache.get(authenticationRequest.getVkey()))) {
            if (!((String) redisCache.get(authenticationRequest.getVkey())).equals(authenticationRequest.getVcode())) {
                return ResponseEntity.ok(HttpResponseUtil.error("验证码不正确"));
            }
        } else {
            return ResponseEntity.ok(HttpResponseUtil.error("验证码不存在或已过期"));
        }
        redisCache.del(authenticationRequest.getVkey());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword());
        usernamePasswordAuthenticationToken.setDetails(new HttpAuthenticationDetails());

        Authentication authentication = null;
        try {
            authentication = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            if (authentication == null) {
                return ResponseEntity.ok(HttpResponseUtil.error("未检测到验证信息"));
            }
        } catch (InternalAuthenticationServiceException failed) {
            logger.error("An internal error occurred while trying to authenticate the user.", failed);
            return ResponseEntity.ok(HttpResponseUtil.error(failed.getMessage()));
        } catch (AuthenticationException failed) {
            return ResponseEntity.ok(HttpResponseUtil.error(failed.getMessage()));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) redisCache
                .get(SecurityConstant.USER_CACHE_PREFIX + authenticationRequest.getUsername());
        if (userDetails == null)  {
            userDetails = this.userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            redisCache.set(SecurityConstant.USER_CACHE_PREFIX + authenticationRequest.getUsername(), userDetails, 10 * 12 * 30 * 24 * 60 * 60);
        }
        String token = this.tokenUtils.generateToken(userDetails);
        userService.updateLastLoginInfoByUserName(authenticationRequest.getUsername(), new Date(),
                RequestUtil.getIpAddress(request));

        return ResponseEntity.ok(HttpResponseUtil.success(token));
    }
}
