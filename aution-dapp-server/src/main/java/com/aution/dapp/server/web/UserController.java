package com.aution.dapp.server.web;

import com.aution.dapp.server.core.ApiConstants;
import com.aution.dapp.server.core.ApiException;
import com.aution.dapp.server.core.ApiResult;
import com.aution.dapp.server.model.ShiroUser;
import com.aution.dapp.server.service.DappService;
import com.google.common.base.Strings;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {


    @Autowired
    private DappService dappService;

    @RequestMapping(value="/getUserInfo",method= RequestMethod.POST)
    public ApiResult<Map<String,String>> getUserInfo() throws ApiException, IOException {

        Subject subject = SecurityUtils.getSubject();
        ShiroUser user = (ShiroUser)subject.getPrincipal();
        ApiResult<Map<String,String>> result = new ApiResult<Map<String,String>>();
        Map<String,String> map = null;
        try {

            if(!Strings.isNullOrEmpty(user.getLoginName())) {
                map =  dappService.getUserInfoUserId(user.getLoginName());
            }

            result.setCode(ApiConstants.CODE_SUCCESS);
            result.setMsg("");
            result.setData(map);

        }catch(IllegalArgumentException e) {
            result.setCode(ApiConstants.CODE_ARGS_ERROR);
            result.setMsg(e.getMessage());
            result.setData(null);
        }catch(ApiException e) {
            result.setCode(String.valueOf(e.getStatusCode()));
            result.setMsg(e.getMessage());
            result.setData(null);
        }
        return result;

    }
}
