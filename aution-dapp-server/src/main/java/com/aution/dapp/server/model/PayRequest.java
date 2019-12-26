package com.aution.dapp.server.model;

import com.aution.dapp.server.core.ApiConstants;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

/**
 * 第三方创建灵光币支付预订单所需参数
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayRequest {

    /**
     * <pre>
     * 字段名：授权支付方式
     * 是否必填：是
     * 类型：String(16)
     * 示例值:"MERCHANT"
     * 描述：“MERCHANT”：商户自行调用灵光币相关授权接口，获取用户信息。
     *                    并在调用支付接口时，传入userId。
     *       “COIN”：商户展示灵光币返回的支付二维码，
     *                 用户扫码后，灵光币自行获取用户信息，让用户进入相应支付页面
     * 默认支付方式：MERCHANT
     * </pre>
     */
//    private ApiConstants.ApiPayAuthType authType = ApiConstants.ApiPayAuthType.MERCHANT;

    /**
     * <pre>
     * 字段名：商品简单描述(用于灵光币账单展示)
     * 是否必填：是
     * 类型：String(16)
     * 示例值:"红包"
     * 描述：商品标题
     * </pre>
     */
    private String title;

    /**
     * <pre>
     * 字段名：总金额
     * 是否必填：是
     * 类型：Decimal(10,2)
     * 示例值:8.88
     * 描述：订单总金额，单位为元(最多2位小数)
     * </pre>
     */
    private BigDecimal amount;

    /**
     * <pre>
     * 字段名：商户订单号
     * 是否必填：是
     * 类型：String(32)
     * 描述：第三方系统内部的订单号
     * </pre>
     */
    private String tradeNo;

    /**
     * <pre>
     * 字段名：通知地址
     * 是否必填：是
     * 类型：String(255)
     * 描述：商户提供的Url地址:用于灵光币回调通知商户，用户已完成相关订单的支付(不能带任何参数)
     * (因为最后会在商户提供的notifyUrl后拼上"?sign=XXX")
     * </pre>
     */
    private String notifyUrl;

    /**
     * <pre>
     * 字段名：用户识别号
     * 是否必填：否
     * 类型：String(32)
     * 示例值:"cf25bccf9d03e9a5"
     * 描述：灵光币用户标识(为用户表中的job_number)
     * "MERCHANT"授权支付必传；
     * "COIN"授权支付不需要传；
     * （其获取方法参见“灵光币授权获取用户信息”相关接口）
     * </pre>
     */
    private String userNo;

    /**
     * <pre>
     * 字段名：订单详情页
     * 是否必填：否
     * 类型：String(255)
     * 描述：商户方提供的订单详情页（用户支付成功后查看，建议提供）
     * (若不传,默认设置为灵光币首页)
     * </pre>
     */
    private String orderDetailUrl;

    /**
     * <pre>
     * 字段名：商品详情
     * 是否必填：否
     * 类型：String(6000)
     * 示例值:{  "goods_detail":[
     *       {
     *       "goods_id":"2c918082691921a20169357af3b90010",
     *       "goods_name":"小米手机",
     *       "price":5288.01,
     *       "goods_num":1,
     *       "body":"最新款,旗舰版"
     *       },
     *       {
     *       "goods_id":"2c918082691921a20169357af3b90011",
     *       "goods_name":"HUAWEI 64G",
     *       "price":6280.01,
     *       "goods_num":2,
     *       "body":"最新款,旗舰版"
     *       }
     *       ]
     *       }
     * 描述：商品详细列表，使用Json格式
     *  goods_detail []：
     *  └ goods_id   String 必填 64   商品编号
     *  └ goods_name String 必填 64   商品名称
     *  └ goods_num  Int    必填      商品数量
     *  └ price      Decimal(10,2)    必填      商品单价，单位为元
     *  └ body       String 可选 1000 商品描述信息
     * </pre>
     */
    private String detail;

    /**
     * <pre>
     * 字段名：订单多少秒后失效
     * 是否必填：否
     * 类型：Int
     * 示例值：3600
     * 描述：1.最短失效时间必须大于1分钟(60s),最长失效时间不能大于24小时(86400s)
     *      2.若未填expire_seconds,则认为订单不会失效(此时请确保商品无论何时都有库存)
     * 默认值： 20分钟，在规定时间内没有支付则取消交易
     * </pre>
     */
//    private Integer expireSeconds = 1200;

	/**
	 * <pre>
	 * 字段名：支付后确认按钮跳转的链接地址
	 * 是否必填：否
	 * 类型：String
	 * </pre>
 	 */
	private String redirect_url;

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getRedirect_url() {
		return redirect_url;
	}

	public void setRedirect_url(String redirect_url) {
		this.redirect_url = redirect_url;
	}

	/*	public ApiConstants.ApiPayAuthType getAuthType() {
		return authType;
	}

	public void setAuthType(ApiConstants.ApiPayAuthType authType) {
		this.authType = authType;
	}*/

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getOrderDetailUrl() {
		return orderDetailUrl;
	}

	public void setOrderDetailUrl(String orderDetailUrl) {
		this.orderDetailUrl = orderDetailUrl;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

/*	public Integer getExpireSeconds() {
		return expireSeconds;
	}

	public void setExpireSeconds(Integer expireSeconds) {
		this.expireSeconds = expireSeconds;
	}*/

	public PayRequest() {
		super();
	}

	@Override
	public String toString() {
		return "PayRequest{" +
				"title='" + title + '\'' +
				", amount=" + amount +
				", tradeNo='" + tradeNo + '\'' +
				", notifyUrl='" + notifyUrl + '\'' +
				", userNo='" + userNo + '\'' +
				", orderDetailUrl='" + orderDetailUrl + '\'' +
				", detail='" + detail + '\'' +
				", redirect_url='" + redirect_url + '\'' +
				'}';
	}
}
