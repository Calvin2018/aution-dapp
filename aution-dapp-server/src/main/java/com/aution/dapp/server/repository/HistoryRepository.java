package com.aution.dapp.server.repository;

import com.aution.dapp.server.model.History;
import com.cesgroup.platform.mybatis.search.PlatformMybatisRepository;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 
 * @author hws
 *
 */
@Mapper
public interface HistoryRepository extends PlatformMybatisRepository<History> {

  @Select("select trade_no,goods_id,h.user_id,bid_price,bid_time,pay_price,temp,avatar,user_name,user_phone from t_history h left join t_user u on h.user_id=u.user_id  where temp = '1' and is_valid = '1' ")
  List<History> findAllHistory();
	
  @Select("select trade_no,goods_id,h.user_id,bid_price,bid_time,pay_price,temp,avatar,user_name,user_phone from t_history h LEFT JOIN t_user u on h.user_id=u.user_id where temp = '1' and is_valid = '1'  and h.user_id = #{userId} and goods_id = #{gId} order by bid_price DESC,bid_time ASC")
  List<History> findHistorysByUserIdAndGoodsId(@Param("userId")String userId,@Param("gId")String gId,Pageable pageable);
  
      @Select("select trade_no,g.goods_id,h.user_id,bid_time,bid_price,h.temp,avatar,user_name,user_phone,start_price,current_price,end_time\n" +
              "          from  t_goods g  LEFT JOIN (select user_id,goods_id,trade_no,bid_price,temp,bid_time from t_history where temp = '1' and is_valid = '1'  and \n" +
              "          user_id = #{userId}) h on g.goods_id = h.goods_id left join t_user u on h.user_id=u.user_id    where  g.goods_id = #{gId} order by bid_time desc LIMIT 1")
  History findMaxHistoryByUserIdAndGoodsId(@Param("userId")String userId,@Param("gId")String gId);
  
  
  @Select("select trade_no,goods_id,h.user_id,bid_price,bid_time,pay_price,temp,avatar,user_name,user_phone from t_history h LEFT JOIN t_user u on h.user_id=u.user_id where temp = '1' and is_valid = '1'  and h.user_id = #{userId}")
  List<History> findHistoryByUserId(@Param("userId")String userId,Pageable pageable);
  
  @Select("select trade_no,goods_id,h.user_id,bid_price,bid_time,pay_price,temp,avatar,user_name,user_phone from t_history h LEFT JOIN t_user u on h.user_id=u.user_id where temp = '1' and is_valid = '1'  and goods_id = #{gId} ")
  List<History> findHistoryByGoodsIdAndTimeSort(@Param("gId")String gId,Pageable pageable);


   @Select("SELECT\n" +
            "\ttrade_no,issue_trade_no,\n" +
            "\th.goods_id,\n" +
            "\th.user_id,\n" +
            "\th.bid_price,\n" +
            "\tbid_time,\n" +
            "\tpay_price,\n" +
            "\tt.current_price,\n" +
            "\tt.buyer_id,\n" +
            "  is_issue FROM\n" +
            "\t(select user_id,\n" +
            "\tmax( bid_price ) bid_price FROM\n" +
            "\tt_history h WHERE\th.temp = '1' \n" +
            "\tAND is_valid = '1' AND h.goods_id = #{gId} group by h.user_id) his " +
            "left join t_history h on his.user_id = h.user_id and his.bid_price = h.bid_price and h.goods_id =#{gId} " +
            "LEFT JOIN t_goods t ON h.goods_id = t.goods_id \n" +
            " order by h.bid_price desc,bid_time ASC")
  List<History> findBidHistoryByGoodsId(@Param("gId")String gId);

    @Select("SELECT\n" +
            "\ttrade_no,\n" +
            "\th.goods_id,\n" +
            "\th.user_id,\n" +
            "\th.bid_price,\n" +
            "\tbid_time,\n" +
            "\tpay_price,\n" +
            "\tt.current_price,\n" +
            "\tt.buyer_id,\n" +
            "  is_issue FROM\n" +
            "\t(select user_id,\n" +
            "\tmax( bid_price ) bid_price FROM\n" +
            "\tt_history h WHERE\th.temp = '1' \n" +
            "\tAND is_valid = '1' AND h.goods_id = #{gId} group by h.user_id) his " +
            "left join t_history h on his.user_id = h.user_id and his.bid_price = h.bid_price and h.goods_id =#{gId} " +
            "LEFT JOIN t_goods t ON h.goods_id = t.goods_id \n")
  List<History> findHistoryByGoodsIdAndPriceSortAndGroupByUserId(@Param("gId")String gId,Pageable pageable);
  
