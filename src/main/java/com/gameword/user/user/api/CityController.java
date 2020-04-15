package com.gameword.user.user.api;

import com.gameword.user.common.dto.QueryDto;
import com.gameword.user.common.utils.PageConvertUtil;
import com.gameword.user.common.utils.ResponseUtil;
import com.gameword.user.common.utils.Result;
import com.gameword.user.core.model.UserModel;
import com.gameword.user.security.constants.SecurityConstant;
import com.gameword.user.security.utils.SecurityUtil;
import com.gameword.user.user.model.*;
import com.gameword.user.user.service.ICityService;
import com.gameword.user.user.service.ICompanyLabelService;
import com.gameword.user.user.service.ICompanyService;
import com.gameword.user.user.service.ILabelService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by majiancheng on 2020/3/9.
 */
@Controller
@RequestMapping("/api/city")
public class CityController {

    @Autowired
    private ICityService cityService;

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private ILabelService labelService;

    @Autowired
    private ICompanyLabelService companyLabelService;

    /**
     * 城市列表
     *
     * @param queryDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result list(@RequestBody QueryDto queryDto) {
        try {
            List<CityModel> cityModels = cityService.selectByFilter(new CityModel());

            return ResponseUtil.success(Collections.singletonMap("citys", cityModels));
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/cityCompany", method = RequestMethod.POST)
    public Result cityCompany(@RequestBody QueryDto queryDto) {
        try {
            CompanyModel companyModel = new CompanyModel();
            companyModel.setCityId(queryDto.getCityId());
            PageInfo<CompanyModel> pageInfo = companyService.selectByFilterAndPage(companyModel, queryDto.getPageNum(), queryDto.getPageSize());

            return ResponseUtil.success(PageConvertUtil.grid(pageInfo));
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/companyList", method = RequestMethod.POST)
    public Result companyList(@RequestBody QueryDto queryDto) {
        try {
            CompanyModel companyModel = new CompanyModel();
            companyModel.setCityId(queryDto.getCityId());
            if(queryDto.getLanguage() == 1) {
                companyModel.setCnName(queryDto.getQueryStr());
            } else if(queryDto.getLanguage() == 2) {
                companyModel.setEnName(queryDto.getQueryStr());
            }
            PageInfo<CompanyModel> pageInfo = companyService.selectByFilterAndPage(companyModel, queryDto.getPageNum(), queryDto.getPageSize());

            return ResponseUtil.success(PageConvertUtil.grid(pageInfo));
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/companyDetail", method = RequestMethod.POST)
    public Result companyDetail(@RequestBody QueryDto queryDto) {
        try {
            CompanyModel companyModel = companyService.findById(queryDto.getCompanyId());
            if(companyModel == null) {
                return ResponseUtil.error();
            }

            CompanyLabelModel companyLabelModel = new CompanyLabelModel();
            companyLabelModel.setCompanyId(queryDto.getCompanyId());

            List<LabelModel> labelModels = labelService.findByCompanyId(queryDto.getCompanyId());
            if(CollectionUtils.isNotEmpty(labelModels)) {
                List<String> cnLabelNames = new ArrayList<String>();
                List<String> enLabelNames = new ArrayList<String>();

                for(LabelModel labelModel : labelModels) {
                    if(labelModel.getLanguage() == 1) {
                        cnLabelNames.add(labelModel.getLabelName());
                    } else if(labelModel.getLanguage() == 2) {
                        enLabelNames.add(labelModel.getLabelName());
                    }
                }
            }

            return ResponseUtil.success(Collections.singletonMap("company", companyModel));
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/labelList", method = RequestMethod.POST)
    public Result labelList(@RequestBody QueryDto queryDto) {

        try {
            List<LabelModel> labelModels = labelService.findByCityId(queryDto.getCityId());

            return ResponseUtil.success(Collections.singletonMap("labels", labelModels));
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }


}
