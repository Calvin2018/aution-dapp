package com.aution.dapp.server.repository;

import com.aution.dapp.server.model.History;
import com.cesgroup.platform.mybatis.search.PlatformMybatisRepository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 
 * @author hws
 *
 */
@Mapper
public interface HistoryRepository extends PlatformMybatisRepository<History> {

  @Select("select trade_no,goods_id,user_id,bid_price,bid_time,temp from t_history where temp = '1'")
  List<History> findAllHistory();
	
  @Select("select trade_no,goods_id,user_id,bid_price,bid_time,temp from t_history where temp = '1' and user_id = #{userId} and goods_id = #{gId} order by bid_price DESC")	
  List<History> findHistoryByUserIdAndGoodsId(@Param("userId")String userId,@Param("gId")String gId,Pageable pageable);
  
  @Select("select trade_no,goods_id,user_id,bid_price,bid_time,temp from t_history where temp = '1' and user_id = #{userId}")	
  List<History> findHistoryByUserId(@Param("userId")String userId,Pageable pageable);
  
  @Select("select trade_no,goods_id,user_id,bid_price,bid_time,temp from t_history where temp = '1' and goods_id = #{gId} ")	
  List<History> findHistoryByGoodsIdAndTimeSort(@Param("gId")String gId,Pageable pageable);
  
  
  @Select("select trade_no,goods_id,user_id,max(bid_price)bid_price,bid_time,temp from t_history  where temp = '1' and goods_id = #{gId} group by user_id ")	
  List<History> findHistoryByGoodsIdAndPriceSortAndGroupByUserId(@Param("gId")String gId,Pageable pageable);
  
  @Select("select MAX(bid_price)MaxPrice from t_history where temp = '1' and  goods_id = #{gId}")
  Double findMaxPriceByGid(@Param("gId")String gId);
  
  @Select("select trade_no,t.goods_id,t.user_id,bid_price from t_history h ,(select user_id,goods_id from t_history where temp = '1' and trade_no = #{tradeNo})t where t.user_id = h.user_id and h.goods_id = t.goods_id order by bid_price DESC")
  List<History> findHistoryByTradeNoAndGidAndPriceSort(@Param("tradeNo")String tradeNo,Pageable pageable);
  
  
  @Insert("insert into t_history (trade_no,goods_id,user_id,bid_price,bid_time,temp) values (#{tradeNo},#{goodsId},#{userId},#{bidPrice},#{bidTime},#{temp})")
  Integer insertHistory(@RequestBody History History);
  
}
