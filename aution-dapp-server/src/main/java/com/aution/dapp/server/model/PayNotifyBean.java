package com.aution.dapp.server.model;

import com.aution.dapp.server.core.message.MsgIdGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

/**
 * 用户支付成功,通知商户订单已完成 
 *
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayNotifyBean extends BeanSigner implements MsgIdGenerator {

    private String tradeNo; //商户订单号

    private String coinTradeNo; //灵光币订单号

    private BigDecimal amount; //总金额,单位为元

    private String orderStatus; //订单支付状态

    @DateTimeFormat(pattern = "yyyyMMddHHmmss")
    private String payTime; //订单支付完成时间

    @Override
    @JsonIgnore
    public String getMessageId() {
        return tradeNo;
    }

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getCoinTradeNo() {
		return coinTradeNo;
	}

	public void setCoinTradeNo(String coinTradeNo) {
		this.coinTradeNo = coinTradeNo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public PayNotifyBean() {
		super();
	}

	@Override
	public String toString() {
		return "PayNotifyBean [tradeNo=" + tradeNo + ", coinTradeNo=" + coinTradeNo + ", amount=" + amount
				+ ", orderStatus=" + orderStatus + ", payTime=" + payTime + "]";
	}
    
}
