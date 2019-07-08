package com.aution.dapp.server.core;

import java.io.Serializable;

public class ApiResult<T> implements Serializable {
  protected String code;
  protected String msg;
  protected T data;

  public ApiResult() { }

  

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

 

  public String getMsg() {
	return msg;
  }



  public void setMsg(String msg) {
	  this.msg = msg;
  }



  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }



  @Override
  public int hashCode() {
	  final int prime = 31;
	  int result = 1;
	  result = prime * result + ((code == null) ? 0 : code.hashCode());
	  result = prime * result + ((data == null) ? 0 : data.hashCode());
	  result = prime * result + ((msg == null) ? 0 : msg.hashCode());
	  return result;
  }



  @Override
  public boolean equals(Object obj) {
	  if (this == obj)
		  return true;
	  if (obj == null)
		  return false;
	  if (getClass() != obj.getClass())
		  return false;
	  ApiResult other = (ApiResult) obj;
	  if (code == null) {
		  if (other.code != null)
			  return false;
	  } else if (!code.equals(other.code))
		  return false;
	  if (data == null) {
		  if (other.data != null)
			  return false;
	  } else if (!data.equals(other.data))
		  return false;
	  if (msg == null) {
		  if (other.msg != null)
			  return false;
	  } else if (!msg.equals(other.msg))
		  return false;
	  return true;
  }

  @Override
  public String toString() {
	  return "ApiResult [code=" + code + ", msg=" + msg + ", data=" + data + "]";
  }

}
