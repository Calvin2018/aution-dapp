package com.aution.dapp.server.core.internal;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import java.util.Set;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aution.dapp.server.core.ApiConstants;
import com.aution.dapp.server.core.ApiException;
import com.aution.dapp.server.core.AppClient;
import com.aution.dapp.server.core.AppContext;
import com.aution.dapp.server.core.BaseApiService;
import com.aution.dapp.server.core.RestApiResponse;
import com.aution.dapp.server.model.BusinessRecord;
import com.aution.dapp.server.model.PayRequest;
import com.aution.dapp.server.utils.HttpRequests;
import com.aution.dapp.server.utils.JsonUtil;
import com.aution.dapp.server.utils.SignUtil;
import com.google.common.base.Strings;
import com.google.gson.reflect.TypeToken;

public class DBaseApiService extends BaseApiService{

	private static final Logger LOGGER = LoggerFactory.getLogger(DBaseApiService.class);
	public DBaseApiService(AppContext appContext) {
		super(appContext);
	}
	
	public DBaseApiService(AppContext appContext, Properties configuration) {
		super(appContext, configuration);
	}

	/**
	 * 查询用户信息
	 * @param token
	 * @param typeToken
	 * @param <T>
	 * @return
	 * @throws ApiException
	 * @throws IOException
	 */
	public <T> RestApiResponse<T> getUserInfo(String token,TypeToken<RestApiResponse<T>> typeToken) throws ApiException, IOException{
		if(Strings.isNullOrEmpty(token)&&null == typeToken) {
			throw new IllegalArgumentException("Arguments token and typeToken are required");
		}
		String url =  configuration.getProperty(ApiConstants.PROP_COIN_INFO_URL);
		String requestUrl = String.format(url,token);
		
		HttpPost hp = HttpRequests.newHttpPost2(requestUrl,new LinkedHashMap<>());
        
        HttpClientContext context = new HttpClientContext();
        CookieStore cookieStore = new BasicCookieStore();
        context.setCookieStore(cookieStore);
        
        return doPost(hp, context, typeToken);
		
	}

	/**
	 * 获取access_token
	 * @param appClient
	 * @return
	 * @throws IOException
	 */
	public  String accessToken(AppClient appClient) throws IOException{
		
		
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        Map<String, String> signParam = new LinkedHashMap<>();
        signParam.put("appid", configuration.getProperty(ApiConstants.DA_APPID));
        signParam.put("appsecret", configuration.getProperty(ApiConstants.DA_APPSECRET));
        signParam.put("timestamp", timestamp);
        String sign = SignUtil.createCommonSign(signParam);
        
        Map<String, String> params = new LinkedHashMap<>();
        params.put(ApiConstants.APPID, configuration.getProperty(ApiConstants.DA_APPID));
        params.put(ApiConstants.SIGN, sign);
        params.put(ApiConstants.TIMESTAMP, timestamp);
        
        String url = configuration.getProperty(ApiConstants.ACCESS_TOKEN_URL);
        HttpPost hp = HttpRequests.newHttpPost2(url, params);
        
        HttpClientContext context = new HttpClientContext();
        CookieStore cookieStore = new BasicCookieStore();
        context.setCookieStore(cookieStore);
        TypeToken<RestApiResponse<Map<String,String>>> typeToken = new TypeToken<RestApiResponse<Map<String,String>>>(){};
        Map<String,String> map = doPost(hp, context, typeToken).getData();
        String token = null;
        if(null != map) {
        	token = map.get("access_token");

        	if (Strings.isNullOrEmpty(token)) {
				throw new ApiException(Integer.parseInt(ApiConstants.CODE_EMPTY_RESULT), "Return an empty token");
			}
			appClient.setAccessToken(token);
        	// Store HttpContext.
            appContext.putIfAbsentHttpContext(token,context);
        }
        return token;
	}

