package com.gameword.user.common.utils;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by majiancheng on 2020/3/22.
 */
public class MailAuthenticator extends Authenticator {

    private String userName = null;

    private String password = null;

    public MailAuthenticator() {
    }

    public MailAuthenticator(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}
