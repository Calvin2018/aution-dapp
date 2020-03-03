package com.aution.dapp.server.model;

import com.cesgroup.platform.core.security.model.IUser;

public class ShiroUser implements IUser {

    private String userName;
    private String loginName;
    private String password;
    private String tenantId;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getLoginName() {
        return loginName;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getSalt() {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
