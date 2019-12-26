package com.aution.dapp.server.model;

import javax.persistence.*;

/**
 * 商品实体类
 * @author hws
 * 注：前期设计不完善，缺少user model  因此直接把字段加入到goods当中
 * 
 */
@Entity
@Table(name = "t_goods")
public class Goods {

	//商品唯一id
	@Id
	@Column(name = "goods_id")
	private String goodsId;
	//卖家id
	@Column(name = "seller_id")
	private String sellerId;
	//买家id
	@Column(name = "buyer_id")
	private String buyerId;
	//商品title
	private String title;
	//商品类型 1:手机数码 2：生活电器 3：护肤/化妆品 4：影音图书 5:零食 6：珠宝首饰7：其他
	private Integer type;
	//商品起拍价
	@Column(name = "start_price")
	private Double startPrice;
	//商品起拍时间
	@Column(name = "start_time")
	private Long startTime;
	//商品竞拍截止时间
	@Column(name = "end_time")
	private Long endTime;
	//商品详情
	private String details;
	//商品图片地址
	private String imgs;
	//当前商品价格
	@Column(name = "current_price")
	private Double currentPrice;
	//商品成交评价
	private String content;
	//商品状态 1:正在运行 2：竞拍成功 3：竞拍失败
	private Integer status;
	//用户头像
	@Transient
	private String avatar;
	//用户名称
	@Transient
	@Column(name = "user_name")
	private String userName;
	@Transient
	@Column(name = "user_phone")
	private String userPhone;

	@Transient
	private String buyName;
	@Transient
	private String buyPhone;
	
	
	public String getBuyName() {
		return buyName;
	}
	public void setBuyName(String buyName) {
		this.buyName = buyName;
	}
	public String getBuyPhone() {
		return buyPhone;
	}
	public void setBuyPhone(String buyPhone) {
		this.buyPhone = buyPhone;
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


	//待定字段
	private String temp;
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Double getStartPrice() {
		return startPrice;
	}
	public void setStartPrice(Double startPrice) {
		this.startPrice = startPrice;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getImgs() {
		return imgs;
	}
	public void setImgs(String imgs) {
		this.imgs = imgs;
	}
	public Double getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(Double currentPrice) {
		this.currentPrice = currentPrice;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public Goods() {
		super();
	}
	@Override
	public String toString() {
		return "Goods [goodsId=" + goodsId + ", sellerId=" + sellerId + ", buyerId=" + buyerId + ", title=" + title
				+ ", type=" + type + ", startPrice=" + startPrice + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", details=" + details + ", imgs=" + imgs + ", currentPrice=" + currentPrice + ", content=" + content
				+ ", status=" + status + ", avatar=" + avatar + ", userName=" + userName + ", userPhone=" + userPhone
				+ ", buyName=" + buyName + ", buyPhone=" + buyPhone + ", temp=" + temp + "]";
	}
}
