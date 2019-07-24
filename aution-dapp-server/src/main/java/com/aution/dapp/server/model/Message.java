package com.aution.dapp.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 实体类用于提醒用户
 * @author Administrator
 *
 */
@Entity
@Table(name = "t_message")
public class Message {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "msg_id")
	private Integer msgId;
	@Column(name = "goods_id")
	private String goodsId;
	@Column(name = "user_id")
	private String userId;
	//消息类型 1：拍卖成功 2：拍卖失败 3：竞拍成功 4：竞拍失败
	private Character type;
	//标志位：消息是否已读
	private Character flag;
	private String temp;
	public Integer getMsgId() {
		return msgId;
	}
	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
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
	public Character getType() {
		return type;
	}
	public void setType(Character type) {
		this.type = type;
	}
	public Character getFlag() {
		return flag;
	}
	public void setFlag(Character flag) {
		this.flag = flag;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public Message() {
		super();
	}
	@Override
	public String toString() {
		return "Message [msgId=" + msgId + ", goodsId=" + goodsId + ", userId=" + userId + ", type=" + type + ", flag="
				+ flag + ", temp=" + temp + "]";
	}
}
