package com.aution.dapp.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 交易实体类
 * @author hws
 *
 */
@Entity
@Table(name="t_transaction")
public class Transaction {

	//交易id
	@Column(name="tx_id")
	private String txId;
	//转出账号id
	@Column(name="from_user_id")
	private String fromUserId;
	//转入账号id
	@Column(name="to_user_id")
	private String toUserId;
	//转入金额
	private double price;
	//商品id
	@Column(name="goods_id")
	private String gId;
	//交易时间
	@Column(name="tx_time")
	private long txTime;
	//待定字段
	private String temp;
	
	public String getTxId() {
		return txId;
	}
	public void setTxId(String txId) {
		this.txId = txId;
	}
	public String getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}
	public String getToUserId() {
		return toUserId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getgId() {
		return gId;
	}
	public void setgId(String gId) {
		this.gId = gId;
	}
	public long getTxTime() {
		return txTime;
	}
	public void setTxTime(long txTime) {
		this.txTime = txTime;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public Transaction() {
		super();
	}
	@Override
	public String toString() {
		return "Transactioin [txId=" + txId + ", fromUserId=" + fromUserId + ", toUserId=" + toUserId + ", price="
				+ price + ", gId=" + gId + ", txTime=" + txTime + ", temp=" + temp + "]";
	}

	
}
