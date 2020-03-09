package com.gameword.user.common.exception;

/**
 * 工程：os-app 创建人 : ChenGJ 创建时间： 2015/9/7 说明：
 */
public class DefaultBusinessException extends BusinessException {
    public DefaultBusinessException(int errCode) {
        super(errCode);
    }

    public DefaultBusinessException(String message) {
        super(message);
    }

    @Override
    protected String getPropertiesPath() {
        return "/properties/business_code.properties";
    }
}
