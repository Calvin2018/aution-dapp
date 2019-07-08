package com.aution.dapp.server.repository;

import com.aution.dapp.server.model.Goods;
import com.aution.dapp.server.model.Transaction;
import com.google.common.base.Strings;

public class DappProvider {

	public String findGoodsByBuyerIdAndStatus(String buyerId,Integer status) {
		
		StringBuffer sb = new StringBuffer("select g.goods_id,title,imgs,current_price,end_time,temp " + 
				"from t_goods g left join (select goods_id from t_history where user_id = '"+ buyerId +"' group by goods_id,user_id)ids " + 
				" on g.goods_id = ids.goods_id where status = '"+ status +"'");
		
		if(status == 2) sb.append(" and buyer_id = '" + buyerId + "'");
		
		return sb.toString();
	}
	
	public String findGoodsByEtimeAndSort(Long eTime,String sort) {
		StringBuffer sb = new StringBuffer("select goods_id,title,imgs,start_price,end_time,temp " + 
				"from t_goods ");
		if(null != eTime) sb.append(" where end_time = '" + eTime + "'");
		sb.append(" order by end_time "+ sort);
		return sb.toString();
	}
	
	public String findGoodsBySpriceAndSort(Double sPrice,String sort,Integer relation) {
		StringBuffer sb = new StringBuffer("select goods_id,title,imgs,start_price,end_time,temp " + 
				"from t_goods ");
		if(null != sPrice) {
			//0 ：小于等于  1：大于等于
			if(0 == relation) {
				sb.append(" where start_price <= '"+ sPrice +"'");
			}else {
				sb.append(" where start_price >= '"+ sPrice +"'");
			}
		} 
		sb.append(" order by start_price "+ sort);
		return sb.toString();
	}
	
	public String insertGoods(Goods goods) {
		
		StringBuffer sb = new StringBuffer("insert into t_goods(goods_id,seller_id,title,type,start_price,start_time,details,imgs," + 
				"status,end_time");
		if(!Strings.isNullOrEmpty(goods.getTemp()))
			sb.append(" ,temp");
		sb.append(") values("+goods.getgId()+","+goods.getSellerId()+","+goods.getTitle()+","+goods.getType()+","+goods.getsPrice()+","+goods.getsTime()+","+goods.getDetails()+","+goods.getImgs()+","+goods.getStatus()+","+goods.geteTime());
		if(!Strings.isNullOrEmpty(goods.getTemp()))
			sb.append(" ,"+goods.getTemp());
		sb.append(")");
		return sb.toString();
	}
	
	public String updateGoods(Goods goods) {
		
		StringBuffer sb = new StringBuffer("update goods set ");
		if(null != goods.getcPrice())
			sb.append(" current_price='" +goods.getcPrice()+"', ");
		if(null != goods.geteTime())
			sb.append(" end_time = '" + goods.geteTime() +"', ");
		if(null != goods.getStatus())
			sb.append(" status = '"+ goods.getStatus() +"', ");
		if(null != goods.getType())
			sb.append(" type = '" + goods.getType() +"', ");
		if(!Strings.isNullOrEmpty(goods.getBuyerId()))
			sb.append(" buyer_id = '"+ goods.getBuyerId() +"', ");
		if(!Strings.isNullOrEmpty(goods.getContent()))
			sb.append(" content = '"+ goods.getContent() +"', ");
		if(!Strings.isNullOrEmpty(goods.getDetails()))
			sb.append(" details = '" + goods.getDetails() +"', ");
		if(!Strings.isNullOrEmpty(goods.getImgs()))
			sb.append(" imgs = '" + goods.getImgs() +"', ");
		if(!Strings.isNullOrEmpty(goods.getTitle()))
			sb.append(" title = '"+ goods.getTitle() +"', ");
		if(!Strings.isNullOrEmpty(goods.getTemp()))
			sb.append(" temp = '"+ goods.getTemp() +"', ");
		
		String where = " where goods_id = '" + goods.getgId() + "'";
		
		return sb.substring(0, sb.length()-1) + where;
	}
	
	public String findTransactionByParms(Transaction transaction) {
		StringBuffer sb = new StringBuffer("select tx_id,from_user_id,to_user_id,price,goods_id,tx_time,temp " + 
				"from t_transaction where ");
		if(!Strings.isNullOrEmpty(transaction.getgId()))
			sb.append(" goods_id = '" + transaction.getgId() + "' and ");
		if(!Strings.isNullOrEmpty(transaction.getFromUserId()))
			sb.append(" from_user_id = '"+ transaction.getFromUserId() +"'");
		return sb.toString();
	}
}
