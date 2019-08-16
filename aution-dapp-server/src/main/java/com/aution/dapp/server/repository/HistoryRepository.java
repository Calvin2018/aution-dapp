package com.aution.dapp.server.repository;

import com.aution.dapp.server.model.History;
import com.cesgroup.platform.mybatis.search.PlatformMybatisRepository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 
 * @author hws
 *
 */
@Mapper
public interface HistoryRepository extends PlatformMybatisRepository<History> {

  @Select("select trade_no,goods_id,h.user_id,bid_price,bid_time,pay_price,temp,avatar,user_name,user_phone from t_history h left join t_user u on h.user_id=u.user_id  where temp = '1'")
  List<History> findAllHistory();
	
  @Select("select trade_no,goods_id,h.user_id,bid_price,bid_time,pay_price,temp,avatar,user_name,user_phone from t_history h LEFT JOIN t_user u on h.user_id=u.user_id where temp = '1' and h.user_id = #{userId} and goods_id = #{gId} order by bid_price DESC")
  List<History> findHistorysByUserIdAndGoodsId(@Param("userId")String userId,@Param("gId")String gId,Pageable pageable);
  
  @Select("select trade_no,g.goods_id,h.user_id,max(bid_price),bid_time,pay_price,h.temp,avatar,user_name,user_phone,start_price,current_price,end_time from t_history h LEFT JOIN t_user u on h.user_id=u.user_id " + 
  		"left join t_goods g on g.goods_id = h.goods_id " + 
  		"where h.temp = '1' and h.user_id = #{userId} " + 
  		"and h.goods_id = #{gId} ")
  History findHistoryByUserIdAndGoodsId(@Param("userId")String userId,@Param("gId")String gId);
  
  
  @Select("select trade_no,goods_id,h.user_id,bid_price,bid_time,pay_price,temp,avatar,user_name,user_phone from t_history h LEFT JOIN t_user u on h.user_id=u.user_id where temp = '1' and h.user_id = #{userId}")
  List<History> findHistoryByUserId(@Param("userId")String userId,Pageable pageable);
  
  @Select("select trade_no,goods_id,h.user_id,bid_price,bid_time,pay_price,temp,avatar,user_name,user_phone from t_history h LEFT JOIN t_user u on h.user_id=u.user_id where temp = '1' and goods_id = #{gId} ")
  List<History> findHistoryByGoodsIdAndTimeSort(@Param("gId")String gId,Pageable pageable);
  
  
  @Select("select trade_no,h.goods_id,user_id,max(bid_price)bid_price,bid_time,pay_price,t.current_price,t.buyer_id from t_history h LEFT JOIN t_goods t on h.goods_id = t.goods_id where h.temp = '1' and  h.goods_id = #{gId} group by h.user_id order by h.bid_price desc")	
  List<History> findHistoryByGoodsIdAndPriceSortAndGroupByUserId(@Param("gId")String gId,Pageable pageable);
  
  @Select("select MAX(bid_price)MaxPrice from t_history where temp = '1' and  goods_id = #{gId}")
  Double findMaxPriceByGid(@Param("gId")String gId);
  
  @Select("select trade_no,h.goods_id,h.user_id,bid_price,pay_price,user_name,user_phone,g.current_price,g.end_time from t_history h " + 
  		"  		 		left join t_user u on h.user_id = u.user_id left join t_goods g on g.goods_id = h.goods_id  where trade_no = #{tradeNo} ")
  List<History> findHistoryByTradeNoAndGidAndPriceSort(@Param("tradeNo")String tradeNo,Pageable pageable);
  
  
  @Insert("insert into t_history (trade_no,goods_id,user_id,bid_price,bid_time,pay_price,temp) values (#{tradeNo},#{goodsId},#{userId},#{bidPrice},#{bidTime},#{payPrice},#{temp})")
  Integer insertHistory(@RequestBody History History);
  
  @Update("update t_history set temp = #{temp}  where trade_no = #{tradeNo}")
  Integer updateHistory(@Param("temp")String temp,@Param("tradeNo")String tradeNo);
  @Select("select trade_no,goods_id,user_id,bid_price,bid_time,pay_price,temp FROM t_history WHERE trade_no = #{tradeNo}")
  History findHistoryByTradeNo(@Param("tradeNo")String tradeNo);
  @Select("select trade_no,goods_id,user_id,bid_price,bid_time,pay_price,temp FROM t_history where temp = #{temp}")
  List<History> findHistoryByTemp(@Param("temp")String temp);

  @Select("select h.goods_id,user_id,MAX(bid_price)bid_price,u.seller_id, " + 
	  		"u.current_price,buyer_id,to_user_id temp from t_history h RIGHT JOIN " + 
	  		"(select goods_id,seller_id,current_price,buyer_id from t_goods  " + 
	  		"where status = 1 and end_time < #{endTime}) u  " + 
	  		"on u.goods_id = h.goods_id  " + 
	  		"left join t_transaction t on h.user_id = t.to_user_id and h.goods_id = t.goods_id " + 
	  		"GROUP BY user_id,goods_id  " + 
	  		"order by goods_id,bid_price desc")
  List<History> findTransactionForNoIssueOrder(@Param("endTime")Long endTime);
}
