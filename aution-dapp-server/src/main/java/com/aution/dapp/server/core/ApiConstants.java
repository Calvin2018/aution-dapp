package com.aution.dapp.server.core;

public interface ApiConstants {

  String HEADER_ACCEPT = "Accept";
  String HEADER_CONTENT_TYPE = "Content-type";
  String HEADER_USER_AGENT = "User-Agent";
  String HEADER_COOKIE = "Cookie";
  String HEADER_USER_AGENT_MOZILLA = "Mozilla/5.0";

  String DA_ARG_DELIMITER = ";";

  String CODE_SUCCESS = "0";
  String CODE_REQUEST_EROR = "-1";
  String CODE_PARAM_INVALID = "1001";
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
  
  String PROP_COIN_SERVER_URL = "coin.server.url";
  String FORM_TOKEN = "token";
  String ACCESS_TOKEN_URL="access.token.url";
  String DA_APPID = "appid";
}