  @Select("select MAX(bid_price)maxPrice from t_history where temp = '1' and is_valid = '1'  and  goods_id = #{gId}")
  Double findMaxPriceByGid(@Param("gId")String gId);
  
  @Select("select trade_no,h.goods_id,h.user_id,bid_price,pay_price,user_name,user_phone,g.current_price,g.end_time,h.temp from t_history h " +
  		"  		 		left join t_user u on h.user_id = u.user_id left join t_goods g on g.goods_id = h.goods_id  where trade_no = #{tradeNo} ")
  History findHistoryByTradeNoAndGidAndPriceSort(@Param("tradeNo")String tradeNo);

    @Select("select trade_no,h.goods_id,h.user_id,bid_price,pay_price,user_name,user_phone,g.current_price,g.end_time,is_issue from t_history h " +
            "  		 		left join t_user u on h.user_id = u.user_id left join t_goods g on g.goods_id = h.goods_id  where issue_trade_no = #{issueTradeNo} ")
  History findHistoryByIssueTradeNo(@Param("issueTradeNo")String issueTradeNo);
  
  @Insert("insert into t_history (trade_no,goods_id,user_id,bid_price,bid_time,pay_price,temp) values (#{tradeNo},#{goodsId},#{userId},#{bidPrice},#{bidTime},#{payPrice},#{temp})")
  Integer insertHistory(@RequestBody History History);

  @UpdateProvider(type = DappProvider.class,method = "updateHistory")
  Integer updateHistory(@Param("temp")String temp,@Param("isIssue")String isIssue,@Param("isValid")String isValid,@Param("issueTradeNo")String issueTradeNo,@Param("tradeNo")String tradeNo);
  @Select("select trade_no,goods_id,user_id,bid_price,bid_time,pay_price,temp FROM t_history WHERE trade_no = #{tradeNo}")
  History findHistoryByTradeNo(@Param("tradeNo")String tradeNo);
  @Select("SELECT\n" +
          "\ttrade_no,\n" +
          "\tgoods_id,\n" +
          "\tuser_id,\n" +
          "\tbid_price,\n" +
          "\tbid_time,\n" +
          "\tpay_price,\n" +
          "\ttemp,\n" +
          "\tis_issue,\n" +
          "\tis_valid  from \n" +
          "(SELECT\n" +
          "\ttrade_no,\n" +
          "\tgoods_id,\n" +
          "\tuser_id,\n" +
          "\tbid_price,\n" +
          "\tbid_time,\n" +
          "\tpay_price,\n" +
          "\ttemp,\n" +
          "\tis_issue,\n" +
          "\tis_valid \n" +
          "FROM\n" +
          "\tt_history \n" +
          "WHERE\n" +
          "\ttemp = '0' )temp \n" +
          "\twhere \n" +
          "\t is_valid = '0' \n" +
          "\tOR is_valid = '2'")
  List<History> checkNoPayTx();

  @Select("select h.trade_no,h.issue_trade_no,temp1.goods_id,temp1.user_id,temp1.bid_price,u.seller_id,   " +
	  		" u.current_price,u.buyer_id,temp2.is_issue,h.pay_price from  " +
	  		" (select user_id,goods_id,Max(bid_price)bid_price from t_history h " +
	  		" where  h.temp = '1'  and h.is_issue = '0'  " +
	  		" GROUP BY user_id,goods_id)temp1  " +
	  		" LEFT JOIN t_history h on h.user_id = temp1.user_id and h.goods_id = temp1.goods_id and temp1.bid_price = h.bid_price  " +
	  		" LEFT JOIN " +
          " (select  user_id,goods_id,Max(is_issue)is_issue from t_history h  where  h.temp = '1' and  h.goods_id = h.goods_id GROUP BY user_id,goods_id )temp2  " +
          " on  temp1.goods_id = temp2.goods_id and  temp1.user_id = temp2.user_id " +
          " LEFT JOIN " +
          " (select goods_id,seller_id,current_price,buyer_id,end_time from t_goods " +
          "  ) u   " +
          " on u.goods_id = temp1.goods_id  where  u.end_time < #{endTime}" +
          " order by goods_id ,bid_price desc ")
  List<History> findTransactionForNoIssueOrder(@Param("endTime")Long endTime);
}
