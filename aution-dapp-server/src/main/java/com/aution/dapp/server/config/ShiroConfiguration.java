package com.aution.dapp.server.config;

import com.cesgroup.authen4.ws.CoreHttpClientWS;
import com.cesgroup.authen4.ws.OrganizeHttpClientWS;
import com.cesgroup.platform.autoconfigure.shiro.AbstractShiroBaseWebConfiguration;
import com.cesgroup.platform.autoconfigure.shiro.PlatformShiroProperties;
import com.cesgroup.platform.autoconfigure.shiro.jwt.EnableShiroJWT;
import com.cesgroup.platform.shiro.ShiroConstants;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.*;


@Configuration
@EnableShiroJWT(EnableShiroJWT.Mode.SSO)
public class ShiroConfiguration  extends AbstractShiroBaseWebConfiguration{


    public ShiroConfiguration(PlatformShiroProperties properties) {
        super(properties);
    }

	//将自己的验证方式加入容器
    @Bean
    public UserRealm myShiroRealm() {
    	UserRealm myShiroRealm = new UserRealm();
        return myShiroRealm;
    }

    @Override
    @Bean(name="sessionManager")
    public DefaultWebSessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        return sessionManager;

    }
    @Bean
    public UserJwtRealm userJwtRealm() {
        UserJwtRealm realm = new UserJwtRealm();
        realm.setSecretKey(properties.getJwtSecretKey());
        //使用缓存
        realm.setCachingEnabled(true);
        realm.setCacheManager(cacheManager);
        realm.setAuthenticationCachingEnabled(true);
        realm.setAuthenticationCacheName(Optional.ofNullable(properties.getAuthenticationCache())
                .map(c -> c.getName()).orElse(ShiroConstants.CACHE_AUTHENTICATION));
        realm.setAuthorizationCachingEnabled(true);
        realm.setAuthorizationCacheName(Optional.ofNullable(properties.getAuthorizationCache())
                .map(c -> c.getName()).orElse(ShiroConstants.CACHE_AUTHORIZATION));
        return realm;
    }
    //权限管理，配置主要是Realm的管理认证
    @Bean(value = "securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        List<Realm> realms = new ArrayList<>();
        realms.add(myShiroRealm());
        realms.add(userJwtRealm());
        securityManager.setRealms(realms);
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }
    @Bean
    public SimpleMappingExceptionResolver unauthoriedCatch(){
    	SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();
    	Properties exception = new Properties();
    	//value 是页面的名称不是controller中的路径
    	exception.setProperty("org.apache.shiro.authz.UnauthorizedException", "unauthorized");
    	exceptionResolver.setExceptionMappings(exception);
    	return exceptionResolver;
    }

    @Bean
    public CoreHttpClientWS coreHttpClientWS(){
        return CoreHttpClientWS.getCoreHttpClientWS();
    }

    @Bean
    public OrganizeHttpClientWS organizeHttpClientWS(){
        return OrganizeHttpClientWS.getOrganizeHttpClientWS();
    }


//    @Override
//    public void configureFilterChainDefinition(ShiroFilterChainDefinitionBuilder builder) {
//        super.configureFilterChainDefinition(builder);
//        builder.defaultPath(ShiroConstants.FILTER_USER);
//        builder.path("/login", ShiroConstants.FILTER_ANON);
//        builder.path("/scoin", ShiroConstants.FILTER_ANON);
//    }
//
//    @Override
//    public void configureFilter(ShiroWebFiltersBuilder builder) {
//
//    }
}
