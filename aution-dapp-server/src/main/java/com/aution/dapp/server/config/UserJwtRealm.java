///**
// * <p>上海中信信息发展股份有限公司 Copyright(c) 2018</p>
// * <p>http://www.cesgroup.com.cn/</p>
// * <p>2018-09-10 13:08 by huz</p>
// */
//
//package com.aution.dapp.server.config;
//
//import com.cesgroup.platform.commons.utils.JwtUtils;
//import com.cesgroup.platform.core.ApplicationContextUtils;
//import com.cesgroup.platform.core.security.SecurityUtils;
//import com.cesgroup.platform.core.security.model.IUser;
//import com.cesgroup.platform.shiro.token.JwtToken;
//import com.cesgroup.platform.shiro.token.TenantUsernamePasswordToken;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.security.SignatureException;
//import org.apache.shiro.authc.AuthenticationException;
//import org.apache.shiro.authc.AuthenticationInfo;
//import org.apache.shiro.authc.AuthenticationToken;
//import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
//import org.springframework.beans.factory.annotation.Value;
//
///**
// * JWT认证器
// *
// * @author huz
// * @version 0.1.0 2018-09-10
// */
//public class UserJwtRealm extends UserRealm {
//
//    @Value(value = "${platform.shiro.jwt-secret-key}")
//    private String secretKey;
//
//    /**
//     *
//     */
//    public UserJwtRealm() {
//        super();
//        //设置Token类型
//        setAuthenticationTokenClass(JwtToken.class);
//        //设置密码不做验证
//        setCredentialsMatcher(new AllowAllCredentialsMatcher());
//    }
//
//    /**
//     * @return the secretKey
//     */
//    public String getSecretKey() {
//        return secretKey;
//    }
//
//    /**
//     * @param secretKey
//     *            the secretKey to set
//     */
//    public void setSecretKey(final String secretKey) {
//        this.secretKey = secretKey;
//    }
//
//    /**
//     *
//     * @see org.apache.shiro.realm.AuthenticatingRealm
//     *      #doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)
//     */
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token)
//        throws AuthenticationException {
//        JwtToken jwtToken = (JwtToken) token;
//        String jwt = jwtToken.getToken();
//        String msg = null;
//        Claims claims = null;
//        try {
//            claims = JwtUtils.parseJwt(jwt, secretKey);
//        } catch (SignatureException e) {
//            msg = SecurityUtils.MESSAGE_JWT_SIGNATURE;
//        } catch (ExpiredJwtException e) {
//            msg = SecurityUtils.MESSAGE_JWT_EXPIRE;
//        } catch (Exception e) {
//            msg = SecurityUtils.MESSAGE_JWT_INVALID;
//        }
//        if (claims == null) {
//            msg = SecurityUtils.MESSAGE_JWT_INVALID;
//        }
//        if (msg != null) {
//            throw new AuthenticationException(ApplicationContextUtils.getMessage(msg));
//        }
//
//        //认证
//        IUser jwtUser = JwtUtils.claimsToSimpleUser(claims);
//        //不论是否为多租户都是用多租户认证Token
//        TenantUsernamePasswordToken authToken = new TenantUsernamePasswordToken(jwtUser.getLoginName(),
//            jwtUser.getTenantId(), "", jwtToken.getHost());
//        return super.doGetAuthenticationInfo(authToken);
//    }
//
//}
