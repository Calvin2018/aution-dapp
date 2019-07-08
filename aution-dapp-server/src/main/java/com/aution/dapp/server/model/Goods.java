package com.aution.dapp.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 商品实体类
 * @author hws
 * 
 */
@Entity
@Table(name = "t_goods")
public class Goods {

	//商品唯一id
	@Id
	@Column(name = "goods_id")
	private String gId;
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
	private Double sPrice;
	//商品起拍时间
	@Column(name = "start_time")
	private Long sTime;
	//商品竞拍截止时间
	@Column(name = "end_time")
	private Long eTime;
	//商品详情
	private String details;
	//商品图片地址
	private String imgs;
	//当前商品价格
	@Column(name = "current_price")
	private Double cPrice;
	//商品成交评价
	private String content;
	//商品状态 1:正在运行 2：竞拍成功 3：竞拍失败
	private Integer status;
	//待定字段
	private String temp;
	public String getgId() {
		return gId;
	} 
	public void setgId(String gId) {
		this.gId = gId;
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
	public Double getsPrice() {
		return sPrice;
	}
	public void setsPrice(Double sPrice) {
		this.sPrice = sPrice;
	}
	public Long getsTime() {
		return sTime;
	}
	public void setsTime(Long sTime) {
		this.sTime = sTime;
	}
	
	public Long geteTime() {
		return eTime;
	}
	public void seteTime(Long eTime) {
		this.eTime = eTime;
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
	public Double getcPrice() {
		return cPrice;
	}
	public void setcPrice(Double cPrice) {
		this.cPrice = cPrice;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	@Override
	public String toString() {
		return "Goods [gId=" + gId + ", sellerId=" + sellerId + ", buyerId=" + buyerId + ", title=" + title + ", type="
				+ type + ", sPrice=" + sPrice + ", sTime=" + sTime + ", eTime=" + eTime + ", details=" + details
				+ ", imgs=" + imgs + ", cPrice=" + cPrice + ", content=" + content + ", status=" + status + ", temp="
				+ temp + "]";
	}
	
	
}
