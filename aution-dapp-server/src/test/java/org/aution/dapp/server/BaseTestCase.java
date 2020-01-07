package org.aution.dapp.server;

import com.aution.dapp.server.core.ApiConstants;
import com.aution.dapp.server.core.AppClient;
import com.aution.dapp.server.service.DappService;
import com.aution.dapp.server.service.GoodsService;
import com.aution.dapp.server.utils.HttpRequests;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;


import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseTestCase {
  private static final AppClient appClient = AppClient.getInstance();
  private DappService dappService;
  private GoodsService goodsService;

  protected static void doInit() throws IOException {
     //dappService = appClient.dBaseApiService();
  }

//  public static boolean getAuthToken() {
//    String loginUrl = "https://train.cesinstitute.com.cn/scoin-auth/login";
//    String username = "";
//    String password = "https://train.cesinstitute.com.cn/scoin-auth/login?appid=xftestmkhc5jv31rzfuklzf137&redirect_uri=http://testcbaas.cclcloud.net/cesbaas/index.html#/login&response_type=code&scope=userinfo&state=LUNXG65GAA1N4GNIU2";
//
//    Map<String, String> params = new LinkedHashMap<>();
//    params.put("username", username);
//    params.put("password", password);
//
//    HttpPost hp = HttpRequests.newHttpPost2(loginUrl,params);
//
//    HttpClientContext context = new HttpClientContext();
//    CookieStore cookieStore = new BasicCookieStore();
//    context.setCookieStore(cookieStore);
//
//  }

}
