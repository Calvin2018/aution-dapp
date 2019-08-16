package com.aution.dapp.server.core;

public interface ApiConstants {

  String HEADER_ACCEPT = "Accept";
  String HEADER_CONTENT_TYPE = "Content-type";
  String HEADER_USER_AGENT = "User-Agent";
  String HEADER_COOKIE = "Cookie";
  String HEADER_USER_AGENT_MOZILLA = "Mozilla/5.0";

  String APPLICATION_JSON = "application/json";
  String UTF_8 = "UTF-8";
  String DA_ARG_DELIMITER = ";";
  String DA_IMG_FILENAME = "dapp.img.filename";

  String CODE_SUCCESS = "0";
  String CODE_REQUEST_EROR = "-1";
  String CODE_EMPTY_RESULT = "1002";
  String CODE_PARAM_INVALID = "1001";
  String CODE_PRICE_ERROR = "1003";
  String CODE_ARGS_ERROR = "1004";
  String CODE_TIMT_OUT = "1005";
  String CODE_PARAM_EROR = "40001";
  String CODE_TIMESTAMP_EXPIRED = "40002";
  String CODE_TOKEN_ERROR = "40003";
  String CODE_ORDER_REPEAT = "40004";
  String CODE_SIGN_ERROR = "40005";
  String CODE_APPID_ERROR = "40006";
  String CODE_USERID_NULL = "40007";
  String CODE_TRANSACTION_EXCEPTION = "50001";
  String CODE_CHECK_FAILED = "50002";
  String CODE_INSUFFICIENT_BALANCE = "50003";
  String CODE_TRANSACTION_NOT_EXIST = "50004";
  String CODE_REFUND_FAILED = "50005";
  String CODE_FEE_FAILED = "50006";
  String CODE_CORRECT_FAILED = "50007";
  String CODE_OPERATE_FREQUENTLY = "50008";
  String CODE_TRY_AGAIN_LATER = "50009";
  String CODE_ORDER_COMPLETED = "500010";  
  
  String PROP_COIN_AUTH_URL= "dapp.auth.token.url";
  String PROP_COIN_LOGIN_URL = "dapp.access.login.url";
  String PROP_COIN_INFO_URL = "dapp.coin.info.url";
  String PROP_COIN_PAY_URL = "dapp.coin.pay.url";
  String PROP_COIN_ISSUE_URL = "dapp.coin.issue.url";
  String PROP_COIN_QUERY_URL = "dapp.coin.balance.url";
  String FORM_TOKEN = "token";
  String ACCESS_TOKEN_URL="dapp.access.token.url";
  String DA_APPID = "dapp.appId";
  String DA_APPSECRET = "dapp.appSecret";
  String SIGN = "sign";
  String APPID = "appid";
  String TIMESTAMP = "timestamp";
  String X_TRADE_NO = "x_trade_no";
  String ACCESS_TOKEN = "access.token";
  String DA_SUCCESS = "SUCCESS";
  String DA_PENDING  = "PENDING ";
  String DA_CANCEL = "CANCEL";
  String DA_NOTIFY_URL = "dapp.pay.notify.url";
  String DA_DETAIL_URL = "dapp.pay.order.detail.url";
  
  enum ApiPayAuthType {
      /**
       * “MERCHANT”：商户调用灵光币支付接口时，已有灵光币用户信息
       */
      MERCHANT("MERCHANT"),
      /**
       * “COIN”：商户调用灵光币支付接口时，无灵光币用户信息
       */
      COIN("COIN");

      private String value;

      ApiPayAuthType(String value) {
          this.value = value;
      }

      public String getValue() {
    	  return value;
      }

      public void setValue(String value) {
    	  this.value = value;
      }
      
  }
}
