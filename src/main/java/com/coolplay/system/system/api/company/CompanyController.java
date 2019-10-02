package com.coolplay.system.system.api.company;

import com.coolplay.system.common.utils.PageConvertUtil;
import com.coolplay.system.common.utils.ResponseUtil;
import com.coolplay.system.common.utils.Result;
import com.coolplay.system.system.model.CompanyModel;
import com.coolplay.system.system.service.ICompanyDeptService;
import com.coolplay.system.system.service.ICompanyService;
import com.coolplay.system.security.service.IRoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @ResponseBody
    @RequestMapping(value="list", method = RequestMethod.GET)
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
        int cnt = companyService.updateNotNull(companyModel);

        return ResponseUtil.success();
    }

}