	/**
	 * 创建订单
	 * @param appid
	 * @param accessToken
	 * @param payRequest
	 * @param appClient
	 * @return
	 * @throws IOException
	 */
	public String doOrder(String appid,String accessToken,PayRequest payRequest,AppClient appClient) throws IOException{
		
		if(Strings.isNullOrEmpty(appid)||null == payRequest) {
			throw new IllegalArgumentException("Arguments appid  payRequest and typeToken are required");
		}
		
		if(Strings.isNullOrEmpty(accessToken)) {
			accessToken = accessToken(appClient);
			appClient.setAccessToken(accessToken);
		}
		
		String jsonParam = JsonUtil.toSnakeJson(payRequest);
		
		@SuppressWarnings("unchecked")
		Map<String, String> params = JsonUtil.toObjectFromSnakeJson(jsonParam, Map.class);
		
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		Map<String, String> signParam = new LinkedHashMap<>();
        signParam.put("_body", jsonParam);	
        signParam.put("access_token", accessToken);
        signParam.put("appid", configuration.getProperty(ApiConstants.DA_APPID));
        signParam.put("appsecret", configuration.getProperty(ApiConstants.DA_APPSECRET));
        signParam.put("timestamp", timestamp);
        String sign = SignUtil.createCommonSign(signParam);
        
        
        String url = configuration.getProperty(ApiConstants.PROP_COIN_PAY_URL);
        String createOrderUrl = String.format(url,timestamp,appid,accessToken,sign);
        
        HttpPost hp = HttpRequests.newHttpPost2(createOrderUrl, params);
        
        HttpClientContext context = appContext.getHttpContext(accessToken);
        RestApiResponse<Object> result = null;
        try {
        	result = doPost(hp, context, new TypeToken<RestApiResponse<Object>>() {});
        }catch(ApiException e) {
        	if(String.valueOf(e.getStatusCode()).equals(ApiConstants.CODE_TOKEN_ERROR)) {
    		    accessToken = accessToken(appClient);
    		    appClient.setAccessToken(accessToken);
    		    signParam.put("access_token", accessToken);
    		    sign = SignUtil.createCommonSign(signParam);
    		    createOrderUrl = String.format(url,timestamp,appid,accessToken,sign);
    		    hp = HttpRequests.newHttpPost2(createOrderUrl, params);
    		    result = doPost(hp, context, new TypeToken<RestApiResponse<Object>>() {});
        	}else {
        		throw e;
        	}
        }
        String payUrl = null;
/*        if(payRequest.getAuthType().equals(ApiConstants.ApiPayAuthType.MERCHANT)) {
        	payUrl = (String)((Map<?, ?>)result.getData()).get("pay_url");
        	if(Strings.isNullOrEmpty(payUrl)) {
				throw new ApiException(Integer.parseInt(ApiConstants.CODE_REQUEST_EROR),"创建支付链接失败");
			}
        }else if(payRequest.getAuthType().equals(ApiConstants.ApiPayAuthType.COIN)) {
        	payUrl = (String)result.getData();
        	if(Strings.isNullOrEmpty(payUrl)) {
				throw new ApiException(Integer.parseInt(ApiConstants.CODE_REQUEST_EROR),"创建支付二维码失败");
			}
        }else {
        	throw new ApiException(Integer.parseInt(ApiConstants.CODE_REQUEST_EROR),"未知授权支付方式");
        }*/
        payUrl = (String)((Map<?, ?>)result.getData()).get("pay_url");
        if (Strings.isNullOrEmpty(payUrl)){
        	throw new ApiException(Integer.parseInt(ApiConstants.CODE_REQUEST_EROR),"创建支付链接失败");
		}
        LOGGER.info("payUrl: %s"+payUrl);
        return payUrl;
	}

	/**
	 * 下发
	 * @param appid
	 * @param accessToken
	 * @param businessRecords
	 * @param typeToken
	 * @param appClient
	 * @param <T>
	 * @return
	 * @throws IOException
	 */
	public <T> RestApiResponse<T> doIssue(String appid,String accessToken,
			List<BusinessRecord> businessRecords,TypeToken<RestApiResponse<T>> typeToken,AppClient appClient) throws IOException{

		if(Strings.isNullOrEmpty(appid)||null == businessRecords) {
			throw new IllegalArgumentException("Arguments appid  businessRecord and typeToken are required");
		}
		
		if(Strings.isNullOrEmpty(accessToken)) {
			accessToken = accessToken(appClient);
			appClient.setAccessToken(accessToken);
		}
		
		String jsonParam = JsonUtil.toSnakeJson(businessRecords);
		
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        Map<String, String> signParam = new LinkedHashMap<>();
        signParam.put("_body", jsonParam);
        signParam.put("access_token", accessToken);
        signParam.put("appid", configuration.getProperty(ApiConstants.DA_APPID));
        signParam.put("appsecret", configuration.getProperty(ApiConstants.DA_APPSECRET));
        signParam.put("timestamp", timestamp);
        String sign = SignUtil.createCommonSign(signParam);
        
        String url = configuration.getProperty(ApiConstants.PROP_COIN_ISSUE_URL);
        String createOrderUrl = String.format(url,timestamp,appid,accessToken,sign);
        
        @SuppressWarnings("unchecked")
		//Map<String, String> params = JsonUtil.toObjectFromSnakeJson(jsonParam, Map.class);
        
        HttpPost hp = HttpRequests.newHttpPost2(createOrderUrl, jsonParam);
        RestApiResponse<T> result = null;
        try {
        	result = doPost(hp, appContext.getHttpContext(accessToken), typeToken);
        }catch(ApiException e) {
        	if(String.valueOf(e.getStatusCode()).equals(ApiConstants.CODE_TOKEN_ERROR)) {
    		    accessToken = accessToken(appClient);
    		    appClient.setAccessToken(accessToken);
    		    signParam.put("access_token", accessToken);
    		    sign = SignUtil.createCommonSign(signParam);
    		    createOrderUrl = String.format(url,timestamp,appid,accessToken,sign);
    		    hp = HttpRequests.newHttpPost2(createOrderUrl, jsonParam);
    		    result = doPost(hp, appContext.getHttpContext(accessToken), typeToken);
        	}else {
        		throw e;
        	}
        }
		return result;
		
	}

