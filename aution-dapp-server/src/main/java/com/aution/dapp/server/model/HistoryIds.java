package com.aution.dapp.server.model;


public class HistoryIds {

	private String gId;
	private String userId;
	private double bPrice;
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
	public HistoryIds() {
		super();
	}
	@Override
	public String toString() {
		return "HistoryIds [gId=" + gId + ", userId=" + userId + ", bPrice=" + bPrice + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(bPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((gId == null) ? 0 : gId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoryIds other = (HistoryIds) obj;
		if (Double.doubleToLongBits(bPrice) != Double.doubleToLongBits(other.bPrice))
			return false;
		if (gId == null) {
			if (other.gId != null)
				return false;
		} else if (!gId.equals(other.gId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
}
