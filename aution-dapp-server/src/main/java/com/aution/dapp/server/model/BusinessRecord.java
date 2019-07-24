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
    private String userId;

    //订单实际需要支付金额
    private BigDecimal amount;

    //简易商品信息
    private String goodsDetail;

    //第三方用于显示消费的标题（比如：饿了么)
    private String shopApp;

    //第三方用于展示消费的图标
    private String shopIcon;

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getGoodsDetail() {
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
	}

	public BusinessRecord() {
		super();
	}

	@Override
	public String toString() {
		return "BusinessRecord [tradeNo=" + tradeNo + ", userId=" + userId + ", amount=" + amount + ", goodsDetail="
				+ goodsDetail + ", shopApp=" + shopApp + ", shopIcon=" + shopIcon + "]";
	}
	
}
