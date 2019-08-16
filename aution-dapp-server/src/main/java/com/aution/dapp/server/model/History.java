package com.aution.dapp.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	//用户判断竞拍是否支付  0：未支付 1：支付成功
	private String temp;

	//用户头像
	private String avatar;
	//用户名称
	@Column(name = "user_name")
	private String userName;
	@Column(name = "user_phone")
	private String userPhone;
	@Column(name="pay_price")
	private Double payPrice;
	
	
	@ManyToOne(targetEntity=Goods.class)
	@JoinColumn(name="goods_id")
	private Goods goods;
	
	
	
	public Double getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(Double payPrice) {
		this.payPrice = payPrice;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

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
				+ ", bidTime=" + bidTime + ", temp=" + temp + ", avatar=" + avatar + ", userName=" + userName
				+ ", userPhone=" + userPhone + ", payPrice=" + payPrice + ", goods=" + goods + "]";
	}


}
