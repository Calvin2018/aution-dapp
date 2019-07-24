package com.aution.dapp.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="t_history")
public class History {

	//交易号
	@Id
	@Column(name="trade_no")
	private String tradeNo;
	//商品id
	@Column(name="goods_id")
	private String goodsId;
	//用户id
	@Column(name="user_id")
	private String userId;
	//竞拍价
	@Column(name="bid_price")
	private Double bidPrice;
	//竞拍时间
	@Column(name="bid_time")
	private Long bidTime;
	private String temp;
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Double getBidPrice() {
		return bidPrice;
	}
	public void setBidPrice(Double bidPrice) {
		this.bidPrice = bidPrice;
	}
	public Long getBidTime() {
		return bidTime;
	}
	public void setBidTime(Long bidTime) {
		this.bidTime = bidTime;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public History() {
		super();
	}
	@Override
	public String toString() {
		return "History [tradeNo=" + tradeNo + ", goodsId=" + goodsId + ", userId=" + userId + ", bidPrice=" + bidPrice
				+ ", bidTime=" + bidTime + ", temp=" + temp + "]";
	}
}
