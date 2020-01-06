package com.aution.dapp.server.config;

import com.aution.dapp.server.model.ShiroUser;
import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

public class MyUsernamePasswordToken implements HostAuthenticationToken, RememberMeAuthenticationToken {

    private ShiroUser user;

    private String host;
    private boolean rememberMe;

    public void setUser(ShiroUser user) {
        this.user = user;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    @Override
    public Object getCredentials() {
        return user.getPassword();
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public MyUsernamePasswordToken(ShiroUser user) {
        this.user = user;
    }
}
