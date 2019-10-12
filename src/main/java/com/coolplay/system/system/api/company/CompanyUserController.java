package com.coolplay.system.system.api.company;

import com.coolplay.system.common.tools.RedisCache;
import com.coolplay.system.common.utils.PageConvertUtil;
import com.coolplay.system.common.utils.ResponseUtil;
import com.coolplay.system.common.utils.Result;
import com.coolplay.system.security.constants.SecurityConstant;
import com.coolplay.system.system.model.*;
import com.coolplay.system.system.service.*;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by majiancheng on 2019/10/2.
 */
@Controller
@RequestMapping("/api/company/companyUser")
public class CompanyUserController {

    @Autowired
    private ICompanyUserService companyUserService;

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private ICompanyDeptService companyDeptService;

    @Autowired
    private ICompanyRoleService companyRoleService;

    @Autowired
    private ICompanyUserRoleService companyUserRoleService;

    @Autowired
    private RedisCache redisCache;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map list(CompanyUserModel companyUserModel,
            @RequestParam(value = "page", required = false, defaultValue = "1") int pageNum,
            @RequestParam(value = "rows", required = false, defaultValue = "15") int pageSize) {
        PageInfo<CompanyUserModel> pageInfo = companyUserService
                .selectByFilterAndPage(companyUserModel, pageNum, pageSize);

        if (CollectionUtils.isNotEmpty(pageInfo.getList())) {
            List<Integer> companyIds = new ArrayList<Integer>();
            for(CompanyUserModel tmpCompanyUser : pageInfo.getList()) {
                if(!companyIds.contains(tmpCompanyUser.getCompanyId())) {
                    companyIds.add(tmpCompanyUser.getCompanyId());
                }
            }

            Map<Integer, CompanyModel> companyMap = companyService.findMapByIds(companyIds);
            for(CompanyUserModel tmpCompanyUser : pageInfo.getList()) {
                CompanyModel companyModel = companyMap.get(tmpCompanyUser.getCompanyId());
                if(companyModel != null) {
                    tmpCompanyUser.setCompanyName(companyModel.getCompanyName());
                }
            }
        }

        return PageConvertUtil.grid(pageInfo);
    }

    @ResponseBody
    @RequestMapping(value = "/updateUserEnable", method = RequestMethod.GET)
    public Result updateUserEnable(@RequestParam("userId") Integer userId, @RequestParam("enable") Integer enable) {
        CompanyUserModel companyUserModel = new CompanyUserModel();
        companyUserModel.setId(userId);
        companyUserModel.setEnabled(enable);

        int updateCnt = companyUserService.updateNotNull(companyUserModel);

        companyUserModel = companyUserService.findById(userId);
        redisCache.del(SecurityConstant.COMPANY_USER_CACHE_PREFIX + companyUserModel.getUserName());

        return ResponseUtil.success();
    }

    /**
     * 获取用户详情
     *
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCompanyUser", method = RequestMethod.GET)
    public Result getCompanyUser(@RequestParam("userId") Integer userId) {
        CompanyUserModel companyUserModel = companyUserService.findById(userId);

        CompanyDeptModel companyDeptModel = companyDeptService.selectById(companyUserModel.getDeptId());
        if(companyDeptModel != null) {
            companyUserModel.setDeptName(companyDeptModel.getDeptName());
        }

        List<CompanyUserRoleModel> companyUserRoles = companyUserRoleService.findByUserId(userId);
        if(CollectionUtils.isNotEmpty(companyUserRoles)) {
            StringBuffer sb = new StringBuffer();
            for(CompanyUserRoleModel companyUserRole : companyUserRoles) {
                if(sb.length() > 0) {
                    sb.append("、");
                }
                sb.append(companyUserRole.getRoleName());
            }

            companyUserModel.setRoleName(sb.toString());
        }

        return ResponseUtil.success(companyUserModel);
    }

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delUser", method = RequestMethod.GET)
    public Result delUser(@RequestParam("userId") Integer userId) {


        CompanyUserModel companyUserModel = companyUserService.findById(userId);
        if(companyUserModel != null) {
            redisCache.del(SecurityConstant.COMPANY_USER_CACHE_PREFIX + companyUserModel.getUserName());
        }

        int delCnt = companyUserService.delById(userId);

        return ResponseUtil.success();
    }

    /**
     * 更新用户
     *
     * @param companyUserModel
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateCompanyUser", method = RequestMethod.GET)
    public Result updateCompanyUser(CompanyUserModel companyUserModel) {
        if (companyUserModel == null) {
            return ResponseUtil.error("系统错误, 请稍后重试");
        }

        int updateCnt = companyUserService.updateNotNull(companyUserModel);

        if(companyUserModel != null) {
            redisCache.del(SecurityConstant.COMPANY_USER_CACHE_PREFIX + companyUserModel.getUserName());
        }

        return ResponseUtil.success();
    }
}
