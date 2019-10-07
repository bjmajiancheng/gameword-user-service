package com.coolplay.system.system.api.company;

import com.alibaba.fastjson.JSON;
import com.coolplay.system.common.utils.MessageUtil;
import com.coolplay.system.common.utils.PageConvertUtil;
import com.coolplay.system.common.utils.ResponseUtil;
import com.coolplay.system.common.utils.Result;
import com.coolplay.system.security.utils.SecurityUtil;
import com.coolplay.system.system.model.*;
import com.coolplay.system.system.service.*;
import com.coolplay.system.security.service.IRoleService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by majiancheng on 2019/9/19.
 */
@Controller
@RequestMapping("/api/system/company")
public class CompanyController {

    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private ICompanyDeptService companyDeptService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private ICompanyUserService companyUserService;

    @Autowired
    private ICompanyRoleService companyRoleService;

    @Autowired
    private ICompanyRoleFunctionService companyRoleFunctionService;

    @Autowired
    private ICompanyUserRoleService companyUserRoleService;

    @Autowired
    private MessageUtil messageUtil;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map list(CompanyModel companyModel,
            @RequestParam(value = "page", required = false, defaultValue = "1") int pageNum,
            @RequestParam(value = "rows", required = false, defaultValue = "15") int pageSize) {
        companyModel.setIsDel(0);
        PageInfo<CompanyModel> pageInfo = companyService.selectByFilterAndPage(companyModel, pageNum, pageSize);

        return PageConvertUtil.grid(pageInfo);
    }

    @ResponseBody
    @RequestMapping(value = "/companyInfo", method = RequestMethod.GET)
    public Result companyInfo(HttpServletRequest request, @RequestParam("id") Integer id) {
        CompanyModel companyModel = companyService.findCompanyById(id);

        return ResponseUtil.success(companyModel);
    }

    @ResponseBody
    @RequestMapping(value = "/updateCompany", method = RequestMethod.POST)
    public Result updateCompany(CompanyModel companyModel) {
        companyModel.setReviewStatus(1);

        //审核通过, 验证用户账户信息
        if (companyModel.getStatus() == 1) {
            CompanyUserModel companyUserModel = companyUserService.findByUserName(companyModel.getAdminUserName());
            if (companyUserModel != null && !companyUserModel.getCompanyId().equals(companyModel.getId())) {
                return ResponseUtil.error("后台账号已存在, 请设置其他后台账号!!!");
            }

            if (companyUserModel == null) {
                companyUserModel = new CompanyUserModel();
                companyUserModel.setCompanyId(companyModel.getId());
                companyUserModel.setUserName(companyModel.getAdminUserName());
                companyUserModel.setPassword(SecurityUtil.encodeString(companyModel.getAdminPassword()));
                companyUserModel.setContactPhone(companyModel.getContactMobile());
                companyUserModel.setAccountNonLocked(1);
                companyUserModel.setAccountNonExpired(1);
                companyUserModel.setCredentialsNonExpired(1);
                companyUserModel.setIsAdmin(1);
                companyUserModel.setEnabled(1);

                int saveCnt = companyUserService.saveNotNull(companyUserModel);

                CompanyRoleModel companyRoleModel = new CompanyRoleModel();
                companyRoleModel.setCompanyId(companyModel.getId());
                companyRoleModel.setRoleName("系统管理员");
                companyRoleModel.setStatus(1);

                saveCnt = companyRoleService.saveNotNull(companyRoleModel);

                CompanyUserRoleModel companyUserRoleModel = new CompanyUserRoleModel();
                companyUserRoleModel.setUserId(companyUserModel.getId());
                companyUserRoleModel.setRoleId(companyRoleModel.getId());

                companyUserRoleService.saveNotNull(companyUserRoleModel);

                List<CompanyRoleFunctionModel> companyRoleFunctions = new ArrayList<CompanyRoleFunctionModel>();
                List<Integer> functionIds = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
                for (Integer functionId : functionIds) {
                    CompanyRoleFunctionModel companyRoleFunction = new CompanyRoleFunctionModel();
                    companyRoleFunction.setRoleId(companyRoleModel.getId());
                    companyRoleFunction.setFunctionId(functionId);

                    companyRoleFunctions.add(companyRoleFunction);
                }

                for (CompanyRoleFunctionModel companyRoleFunction : companyRoleFunctions) {
                    companyRoleFunctionService.saveNotNull(companyRoleFunction);
                }

                String key = "sms_0000000001";
                String[] values = new String[] { companyModel.getAdminUserName(), companyModel.getAdminPassword() };
                Result result = messageUtil.sendMessage(companyModel.getContactMobile(), key, values);

                logger.info("发送短信完成, 手机号:{}, msg:{}, result:{}.", companyModel.getContactMobile(),
                        messageUtil.getProperty(key, values), JSON.toJSONString(result));
            }
        } else {
            String key = "sms_0000000002";
            String[] values = new String[] { companyModel.getRejectReason() };

            //驳回返回短信信息
            Result result = messageUtil.sendMessage(companyModel.getContactMobile(), key, values);
            logger.info("发送短信完成, 手机号:{}, msg:{}, result:{}.", companyModel.getContactMobile(),
                    messageUtil.getProperty(key, values), JSON.toJSONString(result));
        }

        int cnt = companyService.updateNotNull(companyModel);

        //审核通过,新增用户账户信息

        return ResponseUtil.success();
    }

    @ResponseBody
    @RequestMapping(value = "/companyOptions", method = RequestMethod.GET)
    public Result companyOptions() {
        CompanyModel companyModel = new CompanyModel();
        List<CompanyModel> companyModels = companyService.selectByFilter(companyModel);

        return ResponseUtil.success(PageConvertUtil.grid(companyModels));
    }

}
