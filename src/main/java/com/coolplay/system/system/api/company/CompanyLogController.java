package com.coolplay.system.system.api.company;

import com.coolplay.system.common.utils.PageConvertUtil;
import com.coolplay.system.system.model.CompanyLogModel;
import com.coolplay.system.system.model.CompanyUserModel;
import com.coolplay.system.system.service.ICompanyLogService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by majiancheng on 2019/10/2.
 */
@Controller
@RequestMapping("/api/company/companyLog")
public class CompanyLogController {

    @Autowired
    private ICompanyLogService companyLogService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map list(CompanyLogModel companyLogModel,
            @RequestParam(value = "page", required = false, defaultValue = "1") int pageNum,
            @RequestParam(value = "rows", required = false, defaultValue = "15") int pageSize) {
        PageInfo<CompanyLogModel> pageInfo = companyLogService
                .selectByFilterAndPage(companyLogModel, pageNum, pageSize);

        return PageConvertUtil.grid(pageInfo);
    }
}
