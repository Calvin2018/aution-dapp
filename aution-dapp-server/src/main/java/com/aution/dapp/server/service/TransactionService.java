package com.aution.dapp.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aution.dapp.server.model.Transaction;
import com.aution.dapp.server.repository.TransactionRepository;
import com.google.common.base.Strings;
/**
 * 交易记录服务类
 * @author hws
 *
 */
@Service
@Transactional
public class TransactionService {

  @Autowired
  TransactionRepository tRepository;

  /**
   * 通过交易id精确查询交易信息
   * @param txId 交易id 调用第三方服务返回
   * @return
   */
  public Transaction findTransactionByTxId(String txId) {
	  if(!Strings.isNullOrEmpty(txId)) {
          return tRepository.findTransactionByTxId(txId);
      }
	  throw new IllegalArgumentException("Arguments txId are required");
  };
	
  /**
   * 通过时间排序方式查询交易信息列表
   * @param sort 时间排序方式 ASC DESC
   * @return
   */
  public List<Transaction> findAllTransactionByTimeSort(String sort,Integer page,Integer size){
      Pageable pageable = PageRequest.of(page, size);
	  if(!Strings.isNullOrEmpty(sort)) {
	      String asc = "ASC";
	      if(asc.equals(sort.toUpperCase())){
              pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("tx_time").ascending());
          }else{
              pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("tx_time").descending());
          }
      }else{
          pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("tx_time").descending());
      }
      return tRepository.findAllTransactionByTimeSort(pageable);
  }
  /**
   * 通过价格排序查询交易信息列表
   * @param sort
   * @return
   */
  public List<Transaction> findAllTransactionByPriceSort(String sort,Pageable pageable){
	  if(!Strings.isNullOrEmpty(sort)) {
          String asc = "ASC";
          if(asc.equals(sort.toUpperCase())){
              pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("price").ascending());
          }else{
              pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("price").descending());
          }
          return tRepository.findAllTransactionByPriceSort(pageable);
      }else{
          pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("price").descending());
      }
      return tRepository.findAllTransactionByPriceSort(pageable);
  }
  /**
   * 通过交易对象查询交易信息列表
   * @param transaction 交易信息实体类
   * @return
   */
  public List<Transaction> findTransactionByParms(Transaction transaction){
	  if(null != transaction) {
          return tRepository.findTransactionByParms(transaction);
      }
	  throw new IllegalArgumentException("Arguments transaction are required");
  }
}
