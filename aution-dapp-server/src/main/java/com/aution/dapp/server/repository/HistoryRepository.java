package com.aution.dapp.server.repository;

import com.aution.dapp.server.model.History;
import com.cesgroup.platform.mybatis.search.PlatformMybatisRepository;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 
 * @author hws
 *
 */
@Mapper
public interface HistoryRepository extends PlatformMybatisRepository<History> {

  @Select("select goods_id,user_id,bid_price,bid_time,temp from t_history")
  Page<History> findAllHistory(Pageable pageable );
	
  @Select("select goods_id,user_id,bid_price,bid_time,temp from t_history where user_id = #{user_id} and goods_id = #{gId} order by bid_price DESC")	
  Page<History> findHistoryByUserIdAndGoodsId(@Param("user_id")String user_id,@Param("gId")String gId,Pageable pageable);
  
  @Select("select goods_id,user_id,bid_price,bid_time,temp from t_history where user_id = #{user_id}")	
  Page<History> findHistoryByUserId(@Param("user_id")String user_id,Pageable pageable);
  
  @Select("select goods_id,user_id,bid_price,bid_time,temp from t_history where goods_id = #{gId} order by bid_time #{sort}")	
  Page<History> findHistoryByGoodsIdAndTimeSort(@Param("gId")String gId,@Param("sort")String sort,Pageable pageable);
  
  @Select("select goods_id,user_id,bid_price,bid_time,temp from t_history where goods_id = #{gId} order by bid_price #{sort}")	
  Page<History> findHistoryByGoodsIdAndPriceSort(@Param("gId")String gId,@Param("sort")String sort,Pageable pageable);
  
  @Select("select MAX(bid_price)MaxPrice from t_history where goods_id = #{gId}")
  Double findMaxPriceByGid(@Param("gId")String gId);
  
  @Insert("insert into t_history (goods_id,user_id,bid_price,bid_time,temp) values (#{gId},#{userId},#{bPrice},#{bTime},#{temp})")
  Integer insertHistory(@RequestBody History History);
  
}
