package com.aution.dapp.server.repository;



import com.aution.dapp.server.core.ApiConstants;
import com.aution.dapp.server.model.Goods;
import com.aution.dapp.server.model.Transaction;
import com.google.common.base.Strings;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

public class DappProvider {

	public String findGoodsByTypeAndSpriceSortAndEtimeSort(@Param("type")Integer type) {
		StringBuffer sb = new StringBuffer("select goods_id,seller_id,title,imgs,start_price,end_time,temp,avatar,user_name,user_phone from t_goods g,t_user u where g.seller_id = u.user_id and status = '1' ");
		if(null != type) {
			sb.append(" and type = #{type} ");
		}

		return sb.toString();
			
	}
	
	public String findGoodsByBuyerIdAndStatus(@Param("buyerId")String buyerId,@Param("status")Integer status) {
		

		
		StringBuffer sb = new StringBuffer("select g.goods_id,seller_id,title,imgs,current_price,end_time,temp,avatar,user_name,user_phone " + 
				"from t_goods g left join t_user u on g.seller_id = u.user_id right join (select goods_id from t_history where temp = '1' and user_id = #{buyerId} group by goods_id,user_id)ids " +
				" on g.goods_id = ids.goods_id where status = #{status}");
		
		if(status == 2) {
			sb.append(" and buyer_id = #{buyerId}");
		}
		if(status == 3) {
			sb.append(" and buyer_id != #{buyerId}");
		}
		return sb.toString();
	}
	
	public String findGoodsByEtimeAndSort(@Param("eTime")Long eTime) {
		StringBuffer sb = new StringBuffer("select goods_id,seller_id,title,imgs,start_price,end_time,temp,avatar,user_name,user_phone  " + 
				"from t_goods g,t_user u where g.seller_id = u.user_id ");
		if(null != eTime) {
			sb.append("  and end_time = #{eTime}");
		}
		return sb.toString();
	}
	
	public String findGoodsBySpriceAndSort(@Param("sPrice") Double sPrice,@Param("sort")String sort,@Param("relation")Integer relation) {
		StringBuffer sb = new StringBuffer("select goods_id,seller_id,title,imgs,start_price,end_time,temp,avatar,user_name,user_phone " + 
				"from t_goods g ,t_user u where g.seller_id = u.user_id ");
		if(null != sPrice) {
			//0 ：小于等于  1：大于等于
			if(0 == relation) {
				sb.append(" and start_price <= #{sPrice}");
			}else {
				sb.append(" and start_price >= #{sPrice}");
			}
		}
		return sb.toString();
	}
	
	public String insertGoods(@RequestBody Goods goods) {
		
		StringBuffer sb = new StringBuffer("insert into t_goods(goods_id,seller_id,title,type,start_price,start_time,details,imgs," + 
				"status,end_time");
		if(!Strings.isNullOrEmpty(goods.getTemp())) {
			sb.append(" ,temp");
		}
		sb.append(") values(#{goodsId},#{sellerId},#{title},#{type},#{startPrice},#{startTime},#{details},#{imgs},#{status},#{endTime}");
		if(!Strings.isNullOrEmpty(goods.getTemp())) {
			sb.append(",#{temp}");
		}
		sb.append("')");
		return sb.toString();
	}
	
	public String updateGoods(@RequestBody Goods goods) {
		
		StringBuffer sb = new StringBuffer("update t_goods set ");
		if(null != goods.getCurrentPrice()) {
			sb.append(" current_price=#{currentPrice}, ");
		}
		if(null != goods.getStatus()) {
			sb.append(" status = #{status}, ");
		}
		if(null != goods.getType()) {
			sb.append(" type = #{type}, ");
		}
		if(!Strings.isNullOrEmpty(goods.getBuyerId())) {
			sb.append(" buyer_id = #{buyerId}, ");
		}
		if(!Strings.isNullOrEmpty(goods.getContent())) {
			sb.append(" content = #{content}, ");
		}
		if(!Strings.isNullOrEmpty(goods.getDetails())) {
			sb.append(" details = #{details}, ");
		}
		if(!Strings.isNullOrEmpty(goods.getImgs())) {
			sb.append(" imgs = #{imgs}, ");
		}
		if(!Strings.isNullOrEmpty(goods.getTitle())) {
			sb.append(" title = #{title}, ");
		}
		if(!Strings.isNullOrEmpty(goods.getTemp())) {
			sb.append(" temp = #{temp}, ");
		}
		String result = sb.toString();
		result =  result.substring(0, result.length()-2);
		String where = " where goods_id = #{goodsId}";
		
		return result + where;
	}
	
	public String findTransactionByParms(@RequestBody Transaction transaction) {
		StringBuffer sb = new StringBuffer("select tx_id,from_user_id,to_user_id,price,goods_id,tx_time,temp " + 
				"from t_transaction where ");
		if(!Strings.isNullOrEmpty(transaction.getGoodsId())) {
			sb.append(" goods_id = #{goodsId} and ");
		}
		if(!Strings.isNullOrEmpty(transaction.getFromUserId())) {
			sb.append(" from_user_id = #{fromUserId}");
		}
		return sb.toString();
	}

	public String updateHistory(@Param("temp")String temp, @Param("isIssue")String isIssue, @Param("isValid")String isValid, @Param("issueTradeNo")String issueTradeNo,@Param("tradeNo")String tradeNo){

		StringBuffer sb = new StringBuffer("update t_history set ");
		if(!Strings.isNullOrEmpty(temp)){
			sb.append(" temp = #{temp},");
		}
		if(!Strings.isNullOrEmpty(isIssue)){
			sb.append(" is_issue = #{isIssue},");
		}
		if(!Strings.isNullOrEmpty(isValid)){
			sb.append(" is_valid = #{isValid},");
		}
		if(!Strings.isNullOrEmpty(issueTradeNo)){
			sb.append(" issue_trade_no = #{issueTradeNo},");
		}
		String test = sb.toString();
		test = test.substring(0, test.length()-1);
		return test + "where trade_no = #{tradeNo}";

	}
	
}