	/**
	 * 查询用户余额  接口不提供
	 * @param appid
	 * @param accessToken
	 * @param userId
	 * @param amount
	 * @param feeAmount
	 * @param typeToken
	 * @param appClient
	 * @param <T>
	 * @return
	 * @throws IOException
	 */
	public <T> RestApiResponse<T> doQuery(String appid,String accessToken,String userId,String amount,String feeAmount,TypeToken<RestApiResponse<T>> typeToken,AppClient appClient) throws IOException{
		
		if(Strings.isNullOrEmpty(appid)) {
			throw new IllegalArgumentException("Arguments appid  typeToken are required");
		}
		if(Strings.isNullOrEmpty(accessToken)) {
			accessToken = accessToken(appClient);
			appClient.setAccessToken(accessToken);
		}
			
		if(Strings.isNullOrEmpty(amount)) {
			feeAmount = null;
		}
		if(!Strings.isNullOrEmpty(amount)&&Strings.isNullOrEmpty(feeAmount)) {
			feeAmount = "0";
		}
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		
		String jsonParam = "{\"user_id\":\"";
		jsonParam += userId;
		if(Strings.isNullOrEmpty(amount)) {
			jsonParam += "\"}";
		}else {
			jsonParam += "\",\"amount\":\"" + amount +"\",\"fee_amount\":\""+feeAmount+"\"}";
		}
		
        Map<String, String> signParam = new LinkedHashMap<>();
        signParam.put("_body", jsonParam);	
        signParam.put("access_token", accessToken);
        signParam.put("appid", configuration.getProperty(ApiConstants.DA_APPID));
        signParam.put("appsecret", configuration.getProperty(ApiConstants.DA_APPSECRET));
        signParam.put("timestamp", timestamp);
        String sign = SignUtil.createCommonSign(signParam);
        
        String url = configuration.getProperty(ApiConstants.PROP_COIN_QUERY_URL);
        String createOrderUrl = String.format(url,timestamp,appid,accessToken,sign);
        
        @SuppressWarnings("unchecked")
		Map<String, String> params = JsonUtil.toObjectFromSnakeJson(jsonParam, Map.class);
        
        HttpPost hp = HttpRequests.newHttpPost2(createOrderUrl, params);
        RestApiResponse<T> result = null;
        try {
        	result = doPost(hp, appContext.getHttpContext(accessToken), typeToken);
        }catch(ApiException e) {
        	if(String.valueOf(e.getStatusCode()).equals(ApiConstants.CODE_TOKEN_ERROR)) {
    		    accessToken = accessToken(appClient);
    		    appClient.setAccessToken(accessToken);
    		    signParam.put("access_token", accessToken);
    		    sign = SignUtil.createCommonSign(signParam);
    		    createOrderUrl = String.format(url,timestamp,appid,accessToken,sign);
    		    hp = HttpRequests.newHttpPost2(createOrderUrl, params);
    		    result = doPost(hp, appContext.getHttpContext(accessToken), typeToken);
        	}else {
        		throw e;
        	}
        }
		return result;
		
	}

	public <T> RestApiResponse<T> doQueryTxStatus(String appid,String accessToken,String tradeNo,TypeToken<RestApiResponse<T>> typeToken,AppClient appClient) throws IOException{

		if(Strings.isNullOrEmpty(appid)) {
			throw new IllegalArgumentException("Arguments appid   are required");
		}
		if(Strings.isNullOrEmpty(accessToken)) {
			accessToken = accessToken(appClient);
			appClient.setAccessToken(accessToken);
		}

		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

		String jsonParam = "{\"trade_no\":\"" +  tradeNo +"\"}";


		Map<String, String> signParam = new LinkedHashMap<>();
		signParam.put("_body", jsonParam);
		signParam.put("access_token", accessToken);
		signParam.put("appid", configuration.getProperty(ApiConstants.DA_APPID));
		signParam.put("appsecret", configuration.getProperty(ApiConstants.DA_APPSECRET));
		signParam.put("timestamp", timestamp);
		String sign = SignUtil.createCommonSign(signParam);

		String url = configuration.getProperty(ApiConstants.PROP_COIN_QUERY_TX_URL);
		String createOrderUrl = String.format(url,timestamp,appid,accessToken,sign);

		@SuppressWarnings("unchecked")
		Map<String, String> params = JsonUtil.toObjectFromSnakeJson(jsonParam, Map.class);

		HttpPost hp = HttpRequests.newHttpPost2(createOrderUrl, params);
		RestApiResponse<T> result = null;
		try {
			result = doPost(hp, appContext.getHttpContext(accessToken), typeToken);
		}catch(ApiException e) {
			if(String.valueOf(e.getStatusCode()).equals(ApiConstants.CODE_TOKEN_ERROR)) {
				accessToken = accessToken(appClient);
				appClient.setAccessToken(accessToken);
				signParam.put("access_token", accessToken);
				sign = SignUtil.createCommonSign(signParam);
				createOrderUrl = String.format(url,timestamp,appid,accessToken,sign);
				hp = HttpRequests.newHttpPost2(createOrderUrl, params);
				result = doPost(hp, appContext.getHttpContext(accessToken), typeToken);
			}else {
				throw e;
			}
		}
		return result;

	}
}
