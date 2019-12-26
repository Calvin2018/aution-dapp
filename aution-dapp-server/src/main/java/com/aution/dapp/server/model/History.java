package com.aution.dapp.server.model;

import javax.persistence.*;

/**
 * @author hewensheng
 */
@Entity
@Table(name="t_history")
public class History {

	/**
	 * 交易号
	 */
	@Id
	@Column(name="trade_no")
	private String tradeNo;
	/**
	 * 商品id
	 */
	@Column(name="goods_id")
	private String goodsId;
	/**
	 * 用户id
	 */
	@Column(name="user_id")
	private String userId;
	/**
	 * 竞拍价
	 */
	@Column(name="bid_price")
	private Double bidPrice;
	/**
	 * /竞拍时间
	 */
	@Column(name="bid_time")
	private Long bidTime;
	/**
	 * /用户判断竞拍是否支付  0：未支付 1：支付成功
	 */
	private String temp;

	@Column(name="pay_price")
	private Double payPrice;
	@Column(name="is_issue")
	private String isIssue;
	@Column(name="is_valid")
	private String isValid;


	/**
	 * /用户名称
	 */
	@Transient
	@Column(name = "user_name")
	private String userName;
	@Transient
	@Column(name = "user_phone")
	private String userPhone;
	/**
	 * /用户头像
	 */
	@Transient
	private String avatar;
	@Transient
	@Column(name = "start_price")
	private Double startPrice;
	@Transient
	@Column(name = "current_price")
	private Double currentPrice;
	@Transient
	@Column(name = "end_time")
	private Long endTime;
	@Transient
	@Column(name = "buyer_id")
	private String buyerId;
	@Transient
	@Column(name = "seller_id")
	private String sellerId;

	/* 失效-未知
	 * @ManyToOne(targetEntity=Goods.class,optional = true)
	@JoinColumn(name="goods_id",referencedColumnName="goods_id")
	private Goods goods;*/
	
	
	
	public Double getPayPrice() {
		return payPrice;
	}


	public String getSellerId() {
		return sellerId;
	}


	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}


	public String getBuyerId() {
		return buyerId;
	}


	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}


	public Double getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(Double startPrice) {
		this.startPrice = startPrice;
	}

	public Double getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(Double currentPrice) {
		this.currentPrice = currentPrice;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public void setPayPrice(Double payPrice) {
		this.payPrice = payPrice;
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

	public String getIsIssue() {
		return isIssue;
	}

	public void setIsIssue(String isIssue) {
		this.isIssue = isIssue;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
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
		return "History{" +
				"tradeNo='" + tradeNo + '\'' +
				", goodsId='" + goodsId + '\'' +
				", userId='" + userId + '\'' +
				", bidPrice=" + bidPrice +
				", bidTime=" + bidTime +
				", temp='" + temp + '\'' +
				", payPrice=" + payPrice +
				", isIssue='" + isIssue + '\'' +
				", isValid='" + isValid + '\'' +
				", userName='" + userName + '\'' +
				", userPhone='" + userPhone + '\'' +
				", avatar='" + avatar + '\'' +
				", startPrice=" + startPrice +
				", currentPrice=" + currentPrice +
				", endTime=" + endTime +
				", buyerId='" + buyerId + '\'' +
				", sellerId='" + sellerId + '\'' +
				'}';
	}
}
