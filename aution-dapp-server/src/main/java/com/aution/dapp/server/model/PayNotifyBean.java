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

    private String businessNo; //灵光币订单号

    private BigDecimal amount; //总金额,单位为元

    private Integer status; //订单支付状态

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

	public String getBusinessNo() {
		return businessNo;
	}

	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	@Override
	public String toString() {
		return "PayNotifyBean{" +
				"tradeNo='" + tradeNo + '\'' +
				", businessNo='" + businessNo + '\'' +
				", amount=" + amount +
				", status='" + status + '\'' +
				", payTime='" + payTime + '\'' +
				'}';
	}

}
