package com.gameword.user.user.api;

import com.gameword.user.common.utils.*;
import com.gameword.user.core.model.UserModel;
import com.gameword.user.security.constants.SecurityConstant;
import com.gameword.user.security.utils.SecurityUtil;
import com.gameword.user.user.service.*;
import com.github.pagehelper.PageInfo;
import com.gameword.user.common.dto.QueryDto;
import com.gameword.user.common.tools.RedisCache;
import com.gameword.user.security.api.TokenController;
import com.gameword.user.security.security.AuthenticationRequest;
import com.gameword.user.security.security.CoolplayUserCache;
import com.gameword.user.security.security.HttpAuthenticationDetails;
import com.gameword.user.security.service.IUserService;
import com.gameword.user.security.utils.TokenUtils;
import com.gameword.user.user.model.UserPassMappingModel;
import io.rong.models.response.TokenResult;
import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by majiancheng on 2019/10/16.
 */
@Controller
@RequestMapping("/api/user")
public class UserController {

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

    @Autowired
    private CoolplayUserCache coolplayUserCache;

    @Autowired
    private IUserPassMappingService userPassMappingService;

    @Autowired
    private RongyunUtil rongyunUtil;


    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(HttpServletRequest request,
            @RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {

        try {
            UserModel userModel = userService.findUserByMobilePhone(authenticationRequest.getMobilePhone());
            if (userModel == null) {
                return ResponseUtil.error("用户不存在");
            }

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userModel.getUserName(), authenticationRequest.getPassword());
            usernamePasswordAuthenticationToken.setDetails(new HttpAuthenticationDetails());

            Authentication authentication = null;
            try {
                authentication = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
                if (authentication == null) {
                    return ResponseUtil.error("未检测到验证信息");
                }
            } catch (InternalAuthenticationServiceException failed) {
                logger.error("An internal error occurred while trying to authenticate the user.", failed);
                return ResponseUtil.error(failed.getMessage());
            } catch (AuthenticationException failed) {
                return ResponseUtil.error(failed.getMessage());
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userModel.getUserName());
            redisCache.set(
                    SecurityConstant.USER_CACHE_PREFIX + userModel.getUserName(), userDetails, 10 * 12 * 30 * 24 * 60 * 60);

            String token = this.tokenUtils.generateToken(userDetails);
            userService.updateLastLoginInfoByUserName(userModel.getUserName(), new Date(),
                    RequestUtil.getIpAddress(request));

            UserModel userDetailInfo = getUserDetailInfo(userModel.getId());
            userDetailInfo.setToken(token);

            return ResponseUtil.success(userDetailInfo);

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/loginByEmail", method = RequestMethod.POST)
    public Result loginByEmail(HttpServletRequest request,
            @RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {

        try {
            if (StringUtils.isEmpty(authenticationRequest.getVerifyCode())) {
                return ResponseUtil.error("请输入验证码");
            }
            if (StringUtils.isNotEmpty((String) redisCache
                    .get(SecurityConstant.MOBILE_VERIFY_CODE_PREFIX + authenticationRequest.getEmail()))) {
                if (!((String) redisCache
                        .get(SecurityConstant.MOBILE_VERIFY_CODE_PREFIX + authenticationRequest.getEmail()))
                        .equals(authenticationRequest.getVerifyCode())) {
                    return ResponseUtil.error("验证码不正确");
                }
            } else {
                return ResponseUtil.error("验证码不存在或已过期");
            }

            UserModel userModel = userService.findUserByEmail(authenticationRequest.getEmail());
            if (userModel == null) {
                return ResponseUtil.error("用户不存在");
            }

            UserPassMappingModel userPassMappingModel = userPassMappingService
                    .findByPasswordEncode(userModel.getPassword());
            if (userPassMappingModel == null) {
                return ResponseUtil.error("系统异常, 请稍后重试");
            }

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userModel.getUserName(), userPassMappingModel.getPassword());
            usernamePasswordAuthenticationToken.setDetails(new HttpAuthenticationDetails());

            Authentication authentication = null;
            try {
                authentication = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
                if (authentication == null) {
                    return ResponseUtil.error("未检测到验证信息");
                }
            } catch (InternalAuthenticationServiceException failed) {
                logger.error("An internal error occurred while trying to authenticate the user.", failed);
                return ResponseUtil.error(failed.getMessage());
            } catch (AuthenticationException failed) {
                return ResponseUtil.error(failed.getMessage());
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userModel.getUserName());
            redisCache.set(
                    SecurityConstant.USER_CACHE_PREFIX + userModel.getUserName(), userDetails, 10 * 12 * 30 * 24 * 60 * 60);

            String token = this.tokenUtils.generateToken(userDetails);
            userService.updateLastLoginInfoByUserName(userModel.getUserName(), new Date(),
                    RequestUtil.getIpAddress(request));

            UserModel userDetailInfo = getUserDetailInfo(userModel.getId());
            userDetailInfo.setToken(token);

            return ResponseUtil.success(userDetailInfo);

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }

    @RequestMapping(value = "/loginByMobilePhone", method = RequestMethod.POST)
    public ResponseEntity<?> loginByMobilePhone(HttpServletRequest request,
            @RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {

        try {
            UserModel userModel = userService.findUserByMobilePhone(authenticationRequest.getMobilePhone());
            if (userModel == null) {
                return ResponseEntity.ok(HttpResponseUtil.error("用户不存在"));
            }

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userModel.getUserName(), authenticationRequest.getPassword());
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
            /*UserDetails userDetails = (UserDetails) redisCache
                    .get(SecurityConstant.USER_CACHE_PREFIX + userModel.getUserName());
            if (userDetails == null) {
                userDetails = this.userDetailsService.loadUserByUsername(userModel.getUserName());
                redisCache.set(SecurityConstant.USER_CACHE_PREFIX + userModel.getUserName(), userDetails, 10 * 12 * 30 * 24 * 60 * 60);
            }*/
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userModel.getUserName());
            redisCache.set(
                    SecurityConstant.USER_CACHE_PREFIX + userModel.getUserName(), userDetails, 10 * 12 * 30 * 24 * 60 * 60);

            String token = this.tokenUtils.generateToken(userDetails);
            userService.updateLastLoginInfoByUserName(userModel.getUserName(), new Date(),
                    RequestUtil.getIpAddress(request));

            UserModel userDetailInfo = getUserDetailInfo(userModel.getId());
            userDetailInfo.setToken(token);

            return ResponseEntity.ok(HttpResponseUtil.success(userDetailInfo));

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.ok(HttpResponseUtil.error("系统异常, 请稍后重试。"));
        }
    }

    @RequestMapping(value = "/loginByVerifyCode", method = RequestMethod.POST)
    public ResponseEntity<?> loginByVerifyCode(HttpServletRequest request,
            @RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {

        try {
            if (StringUtils.isEmpty(authenticationRequest.getVerifyCode())) {
                return ResponseEntity.ok(HttpResponseUtil.error("请输入验证码"));
            }
            if (StringUtils.isNotEmpty((String) redisCache
                    .get(SecurityConstant.MOBILE_VERIFY_CODE_PREFIX + authenticationRequest.getMobilePhone()))) {
                if (!((String) redisCache
                        .get(SecurityConstant.MOBILE_VERIFY_CODE_PREFIX + authenticationRequest.getMobilePhone()))
                        .equals(authenticationRequest.getVerifyCode())) {
                    return ResponseEntity.ok(HttpResponseUtil.error("验证码不正确"));
                }
            } else {
                return ResponseEntity.ok(HttpResponseUtil.error("验证码不存在或已过期"));
            }

            UserModel userModel = userService.findUserByMobilePhone(authenticationRequest.getMobilePhone());
            if (userModel == null) {
                return ResponseEntity.ok(HttpResponseUtil.error("用户不存在"));
            }

            UserPassMappingModel userPassMappingModel = userPassMappingService
                    .findByPasswordEncode(userModel.getPassword());
            if (userPassMappingModel == null) {
                return ResponseEntity.ok(HttpResponseUtil.error("系统异常, 请稍后重试"));
            }

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userModel.getUserName(), userPassMappingModel.getPassword());
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
            /*UserDetails userDetails = (UserDetails) redisCache
                    .get(SecurityConstant.USER_CACHE_PREFIX + userModel.getUserName());
            if (userDetails == null) {
                userDetails = this.userDetailsService.loadUserByUsername(userModel.getUserName());
                redisCache.set(SecurityConstant.USER_CACHE_PREFIX + userModel.getUserName(), userDetails, 10 * 12 * 30 * 24 * 60 * 60);
            }*/
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userModel.getUserName());
            redisCache.set(SecurityConstant.USER_CACHE_PREFIX + userModel.getUserName(), userDetails, 10 * 12 * 30 * 24 * 60 * 60);

            String token = this.tokenUtils.generateToken(userDetails);
            userService.updateLastLoginInfoByUserName(userModel.getUserName(), new Date(),
                    RequestUtil.getIpAddress(request));

            UserModel userDetailInfo = getUserDetailInfo(userModel.getId());
            userDetailInfo.setToken(token);

            return ResponseEntity.ok(HttpResponseUtil.success(userDetailInfo));

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.ok(ResponseUtil.error("系统异常, 请稍后重试。"));
        }
    }

    /**
     * 微信登录
     *
     * @param request
     * @param authenticationRequest
     * @return
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/loginByWechat")
    public ResponseEntity<?> loginByWechat(HttpServletRequest request,
            @RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {
        if (StringUtils.isEmpty(authenticationRequest.getOpenId()) || StringUtils
                .isEmpty(authenticationRequest.getAccessToken())) {
            return ResponseEntity.ok(HttpResponseUtil.error("请输入第三方登录信息"));
        }

        try {
            UserModel userModel = userService.findUserByThirdInfo(authenticationRequest.getOpenId(), 1);

            if (userModel == null) {
                Map<String, Object> wechatUserInfo = WechatUtils
                        .getWechatInfoByOpenIdAndAccessToken(authenticationRequest.getOpenId(),
                                authenticationRequest.getAccessToken());

                userModel = new UserModel();
                String uuid = UUIDUtils.generUUID();
                userModel.setMobilePhone(uuid);
                userModel.setUserName(uuid);
                userModel.setMobilePhone(uuid);
                String passwordEncode = SecurityUtil.encodeString(uuid);
                userModel.setPassword(passwordEncode);
                userModel.setNickName(String.valueOf(wechatUserInfo.get("nickname")));
                userModel.setHeadImage(String.valueOf(wechatUserInfo.get("headimgurl")));
                userModel.setAccountNonLocked(true);
                userModel.setAccountNonExpired(true);
                userModel.setCredentialsNonExpired(true);
                userModel.setEnabled(true);
                userModel.setLastPasswordReset(new Date());

                int insertCnt = userService.saveNotNull(userModel);

                UserPassMappingModel userPassMappingModel = new UserPassMappingModel();
                userPassMappingModel.setPassword(uuid);
                userPassMappingModel.setPasswordEncode(passwordEncode);
                userPassMappingService.insert(userPassMappingModel);
            }

            UserPassMappingModel userPassMappingModel = userPassMappingService
                    .findByPasswordEncode(userModel.getPassword());
            if (userPassMappingModel == null) {
                return ResponseEntity.ok(HttpResponseUtil.error("系统异常, 请稍后重试"));
            }

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userModel.getUserName(), userPassMappingModel.getPassword());
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
            /*UserDetails userDetails = (UserDetails) redisCache
                    .get(SecurityConstant.USER_CACHE_PREFIX + userModel.getUserName());
            if (userDetails == null) {
                userDetails = this.userDetailsService.loadUserByUsername(userModel.getUserName());
                redisCache.set(SecurityConstant.USER_CACHE_PREFIX + userModel.getUserName(), userDetails, 10 * 12 * 30 * 24 * 60 * 60);
            }*/
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userModel.getUserName());
            redisCache.set(SecurityConstant.USER_CACHE_PREFIX + userModel.getUserName(), userDetails, 10 * 12 * 30 * 24 * 60 * 60);
            String token = this.tokenUtils.generateToken(userDetails);
            userService.updateLastLoginInfoByUserName(userModel.getUserName(), new Date(),
                    RequestUtil.getIpAddress(request));

