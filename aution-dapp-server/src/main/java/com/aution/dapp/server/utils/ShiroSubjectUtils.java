package com.aution.dapp.server.utils;

import com.aution.dapp.server.model.ShiroUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**获取当前登录用户工具类
 * @author hewensheng
 */
public class ShiroSubjectUtils {

    public static ShiroUser getShiroUser(){
        Subject subject = SecurityUtils.getSubject();
        ShiroUser user = (ShiroUser)subject.getPrincipal();
        return user;
    }
    public static String getUserNo(){
        Subject subject = SecurityUtils.getSubject();
        ShiroUser user = (ShiroUser)subject.getPrincipal();
        return user.getLoginName();
    }
}
