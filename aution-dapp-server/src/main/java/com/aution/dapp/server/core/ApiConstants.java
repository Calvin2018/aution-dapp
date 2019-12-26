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

  String DA_COMMA = ",";
  String DA_SEMICOLON = ";";

  String CODE_EMPTY_RESULT = "100002";
  String CODE_PRICE_ERROR = "100003";
  String CODE_ARGS_ERROR = "100004";
  String CODE_TIMT_OUT = "100005";

  /**
   * 成功
   */
  String CODE_SUCCESS = "0";
  /**
   * 请求异常,请联系管理员
   */
  String CODE_REQUEST_EROR = "-1";
  /**
   * 请求参数异常
   */
  String CODE_PARAMS_ERROR = "-1000";
  /**
   * 服务调用异常
   */
  String CODE_SERVER_ERROR = "-1001";
  /**
   * 账号不存在
   */
  String CODE_ACCOUNT_NOT_EXSIT = "1001";
  /**
   * 余额不足
   */
  String CODE_INSUFFICIENT_BALANCE = "1002";
  /**
   * 时间戳已过期
   */
  String CODE_TIMESTAMP_EXPIRED = "2003";
  /**
   * token无效或已过期
   */
  String CODE_TOKEN_ERROR = "2004";
  /**
   * 签名校验失败
   */
  String CODE_SIGN_ERROR = "2005";
  /**
   * 请使用正确的appId
   */
  String CODE_APPID_ERROR = "2006";
  /**
   * 交易类型错误
   */
  String CODE_TRANSACTION_EXCEPTION = "3010";
  /**
   * 该交易不存在
   */
  String CODE_TRANSACTION_NOT_EXIST = "3002";
  /**
   * 该订单已完成
   */
  String CODE_ORDER_COMPLETED = "3015";
  /**
   * 过风险期不能退款
   */
  String CODE_CANT_REFUND = "3022";
  /**
   * 授权码过期或不存在
   */
  String CODE_AUTH_INVALID="7003";

  String PROP_COIN_AUTH_URL= "dapp.auth.code.url";
  String PROP_COIN_INFO_URL = "dapp.coin.info.url";
  String PROP_COIN_PAY_URL = "dapp.coin.pay.url";
  String PROP_COIN_ISSUE_URL = "dapp.coin.issue.url";
  String PROP_COIN_QUERY_URL = "dapp.coin.balance.url";
  String PROP_COIN_QUERY_TX_URL = "dapp.query.tx.url";
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

  String DA_STATE = "dapp.state";
  /**
   * 灵光币平台提供，固定
   */
  String DA_RESPONSE_TYPE="code";
  String DA_URL = "dapp.url";
  
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