            UserModel userDetailInfo = getUserDetailInfo(userModel.getId());
            userDetailInfo.setToken(token);

            return ResponseEntity.ok(HttpResponseUtil.success(userDetailInfo));
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.ok(ResponseUtil.error("系统异常, 请稍后重试。"));
        }
    }

    /**
     * QQ登录
     *
     * @param request
     * @param authenticationRequest
     * @return
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/loginByQQ")
    public ResponseEntity<?> loginByQQ(HttpServletRequest request,
            @RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {
        if (StringUtils.isEmpty(authenticationRequest.getOpenId()) || StringUtils
                .isEmpty(authenticationRequest.getNickname()) || StringUtils.isEmpty(authenticationRequest.getHeadpic())) {
            return ResponseEntity.ok(HttpResponseUtil.error("请输入第三方登录信息"));
        }

        try {
            UserModel userModel = userService.findUserByThirdInfo(authenticationRequest.getOpenId(), 1);

            if (userModel == null) {

                userModel = new UserModel();
                String uuid = UUIDUtils.generUUID();
                userModel.setMobilePhone(uuid);
                userModel.setUserName(uuid);
                userModel.setMobilePhone(uuid);
                String passwordEncode = SecurityUtil.encodeString(uuid);
                userModel.setPassword(passwordEncode);
                userModel.setNickName(authenticationRequest.getNickname());
                userModel.setHeadImage(authenticationRequest.getHeadpic());
                userModel.setAccountNonLocked(true);
                userModel.setAccountNonExpired(true);
                userModel.setCredentialsNonExpired(true);
                userModel.setEnabled(true);
                userModel.setLastPasswordReset(new Date());

                int insertCnt = userService.saveNotNull(userModel);

                UserPassMappingModel userPassMappingModel = new UserPassMappingModel();
                userPassMappingModel.setPassword(uuid);
                userPassMappingModel.setPasswordEncode(passwordEncode);
                userPassMappingService.insert(userPassMappingModel);
            }

            UserPassMappingModel userPassMappingModel = userPassMappingService
                    .findByPasswordEncode(userModel.getPassword());
            if (userPassMappingModel == null) {
                return ResponseEntity.ok(HttpResponseUtil.error("系统异常, 请稍后重试"));
            }

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userModel.getUserName(), userPassMappingModel.getPassword());
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
            /*UserDetails userDetails = (UserDetails) redisCache
                    .get(SecurityConstant.USER_CACHE_PREFIX + userModel.getUserName());
            if (userDetails == null) {
                userDetails = this.userDetailsService.loadUserByUsername(userModel.getUserName());
                redisCache.set(SecurityConstant.USER_CACHE_PREFIX + userModel.getUserName(), userDetails, 10 * 12 * 30 * 24 * 60 * 60);
            }*/

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userModel.getUserName());
            redisCache.set(SecurityConstant.USER_CACHE_PREFIX + userModel.getUserName(), userDetails, 10 * 12 * 30 * 24 * 60 * 60);

            String token = this.tokenUtils.generateToken(userDetails);
            userService.updateLastLoginInfoByUserName(userModel.getUserName(), new Date(),
                    RequestUtil.getIpAddress(request));

            UserModel userDetailInfo = getUserDetailInfo(userModel.getId());
            userDetailInfo.setToken(token);

            return ResponseEntity.ok(HttpResponseUtil.success(userDetailInfo));
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.ok(ResponseUtil.error("系统异常, 请稍后重试。"));
        }
    }

    /**
     * 用户注册
     *
     * @param userModel
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result register(HttpServletRequest request, @RequestBody UserModel userModel) {

        try {
            /*if (StringUtils.isEmpty(userModel.getVerifyCode())) {
                return ResponseUtil.error("请输入验证码");
            }
            if (StringUtils
                    .isNotEmpty((String) redisCache.get(SecurityConstant.MOBILE_VERIFY_CODE_PREFIX + userModel.getMobilePhone()))) {
                if (!((String) redisCache.get(SecurityConstant.MOBILE_VERIFY_CODE_PREFIX + userModel.getMobilePhone()))
                        .equals(userModel.getVerifyCode())) {
                    return ResponseUtil.error("验证码不正确");
                }
            } else {
                return ResponseUtil.error("验证码不存在或已过期");

            }*/
            UserModel userInfo = userService.findUserByMobilePhone(userModel.getMobilePhone());
            if (userInfo != null) {
                return ResponseUtil.error("手机号已使用, 请更换手机号.");
            }

            String password = userModel.getPassword();
            String passwordEncode = SecurityUtil.encodeString(password);
            userModel.setPassword(passwordEncode);
            userModel.setUserName(userModel.getMobilePhone());
            userModel.setAccountNonLocked(true);
            userModel.setAccountNonExpired(true);
            userModel.setCredentialsNonExpired(true);
            userModel.setEnabled(true);
            userModel.setLastPasswordReset(new Date());
            userModel.setUserType(4);

            int insertCnt = userService.saveNotNull(userModel);

            //生成融云Token信息
            String rongyunToken = userService.generRongyunToken(userModel.getId(), userModel.getNickName(), userModel.getHeadImage());

            UserPassMappingModel userPassMappingModel = new UserPassMappingModel();
            userPassMappingModel.setPassword(password);
            userPassMappingModel.setPasswordEncode(passwordEncode);
            userPassMappingService.insert(userPassMappingModel);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userModel.getUserName(), userPassMappingModel.getPassword());
            usernamePasswordAuthenticationToken.setDetails(new HttpAuthenticationDetails());

            Authentication authentication = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            if (authentication == null) {
                return HttpResponseUtil.error("未检测到验证信息");
            }


            SecurityContextHolder.getContext().setAuthentication(authentication);
            /*UserDetails userDetails = (UserDetails) redisCache
                    .get(SecurityConstant.USER_CACHE_PREFIX + userModel.getUserName());
            if (userDetails == null) {
                userDetails = this.userDetailsService.loadUserByUsername(userModel.getUserName());
                redisCache.set(SecurityConstant.USER_CACHE_PREFIX + userModel.getUserName(), userDetails, 10 * 12 * 30 * 24 * 60 * 60);
            }*/

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userModel.getUserName());
            redisCache.set(SecurityConstant.USER_CACHE_PREFIX + userModel.getUserName(), userDetails, 10 * 12 * 30 * 24 * 60 * 60);
            String token = this.tokenUtils.generateToken(userDetails);
            userService.updateLastLoginInfoByUserName(userModel.getUserName(), new Date(),
                    RequestUtil.getIpAddress(request));


            UserModel userDetailInfo = getUserDetailInfo(userModel.getId());
            userDetailInfo.setToken(token);
            userDetailInfo.setRongyunToken(rongyunToken);

            return ResponseUtil.success(userDetailInfo);

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }

    /**
     * 补全用户信息
     *
     * @param userModel
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/adjustUserInfo", method = RequestMethod.POST)
    public Result adjustUserInfo(@RequestBody UserModel userModel) {

        try {
            int currUserId = SecurityUtil.getCurrentUserId();
            if (userModel.getId() != currUserId) {
                return ResponseUtil.error("无权限修改其他用户信息");
            }

            if (StringUtils.isEmpty(userModel.getVerifyCode())) {
                return ResponseUtil.error("请输入验证码");
            }
            if (StringUtils
                    .isNotEmpty((String) redisCache.get(SecurityConstant.MOBILE_VERIFY_CODE_PREFIX + userModel.getMobilePhone()))) {
                if (!((String) redisCache.get(SecurityConstant.MOBILE_VERIFY_CODE_PREFIX + userModel.getMobilePhone()))
                        .equals(userModel.getVerifyCode())) {
                    return ResponseUtil.error("验证码不正确");
                }
            } else {
                return ResponseUtil.error("验证码不存在或已过期");
            }

            UserModel validateUserModel = userService.findUserByMobilePhone(userModel.getMobilePhone());
            if(validateUserModel != null) {
                return ResponseUtil.error("该手机号已注册, 请更换手机号。");
            }
            userModel.setUserName(userModel.getMobilePhone());

            int updateCnt = userService.updateNotNull(userModel);

            //生成融云Token信息
            String rongyunToken = userService.generRongyunToken(userModel.getId(), userModel.getNickName(), userModel.getHeadImage());


            return ResponseUtil.success("补全信息成功");
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }

    /**
     * 重置密码
     *
     * @param queryDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public Result resetPassword(@RequestBody QueryDto queryDto) {

        try {
            if (StringUtils.isEmpty(queryDto.getVerifyCode())) {
                return ResponseUtil.error("请输入验证码");
            }
            if (StringUtils
                    .isNotEmpty((String) redisCache.get(SecurityConstant.MOBILE_VERIFY_CODE_PREFIX + queryDto.getEmail()))) {
                if (!((String) redisCache.get(SecurityConstant.MOBILE_VERIFY_CODE_PREFIX + queryDto.getEmail()))
                        .equals(queryDto.getVerifyCode())) {
                    return ResponseUtil.error("验证码不正确");
                }
            } else {
                return ResponseUtil.error("验证码不存在或已过期");
            }

            UserModel userInfo = userService.findUserByEmail(queryDto.getEmail());
            if (userInfo == null) {
                return ResponseUtil.error("账户不存在");
            }

            UserModel userModel = new UserModel();
            userModel.setId(userInfo.getId());
            String passwordEncode = SecurityUtil.encodeString(queryDto.getPassword());
            userModel.setPassword(passwordEncode);
            userModel.setLastPasswordReset(new Date());
            int updateCnt = userService.updateNotNull(userModel);

            UserPassMappingModel userPassMappingModel = new UserPassMappingModel();
            userPassMappingModel.setPassword(queryDto.getPassword());
            userPassMappingModel.setPasswordEncode(passwordEncode);
            userPassMappingService.insert(userPassMappingModel);

            return ResponseUtil.success("修改密码成功");

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }

    /**
     * 重置密码
     *
     * @param queryDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public Result updatePassword(@RequestBody QueryDto queryDto) {
        /*if (StringUtils.isEmpty(queryDto.getVerifyCode())) {
            return ResponseUtil.error("请输入验证码");
        }*/

        try {
            /*if (StringUtils
                    .isNotEmpty((String) redisCache.get(SecurityConstant.MOBILE_VERIFY_CODE_PREFIX + queryDto.getMobilePhone()))) {
                if (!((String) redisCache.get(SecurityConstant.MOBILE_VERIFY_CODE_PREFIX + queryDto.getMobilePhone()))
                        .equals(queryDto.getVerifyCode())) {
                    return ResponseUtil.error("验证码不正确");
                }
            } else {
                return ResponseUtil.error("验证码不存在或已过期");
            }*/

            if(StringUtils.isEmpty(queryDto.getOriginPassword()) || StringUtils.isEmpty(queryDto.getPassword())) {
                return ResponseUtil.error("原始密码和新密码不能为空");
            }

            UserModel userInfo = userService.findUserByUserId(SecurityUtil.getCurrentUserId());
            if (userInfo == null) {
                return ResponseUtil.error("账户不存在");
            }


            if(!SecurityUtil.matchString(queryDto.getOriginPassword(), userInfo.getPassword())) {
                return ResponseUtil.error("原始密码不正确");
            }

            UserModel userModel = new UserModel();
            userModel.setId(userInfo.getId());
            String passwordEncode = SecurityUtil.encodeString(queryDto.getPassword());
            userModel.setPassword(passwordEncode);
            userModel.setLastPasswordReset(new Date());
            int updateCnt = userService.updateNotNull(userModel);

            UserPassMappingModel userPassMappingModel = new UserPassMappingModel();
            userPassMappingModel.setPassword(queryDto.getPassword());
            userPassMappingModel.setPasswordEncode(passwordEncode);
            userPassMappingService.insert(userPassMappingModel);

            return ResponseUtil.success("修改密码成功");

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }

    /**
     * 退出登录
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Result logout() {
        try {
            String userName = SecurityUtil.getCurrentUserName();
            coolplayUserCache.removeUserFromCache(userName);
            return ResponseUtil.success();

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }

    /**
     * 获取用户详细信息
     *
     * @param id
     * @return
     */
    private UserModel getUserDetailInfo(Integer id) {
        UserModel userModel = userService.findById(id);

        //生成融云Token信息
        if(StringUtils.isEmpty(userModel.getRongyunToken())) {
            String rongyunToken = userService.generRongyunToken(userModel.getId(), userModel.getNickName(), userModel.getHeadImage());

            userModel.setRongyunToken(rongyunToken);
        }

        return userModel;
    }

    /**
     * 更新用户信息
     *
     * @param userModel
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public Result updateUser(@RequestBody UserModel userModel) {

        try {
            int currUserId = SecurityUtil.getCurrentUserId();
            if (userModel.getId() != null && userModel.getId() != currUserId) {
                return ResponseUtil.error("无权限修改其他用户详细信息");
            }

            if(StringUtils.isNotEmpty(userModel.getPassword())) {
                String passwordEncode = SecurityUtil.encodeString(userModel.getPassword());
                userModel.setPassword(passwordEncode);
                userModel.setLastPasswordReset(new Date());

                UserPassMappingModel userPassMappingModel = new UserPassMappingModel();
                userPassMappingModel.setPassword(userModel.getPassword());
                userPassMappingModel.setPasswordEncode(passwordEncode);
                userPassMappingService.insert(userPassMappingModel);
            }

            userModel.setId(currUserId);
            int updateCnt = userService.updateNotNull(userModel);

            //生成融云Token信息
            String rongyunToken = userService.generRongyunToken(userModel.getId(), userModel.getNickName(), userModel.getHeadImage());

            UserModel userDetailInfo = getUserDetailInfo(userModel.getId());

            return ResponseUtil.success(userDetailInfo);

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/updateMobilePhone", method = RequestMethod.POST)
    public Result updateMobilePhone(@RequestBody QueryDto queryDto) {
        if (queryDto.getId() != SecurityUtil.getCurrentUserId()) {
            return ResponseUtil.error("无权限修改其他用户手机号码");
        }

        if (StringUtils.isEmpty(queryDto.getVerifyCode())) {
            return ResponseUtil.error("请输入验证码");
        }

        try {
            if (StringUtils
                    .isNotEmpty((String) redisCache.get(SecurityConstant.MOBILE_VERIFY_CODE_PREFIX + queryDto.getMobilePhone()))) {
                if (!((String) redisCache.get(SecurityConstant.MOBILE_VERIFY_CODE_PREFIX + queryDto.getMobilePhone()))
                        .equals(queryDto.getVerifyCode())) {
                    return ResponseUtil.error("验证码不正确");
                }
            } else {
                return ResponseUtil.error("验证码不存在或已过期");
            }

            UserModel userInfo = userService.findUserByUserId(queryDto.getId());
            if (userInfo == null) {
                return ResponseUtil.error("账户不存在");
            }

            UserModel userModel = new UserModel();
            userModel.setId(queryDto.getId());
            userModel.setMobilePhone(queryDto.getMobilePhone());
            int updateCnt = userService.updateNotNull(userModel);

            return ResponseUtil.success();

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result list(@RequestBody UserModel userModel) {

        try {
            List<Integer> labelUserIds = userService.findByLabelName("%" + userModel.getQueryStr() + "%");

            //模糊查询
            List<Integer> userIds = userService.findByNickName("%" + userModel.getQueryStr() + "%");

            Set<Integer> allUserIds = new HashSet<Integer>();
            if(CollectionUtils.isNotEmpty(labelUserIds)) {
                allUserIds.addAll(labelUserIds);
            }
            if(CollectionUtils.isNotEmpty(userIds)) {
                allUserIds.addAll(userIds);
            }

            userModel.initPageInfo();
            PageInfo<UserModel> pageInfo = this.userService.selectByUserIds(new ArrayList<Integer>(allUserIds), userModel.getPageNum(), userModel.getPageSize());

            return ResponseUtil.success(PageConvertUtil.grid(pageInfo));
        } catch(Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }


    @ResponseBody
    @RequestMapping( value = "/info", method = RequestMethod.POST)
    public  Result info(@RequestBody QueryDto queryDto){
        try {
            UserModel userModel = userService.findById(SecurityUtil.getCurrentUserId());

            if (userModel == null) {
                return ResponseUtil.error("找不到该用户信息");
            }

            return ResponseUtil.success(userModel);
        } catch(Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/userDetail", method = RequestMethod.POST)
    public Result userDetail(@RequestBody QueryDto queryDto) {

        try {
            UserModel userModel = userService.findById(queryDto.getUserId());

            return ResponseUtil.success(userModel);
        } catch(Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/userInfo", method = RequestMethod.POST)
    public Result userInfo(@RequestBody QueryDto queryDto) {

        try {
            UserModel userModel = userService.findById(queryDto.getUserId());

            String password = userModel.getPassword();
            if(StringUtils.isNotEmpty(password)) {
                UserPassMappingModel userPassMappingModel = userPassMappingService.findByPasswordEncode(password);
                if(userPassMappingModel != null) {
                    userModel.setPassword(userPassMappingModel.getPassword());
                }
            }

            return ResponseUtil.success(userModel);
        } catch(Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }
}
