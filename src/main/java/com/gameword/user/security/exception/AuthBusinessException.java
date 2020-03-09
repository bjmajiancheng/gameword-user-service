package com.gameword.user.security.exception;

import com.gameword.user.common.exception.BusinessException;

/**
 * Created by cgj on 2016/4/10.
 */
public class AuthBusinessException extends BusinessException {
    @Override
    protected String getPropertiesPath() {
        return "/properties/business_code.properties";
    }

    public AuthBusinessException(int errCode) {
        super(errCode);
    }

    public AuthBusinessException(String message) {
        super(message);
    }
}
