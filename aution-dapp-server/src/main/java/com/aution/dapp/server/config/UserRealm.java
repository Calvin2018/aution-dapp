package com.aution.dapp.server.config;


import com.aution.dapp.server.model.ShiroUser;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;


public class UserRealm extends AuthorizingRealm{


	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

		return info;
	}


	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		ShiroUser user = (ShiroUser) token.getPrincipal();

		return new SimpleAuthenticationInfo(user,user.getPassword(),getName());
	}

	@Override
	public boolean supports(AuthenticationToken authenticationToken) {
		return authenticationToken instanceof MyUsernamePasswordToken;
	}
}
