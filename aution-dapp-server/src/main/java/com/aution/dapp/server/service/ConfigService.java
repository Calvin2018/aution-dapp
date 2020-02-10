package com.aution.dapp.server.service;

import com.aution.dapp.server.core.ApiConstants;
import com.aution.dapp.server.core.ApiException;
import com.aution.dapp.server.core.AppClient;
import com.aution.dapp.server.core.RestApiResponse;
import com.aution.dapp.server.core.internal.DBaseApiService;
import com.aution.dapp.server.model.*;
import com.aution.dapp.server.repository.*;
import com.aution.dapp.server.utils.GenerateNoUtil;
import com.google.common.base.Strings;
import com.google.gson.reflect.TypeToken;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 用于动态修改配置文件属性
 *
 * @author Administrator
 */
@Service
@Transactional
public class ConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigService.class);

    @Autowired
    private ConfigRepository configRepository;

    public Map<String,String> getServiceStatus(){
        LOGGER.info("获取服务状态");
        String key = "service_status";
        Map<String,String> data = configRepository.getServiceStatus(key);
        return data;
    }

    public boolean updateServiceStatus(String value){
        LOGGER.info("修改服务状态");
        String key = "service_status";
        Integer flag = configRepository.updateResult(key,value);
        return flag==0?false:true;
    }






}
