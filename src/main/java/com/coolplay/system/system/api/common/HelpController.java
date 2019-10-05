package com.coolplay.system.system.api.common;

import com.coolplay.system.common.utils.ResponseUtil;
import com.coolplay.system.common.utils.Result;
import com.coolplay.system.core.model.FunctionModel;
import com.coolplay.system.security.utils.SecurityUtil;
import com.coolplay.system.system.model.HelpModel;
import com.coolplay.system.system.service.IHelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by majiancheng on 2019/10/5.
 */
@Controller
@RequestMapping("/api/common/help")
public class HelpController {

    @Autowired
    private IHelpService helpService;

    @ResponseBody
    @RequestMapping(value = "/getHelp", method = RequestMethod.GET)
    public Result getHelp(@RequestParam("id") Integer id) {
        HelpModel helpModel = helpService.findById(id);

        return ResponseUtil.success(helpModel);
    }

    @ResponseBody
    @RequestMapping(value = "/saveHelp", method = RequestMethod.GET)
    public Result saveHelp(@RequestParam("parentId")Integer parentId, @RequestParam("helpTitle")String helpTitle) {
        HelpModel helpModel = new HelpModel();
        helpModel.setParentId(parentId);
        helpModel.setHelpTitle(helpTitle);
        helpModel.setIsDel(0);
        if(helpModel.getParentId() == 0) {
            helpModel.setHelpLevel(1);
        } else {
            helpModel.setHelpLevel(2);
        }
        helpModel.setSystemUserId(SecurityUtil.getCurrentUserId());
        helpModel.setHelpContent("");

        int saveCnt = helpService.saveNotNull(helpModel);

        return ResponseUtil.success(helpModel);
    }

    @ResponseBody
    @RequestMapping(value = "/updateHelp", method = RequestMethod.POST)
    public Result updateHelp(HelpModel helpModel) {
        int updateCnt = helpService.updateNotNull(helpModel);

        return ResponseUtil.success();
    }

    @ResponseBody
    @RequestMapping(value = "/delHelp", method = RequestMethod.GET)
    public Result delHelp(@RequestParam("id")Integer id) {
        HelpModel helpModel = new HelpModel();
        helpModel.setId(id);
        helpModel.setIsDel(1);

        int updateCnt = helpService.updateNotNull(helpModel);

        return ResponseUtil.success();
    }

    @ResponseBody
    @RequestMapping(value = "/treeNodes", method = RequestMethod.POST)
    public Object treeNodes(HelpModel helpModel) {
        helpModel.setIsDel(0);
        return helpService.getHelpTreeNodes(helpModel);
    }
}
