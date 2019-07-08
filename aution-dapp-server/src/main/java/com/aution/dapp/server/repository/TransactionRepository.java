package com.aution.dapp.server.repository;

import com.aution.dapp.server.model.Transaction;
import com.cesgroup.platform.mybatis.search.PlatformMybatisRepository;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 
 * @author hws
 *
 */
@Mapper
public interface TransactionRepository extends PlatformMybatisRepository<Transaction> {
  
  @Select("select tx_id,from_user_id,to_user_id,price,goods_id,tx_time,temp from t_transaction " + 
  		"where tx_id =  #{txId}")
  Transaction findTransactionByTxId(@Param("txId")String txId);
	
  @Select("select tx_id,from_user_id,to_user_id,price,goods_id,tx_time,temp from t_transaction " + 
	  		"order by tx_time #{sort}")
  Page<Transaction> findAllTransactionByTimeSort(@Param("sort")String sort);
  
  @Select("select tx_id,from_user_id,to_user_id,price,goods_id,tx_time,temp from t_transaction " + 
	  		"order by price #{sort}")
  Page<Transaction> findAllTransactionByPriceSort(@Param("sort")String sort);
  
  @SelectProvider(type=DappProvider.class,method="findTransactionByParms")
  Page<Transaction> findTransactionByParms(@RequestBody Transaction transaction);
  
  @Insert("insert into t_transaction (tx_id,from_user_id,to_user_id,price,goods_id,tx_time,temp) values (#{txId},#{fromUserId},#{toUserId},#{price},#{gId},#{txTime},#{temp})")
  Integer insertTransaction(@RequestBody Transaction transaction);
  
}
