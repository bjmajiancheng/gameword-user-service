package com.gameword.user.common.api;

import com.gameword.user.common.tools.RedisCache;
import com.gameword.user.common.utils.MessageUtil;
import com.gameword.user.user.service.IVerifyCodeService;
import com.gameword.user.security.service.IUserService;
import com.google.code.kaptcha.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by majiancheng on 2019/9/17.
 */
@Controller
@RequestMapping("/api/noneAuth")
public class NoneAuthController {

    private static final Logger logger = LoggerFactory.getLogger(NoneAuthController.class);

    @Autowired
    private Producer producer;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private IVerifyCodeService verifyCodeService;

    @Autowired
    private IUserService userService;

    @RequestMapping("/captcha")
    public void captcha(@RequestParam("vkey") String vkey, HttpServletRequest httpServletRequest,
            HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);

        redisCache.set(vkey, text, 120);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
    }

}
