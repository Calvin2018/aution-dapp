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
	private Double price;
	//商品id
	@Column(name="goods_id")
	private String goodsId;
	//交易时间
	@Column(name="tx_time")
	private Long txTime;
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
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public Long getTxTime() {
		return txTime;
	}
	public void setTxTime(Long txTime) {
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
				+ price + ", goodsId=" + goodsId + ", txTime=" + txTime + ", temp=" + temp + "]";
	}

	
}
