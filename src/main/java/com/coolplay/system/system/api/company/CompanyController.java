package com.coolplay.system.system.api.company;

import com.coolplay.system.common.utils.PageConvertUtil;
import com.coolplay.system.common.utils.ResponseUtil;
import com.coolplay.system.common.utils.Result;
import com.coolplay.system.security.utils.SecurityUtil;
import com.coolplay.system.system.model.CompanyModel;
import com.coolplay.system.system.model.CompanyUserModel;
import com.coolplay.system.system.service.ICompanyDeptService;
import com.coolplay.system.system.service.ICompanyService;
import com.coolplay.system.security.service.IRoleService;
import com.coolplay.system.system.service.ICompanyUserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by majiancheng on 2019/9/19.
 */
@Controller
@RequestMapping("/api/system/company")
public class CompanyController {

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private ICompanyDeptService companyDeptService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private ICompanyUserService companyUserService;

    @ResponseBody
    @RequestMapping(value="/list", method = RequestMethod.GET)
    public Map list(CompanyModel companyModel,
            @RequestParam(value = "page", required = false, defaultValue = "1") int pageNum,
            @RequestParam(value = "rows", required = false, defaultValue = "15") int pageSize) {
        companyModel.setIsDel(0);
        PageInfo<CompanyModel> pageInfo = companyService.selectByFilterAndPage(companyModel, pageNum, pageSize);

        return PageConvertUtil.grid(pageInfo);
    }

    @ResponseBody
    @RequestMapping(value="/companyInfo", method = RequestMethod.GET)
    public Result companyInfo(HttpServletRequest request, @RequestParam("id") Integer id) {
        CompanyModel companyModel = companyService.findCompanyById(id);

        return ResponseUtil.success(companyModel);
    }

    @ResponseBody
    @RequestMapping(value = "/updateCompany", method = RequestMethod.POST)
    public Result updateCompany(CompanyModel companyModel) {
        companyModel.setReviewStatus(1);

        //审核通过, 验证用户账户信息
        if(companyModel.getStatus() == 1) {
            CompanyUserModel companyUserModel = companyUserService.findByUserName(companyModel.getAdminUserName());
            if(companyUserModel != null && !companyUserModel.getCompanyId().equals(companyModel.getId())) {
                return ResponseUtil.error("后台账号已存在, 请设置其他后台账号!!!");
            }


            if(companyUserModel == null) {
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
            }
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
