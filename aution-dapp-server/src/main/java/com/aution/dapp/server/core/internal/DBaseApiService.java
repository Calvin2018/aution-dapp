package com.aution.dapp.server.core.internal;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.aution.dapp.server.core.AppContext;
import com.aution.dapp.server.core.BaseApiService;
import com.aution.dapp.server.core.RestApiResponse;
import com.aution.dapp.server.utils.SignUtil;
import com.google.gson.reflect.TypeToken;

public class DBaseApiService extends BaseApiService{

	public DBaseApiService(AppContext appContext) {
		super(appContext);
	}
	
	public DBaseApiService(AppContext appContext, Properties configuration) {
		super(appContext, configuration);
	}
    
	public <T> RestApiResponse<T> getToken(String timeStamp,String appid,String sign){
		
		return null;
	}
	public <T> RestApiResponse<T> doPayTransaction(String timeStamp,String appid,String ACCESS_TOKEN,String sign,String[] bodyParams){
		return null;
	}
}
