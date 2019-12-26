package com.aution.dapp.server.model;


import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 业务交易记录表
 *
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusinessRecord {

    // 第三方支付交易号
    private String tradeNo;

    //'用户id':是用户表中的job_number
    private String userNo;

    //订单实际需要支付金额
    private BigDecimal amount;

    //交易名称，为空，则为“应用下发
	private String title;

    //简易商品信息
//    private String goodsDetail;

	private String remarks;
    //第三方用于显示消费的标题（比如：饿了么)
//    private String shopApp;

    //第三方用于展示消费的图标
//    private String shopIcon;

	//后台回调URL，交易开始和交易完成时回调
	private String notifyUrl;

	//交易租户，长度32位字节，跨租户交易时使用。为空则表示为当前租户交易
	private String tradeTenantNo;

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

/*	public String getGoodsDetail() {
		return goodsDetail;
	}

	public void setGoodsDetail(String goodsDetail) {
		this.goodsDetail = goodsDetail;
	}

	public String getShopApp() {
		return shopApp;
	}

	public void setShopApp(String shopApp) {
		this.shopApp = shopApp;
	}

	public String getShopIcon() {
		return shopIcon;
	}

	public void setShopIcon(String shopIcon) {
		this.shopIcon = shopIcon;
	}*/

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getTradeTenantNo() {
		return tradeTenantNo;
	}

	public void setTradeTenantNo(String tradeTenantNo) {
		this.tradeTenantNo = tradeTenantNo;
	}

	public BusinessRecord() {
		super();
	}

	@Override
	public String toString() {
		return "BusinessRecord{" +
				"tradeNo='" + tradeNo + '\'' +
				", userNo='" + userNo + '\'' +
				", amount=" + amount +
				", title='" + title + '\'' +
				", remarks='" + remarks + '\'' +
				", notifyUrl='" + notifyUrl + '\'' +
				", tradeTenantNo='" + tradeTenantNo + '\'' +
				'}';
	}
}
