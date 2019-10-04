package com.coolplay.system.system.api.common;

import com.coolplay.system.common.utils.PageConvertUtil;
import com.coolplay.system.common.utils.ResponseUtil;
import com.coolplay.system.common.utils.Result;
import com.coolplay.system.system.model.CompanyModel;
import com.coolplay.system.system.model.LabelModel;
import com.coolplay.system.system.service.ILabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by majiancheng on 2019/10/4.
 */
@Controller
@RequestMapping("/api/common/label")
public class LabelController {

    @Autowired
    private ILabelService labelService;

    @ResponseBody
    @RequestMapping(value = "/options", method = RequestMethod.GET)
    public Result options() {
        LabelModel labelModel = new LabelModel();
        labelModel.setIsDel(0);
        labelModel.setStatus(1);

        List<LabelModel> labelModels = labelService.selectByFilter(labelModel);

        return ResponseUtil.success(PageConvertUtil.grid(labelModels));
    }
}
