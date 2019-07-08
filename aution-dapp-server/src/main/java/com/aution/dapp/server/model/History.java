package com.aution.dapp.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
@Entity
@Table(name="t_history")
@IdClass(value=HistoryIds.class)
public class History {

	@Column(name="goods_id")
	private String gId;
	@Column(name="user_id")
	private String userId;
	@Column(name="bid_price")
	private double bPrice;
	@Column(name="bid_time")
	private long bTime;
	private String temp;
	public String getgId() {
		return gId;
	}
	public void setgId(String gId) {
		this.gId = gId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public double getbPrice() {
		return bPrice;
	}
	public void setbPrice(double bPrice) {
		this.bPrice = bPrice;
	}
	public long getbTime() {
		return bTime;
	}
	public void setbTime(long bTime) {
		this.bTime = bTime;
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
		return "History [gId=" + gId + ", userId=" + userId + ", bPrice=" + bPrice + ", bTime=" + bTime + ", temp="
				+ temp + "]";
	}
	
}
