package com.gameword.user.common.api;

import com.gameword.user.common.service.IAttachmentService;
import com.gameword.user.common.utils.*;
import com.gameword.user.core.model.UserModel;
import com.gameword.user.user.model.*;
import com.gameword.user.common.dto.QueryDto;
import com.gameword.user.common.tools.RedisCache;
import com.gameword.user.core.model.Attachment;
import com.gameword.user.security.constants.SecurityConstant;
import com.gameword.user.security.service.IUserService;
import com.gameword.user.user.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by majiancheng on 2019/9/23.
 */
@Controller
@RequestMapping("/api/common")
public class CommonController {

    private final static Integer IMAGE = 0;

    private final static Integer FILE = 1;

    private final static Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private IAttachmentService attachmentService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private IVerifyCodeService verifyCodeService;

    @Autowired
    private IUserService userService;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private ICountryService countryService;

    @Autowired
    private ICityService cityService;

    @Autowired
    private ISystemVersionService systemVersionService;

    @Autowired
    private IRegionService regionService;

    @Autowired
    private IPriceConfService priceConfService;

    @RequestMapping(value = "/uploadFile", method = { RequestMethod.POST })
    @ResponseBody
    public Result uploadFile(HttpServletRequest request,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseUtil.error("请选择文件。");
            }
            //文件夹名
            String dirName = "file";
            // 最大文件大小
            long maxSize = 1024 * 1024 * 100;

            // 定义允许上传的文件扩展名
            HashMap<String, String> extMap = new HashMap<String, String>();
            extMap.put(dirName,
                    "doc,docx,xls,xlsx,ppt,pptx,txt,zip,rar,gz,bz2,gif,jpg,jpeg,png,bmp,swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb,mp4");

