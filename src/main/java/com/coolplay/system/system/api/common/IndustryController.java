package com.coolplay.system.system.api.common;

import com.coolplay.system.common.utils.PageConvertUtil;
import com.coolplay.system.common.utils.ResponseUtil;
import com.coolplay.system.common.utils.Result;
import com.coolplay.system.system.model.IndustryModel;
import com.coolplay.system.system.service.IIndustryService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by majiancheng on 2019/11/1.
 */
@Controller
@RequestMapping("/api/common/industry")
public class IndustryController {

    @Autowired
    private IIndustryService industryService;

    /**
     * 行业列表
     *
     * @param industryModel
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map list(IndustryModel industryModel,
            @RequestParam(value = "page", required = false, defaultValue = "1") int pageNum,
            @RequestParam(value = "rows", required = false, defaultValue = "15") int pageSize) {
        industryModel.setIsDel(0);

        PageInfo<IndustryModel> pageInfo = industryService.selectByFilterAndPage(industryModel, pageNum, pageSize);

        return PageConvertUtil.grid(pageInfo);
    }

    /**
     * 保存行业信息
     *
     * @param industryModel
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveIndustry", method = RequestMethod.POST)
    public Result saveIndustry(IndustryModel industryModel) {

        industryModel.setIsDel(0);
        industryModel.setIndustryName(industryModel.getIndustryName().trim());

        int cnt = industryService.findCntByIndustryNameAndId(industryModel.getIndustryName(), 0);
        if(cnt > 0) {
            return ResponseUtil.error("行业名称已存在, 请修改行业名称");
        }
        int updateCnt = industryService.saveNotNull(industryModel);

        return ResponseUtil.success();
    }

    /**
     * 删除行业信息
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delIndustry", method = RequestMethod.GET)
    public Result delIndustry(@RequestParam(name = "id")Integer id) {
        IndustryModel industryModel = new IndustryModel();
        industryModel.setId(id);
        industryModel.setIsDel(1);

        int updateCnt = industryService.updateNotNull(industryModel);

        return ResponseUtil.success();
    }

    /**
     * 行业信息详情
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Result detail(@RequestParam(name = "id")Integer id) {
        IndustryModel industryModel = industryService.findById(id);

        return ResponseUtil.success(industryModel);
    }

    /**
     * 更新行业信息
     *
     * @param industryModel
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateIndustry", method = RequestMethod.POST)
    public Result updateIndustry(IndustryModel industryModel) {

        industryModel.setIndustryName(industryModel.getIndustryName().trim());

        int cnt = industryService.findCntByIndustryNameAndId(industryModel.getIndustryName(), industryModel.getId());
        if(cnt > 0) {
            return ResponseUtil.error("行业名称已存在, 请修改行业名称");
        }
        int updateCnt = industryService.updateNotNull(industryModel);

        return ResponseUtil.success();
    }
}
