package com.aution.dapp.server.repository;



import com.aution.dapp.server.model.Goods;
import com.aution.dapp.server.model.Transaction;
import com.google.common.base.Strings;

public class DappProvider {

	public String findGoodsByTypeAndSpriceSortAndEtimeSort(String priceSort,String timeSort,Integer type) {
		StringBuffer sb = new StringBuffer("select goods_id,seller_id,title,imgs,start_price,end_time,temp,avatar,user_name,user_phone from t_goods g,t_user u where g.seller_id = u.user_id and status = '1' ");
		if(null != type)
			sb.append(" and type = '"+type+"' ");
		
		sb.append(" order by");
		if(!Strings.isNullOrEmpty(priceSort)) 
			sb.append(" start_price "+ priceSort + ",");
		if(!Strings.isNullOrEmpty(timeSort))
			sb.append(" end_time "+timeSort);
		
		String temp = sb.toString();
		if(temp.endsWith(","))
			temp = temp.substring(0, temp.length()-1);
		if(temp.endsWith("order by"))
			temp = temp.replace("order by", "");
		return temp;
			
	}
	
	public String findGoodsByBuyerIdAndStatus(String buyerId,Integer status) {
		
		Integer bidStatus = null ;
		if(status == 1 || status == 2) {
			bidStatus = status;
		}else{
			bidStatus = 2;
		}
		
		StringBuffer sb = new StringBuffer("select g.goods_id,seller_id,title,imgs,current_price,end_time,temp,avatar,user_name,user_phone " + 
				"from t_goods g left join t_user u on g.seller_id = u.user_id left join (select goods_id from t_history where user_id = '"+ buyerId +"' group by goods_id,user_id)ids " + 
				" on g.goods_id = ids.goods_id where status = '"+ bidStatus +"'");
		
		if(status == 2) sb.append(" and buyer_id = '" + buyerId + "'");
		if(status == 3) sb.append(" and buyer_id = '0'");
		return sb.toString();
	}
	
	public String findGoodsByEtimeAndSort(Long eTime,String sort) {
		StringBuffer sb = new StringBuffer("select goods_id,seller_id,title,imgs,start_price,end_time,temp,avatar,user_name,user_phone  " + 
				"from t_goods g,t_user u where g.seller_id = u.user_id ");
		if(null != eTime) sb.append("  and end_time = '" + eTime + "'");
		if(null != sort) sb.append(" order by end_time "+ sort);
		return sb.toString();
	}
	
	public String findGoodsBySpriceAndSort(Double sPrice,String sort,Integer relation) {
		StringBuffer sb = new StringBuffer("select goods_id,seller_id,title,imgs,start_price,end_time,temp,avatar,user_name,user_phone " + 
				"from t_goods g ,t_user u where g.seller_id = u.user_id ");
		if(null != sPrice) {
			//0 ：小于等于  1：大于等于
			if(0 == relation) {
				sb.append(" and start_price <= '"+ sPrice +"'");
			}else {
				sb.append(" and start_price >= '"+ sPrice +"'");
			}
		} 
		if(null != sort) sb.append(" order by start_price "+ sort);
		return sb.toString();
	}
	
	public String insertGoods(Goods goods) {
		
		StringBuffer sb = new StringBuffer("insert into t_goods(goods_id,seller_id,title,type,start_price,start_time,details,imgs," + 
				"status,end_time");
		if(!Strings.isNullOrEmpty(goods.getTemp()))
			sb.append(" ,temp");
		sb.append(") values('"+goods.getGoodsId()+"','"+goods.getSellerId()+"','"+goods.getTitle()+"','"+goods.getType()+"','"+goods.getStartPrice()+"','"+goods.getStartTime()+"','"+goods.getDetails()+"','"+goods.getImgs()+"','"+goods.getStatus()+"','"+goods.getEndTime());
		if(!Strings.isNullOrEmpty(goods.getTemp()))
			sb.append("','"+goods.getTemp());
		sb.append("')");
		return sb.toString();
	}
	
	public String updateGoods(Goods goods) {
		
		StringBuffer sb = new StringBuffer("update t_goods set ");
		if(null != goods.getCurrentPrice())
			sb.append(" current_price='" +goods.getCurrentPrice()+"', ");
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
		String result = sb.toString();
		result =  result.substring(0, result.length()-2);
		String where = " where goods_id = '" + goods.getGoodsId() + "'";
		
		return result + where;
	}
	
	public String findTransactionByParms(Transaction transaction) {
		StringBuffer sb = new StringBuffer("select tx_id,from_user_id,to_user_id,price,goods_id,tx_time,temp " + 
				"from t_transaction where ");
		if(!Strings.isNullOrEmpty(transaction.getGoodsId()))
			sb.append(" goods_id = '" + transaction.getGoodsId() + "' and ");
		if(!Strings.isNullOrEmpty(transaction.getFromUserId()))
			sb.append(" from_user_id = '"+ transaction.getFromUserId() +"'");
		return sb.toString();
	}
	
}