            Attachment attachment = attachmentService
                    .uploadFileAttachement(request, file, dirName, maxSize, extMap, FILE);
            return ResponseUtil.success(attachment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.error("上传失败。");
        }
    }

    @RequestMapping(value = "/uploadImage", method = { RequestMethod.POST })
    @ResponseBody
    public Result uploadImage(HttpServletRequest request,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseUtil.error("请选择文件。");
            }
            //文件夹名
            String dirName = "image";
            // 最大文件大小
            long maxSize = 1024 * 1024 * 100;

            // 定义允许上传的文件扩展名
            HashMap<String, String> extMap = new HashMap<String, String>();
            extMap.put(dirName, "gif,jpg,jpeg,png,bmp");

            logger.info("update Image fileName:{}.", file.getOriginalFilename());
            Attachment attachment = attachmentService
                    .uploadFileAttachement(request, file, dirName, maxSize, extMap, IMAGE);
            return ResponseUtil.success(attachment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.error("上传失败。");
        }
    }

    @RequestMapping(value = "/uploadFiles", method = RequestMethod.POST)
    @ResponseBody
    public Object uploadFiles(HttpServletRequest request,
            @RequestParam(value = "files[]", required = false) MultipartFile[] file) throws Exception {
        if (file.length > 0) {
            for (MultipartFile multipartFile : file) {
                //文件夹名
                String dirName = "file";
                // 最大文件大小
                long maxSize = 1024 * 1024 * 100;

                // 定义允许上传的文件扩展名
                HashMap<String, String> extMap = new HashMap<String, String>();
                extMap.put(dirName,
                        "doc,docx,xls,xlsx,ppt,pptx,txt,zip,rar,gz,bz2,gif,jpg,jpeg,png,bmp,swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb,mp4");

                Attachment attachment = attachmentService
                        .uploadFileAttachement(request, multipartFile, dirName, maxSize, extMap, FILE);
                return attachment;
            }
        }
        return false;
    }

    @RequestMapping(value = "/attachment", method = RequestMethod.POST)
    @ResponseBody
    public Result attachment(@RequestParam("attachmentId") Integer attachmentId) {
        try {
            if (attachmentId != null) {
                Attachment attachment = attachmentService.selectByKey(attachmentId);
                if (attachment != null) {
                    return ResponseUtil.success(attachment);
                }
            }
            return ResponseUtil.error("附件id不存在。");
        } catch(Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }

    /**
     * 发送验证码
     *
     * @param queryDto
     */
    @ResponseBody
    @RequestMapping(value = "/verifyCode/sendVerifyCode", method = RequestMethod.POST)
    public Result sendCaptchaCode(@RequestBody QueryDto queryDto) {
        String mobilePhone = queryDto.getMobilePhone();
        if(StringUtils.isEmpty(mobilePhone) || queryDto.getType() == null) {
            return ResponseUtil.error("请输入手机号码和类型");
        }

        try {
            String verifyCode = "";
            Object obj = redisCache.get(SecurityConstant.MOBILE_VERIFY_CODE_PREFIX + mobilePhone);
            if(obj != null) {
                verifyCode = String.valueOf(obj);
            }

            //取消重复发送 验证
            /*if(StringUtils.isNotEmpty(verifyCode)) {
                return ResponseUtil.error("验证码还在有效期内,请输入之前验证码。");
            }*/

            //注册逻辑判断
            if(queryDto.getType() == 1) {
                UserModel userModel = userService.findUserByMobilePhone(queryDto.getMobilePhone());
                if(userModel != null) {
                    return ResponseUtil.error("该手机号已注册");
                }
            }


            verifyCode = String.valueOf(new Random().nextInt(8999) + 1000);

            String key = "sms_0000000003";
            String[] values = {verifyCode};


            String msgContent = messageUtil.getProperty(key, values);

            VerifyCodeModel verifyCodeModel = new VerifyCodeModel();
            verifyCodeModel.setMobilePhone(mobilePhone);
            verifyCodeModel.setVerifyCode(verifyCode);
            verifyCodeModel.setContent(msgContent);
            verifyCodeModel.setCtime(new Date());
            verifyCodeService.saveNotNull(verifyCodeModel);

            //验证码设置缓存信息
            redisCache.set(SecurityConstant.MOBILE_VERIFY_CODE_PREFIX + mobilePhone, verifyCode, SecurityConstant.THREE_MINUTES_EXPIRE_SECOND);

            Result result = messageUtil.sendMessage(mobilePhone, key, values);

            return ResponseUtil.success("验证码发送成功");
        } catch(Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }

    /**
     * 发送验证码
     *
     * @param queryDto
     */
    @ResponseBody
    @RequestMapping(value = "/sendEmailVerifyCode", method = RequestMethod.POST)
    public Result sendEmailVerifyCode(@RequestBody QueryDto queryDto) {
        String email = queryDto.getEmail();
        if(StringUtils.isEmpty(email) || queryDto.getType() == null) {
            return ResponseUtil.error("请输入邮箱和类型");
        }

        try {
            String verifyCode = "";
            Object obj = redisCache.get(SecurityConstant.MOBILE_VERIFY_CODE_PREFIX + email);
            if(obj != null) {
                return ResponseUtil.error("验证码还在有效期, 请查看邮箱");
            }

            //注册逻辑判断
            if(queryDto.getType() == 1) {
                UserModel userModel = userService.findUserByEmail(queryDto.getEmail());
                if(userModel != null) {
                    return ResponseUtil.error("该邮箱已注册");
                }
            }


            verifyCode = String.valueOf(new Random().nextInt(8999) + 1000);

            String key = "sms_0000000003";
            String[] values = {verifyCode};


            String msgContent = messageUtil.getProperty(key, values);

            VerifyCodeModel verifyCodeModel = new VerifyCodeModel();
            verifyCodeModel.setEmail(email);
            verifyCodeModel.setVerifyCode(verifyCode);
            verifyCodeModel.setContent(msgContent);
            verifyCodeModel.setCtime(new Date());
            verifyCodeService.saveNotNull(verifyCodeModel);

            //验证码设置缓存信息
            redisCache.set(SecurityConstant.MOBILE_VERIFY_CODE_PREFIX + email, verifyCode, SecurityConstant.THREE_MINUTES_EXPIRE_SECOND);

            Result result = mailSender.sendEmail(queryDto.getEmail(), "验证码", msgContent);

            return ResponseUtil.success("验证码发送成功");
        } catch(Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }

    /**
     * 国家列表
     *
     * @param queryDto
     */
    @ResponseBody
    @RequestMapping(value = "/countryList", method = RequestMethod.POST)
    public Result countryList(@RequestBody QueryDto queryDto) {

        try {
            List<RegionModel> regionModels = regionService.find(Collections.singletonMap("parentId", 0));
            if (CollectionUtils.isNotEmpty(regionModels)) {
                regionModels.sort(new Comparator<RegionModel>() {
                    @Override
                    public int compare(RegionModel o1, RegionModel o2) {
                        String o1CnNamePinyin = JPinyinUtils.covertToPinyinString(o1.getRegionCnName());
                        String o1EnNamePinyin = JPinyinUtils.covertToPinyinString(o1.getRegionEnName());

                        String o2CnNamePinyin = JPinyinUtils.covertToPinyinString(o2.getRegionCnName());
                        String o2EnNamePinyin = JPinyinUtils.covertToPinyinString(o2.getRegionEnName());
                        if(queryDto.getLanguage() == 1) {
                            return o1CnNamePinyin.compareTo(o2CnNamePinyin);
                        } else if(queryDto.getLanguage() == 2) {
                            return o1EnNamePinyin.compareTo(o2EnNamePinyin);
                        }

                        return 0;
                    }
                });
            }

            return ResponseUtil.success(Collections.singletonMap("countrys", regionModels));
        } catch(Exception e) {
            e.printStackTrace();

            return ResponseUtil.error("系统异常, 请稍后重试。");
        }
    }

    /**
     * 城市列表
     *
     * @param queryDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/cityList", method = RequestMethod.POST)
    public Result cityList(@RequestBody QueryDto queryDto) {

        try {
            List<RegionModel> regionModels = regionService.find(Collections.singletonMap("parentId", queryDto.getCountryId()));
            if (CollectionUtils.isNotEmpty(regionModels)) {
                regionModels.sort(new Comparator<RegionModel>() {
                    @Override
                    public int compare(RegionModel o1, RegionModel o2) {
                        String o1CnNamePinyin = JPinyinUtils.covertToPinyinString(o1.getRegionCnName());
                        String o1EnNamePinyin = JPinyinUtils.covertToPinyinString(o1.getRegionEnName());

                        String o2CnNamePinyin = JPinyinUtils.covertToPinyinString(o2.getRegionCnName());
                        String o2EnNamePinyin = JPinyinUtils.covertToPinyinString(o2.getRegionEnName());
                        if(queryDto.getLanguage() == 1) {
                            return o1CnNamePinyin.compareTo(o2CnNamePinyin);
                        } else if(queryDto.getLanguage() == 2) {
                            return o1EnNamePinyin.compareTo(o2EnNamePinyin);
                        }

                        return 0;
                    }
                });
            }

            return ResponseUtil.success(Collections.singletonMap("citys", regionModels));
        } catch(Exception e) {
            e.printStackTrace();

            return ResponseUtil.error();
        }
    }

    /**
     * 最新版本
     *
     * @param queryDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/lastVersion", method = RequestMethod.POST)
    public Result lastVersion(@RequestBody QueryDto queryDto) {

        try {
            List<SystemVersionModel> systemVersions = systemVersionService.selectByFilter(new SystemVersionModel());
            Map<String, SystemVersionModel> systemVersionMap = new HashMap<String, SystemVersionModel>();

            if(CollectionUtils.isNotEmpty(systemVersions)) {
                for(SystemVersionModel systemVersion : systemVersions) {
                    systemVersionMap.put(systemVersion.getAppType(), systemVersion);
                }
            }

            return ResponseUtil.success(systemVersionMap);

        } catch(Exception e) {
            e.printStackTrace();

            return ResponseUtil.error();
        }
    }

    /**
     * 最新版本
     *
     * @param queryDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/currPriceConf", method = RequestMethod.POST)
    public Result currPriceConf(@RequestBody QueryDto queryDto) {
        try {
            List<PriceConfModel> priceConfModels = priceConfService.selectByFilter(new PriceConfModel());
            PriceConfModel priceConfModel = null;
            if(CollectionUtils.isNotEmpty(priceConfModels)) {
                priceConfModel = priceConfModels.get(0);
            } else {
                priceConfModel = new PriceConfModel();
                priceConfModel.setPriceEn(BigDecimal.ZERO);
                priceConfModel.setPriceCn(BigDecimal.ZERO);
            }

            return ResponseUtil.success(Collections.singletonMap("priceConf", priceConfModel));

        } catch(Exception e) {
            e.printStackTrace();

            return ResponseUtil.error();
        }
    }

}
