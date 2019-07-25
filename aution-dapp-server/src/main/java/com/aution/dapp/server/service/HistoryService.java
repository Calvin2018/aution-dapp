package com.aution.dapp.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aution.dapp.server.core.ApiException;
import com.aution.dapp.server.model.History;
import com.aution.dapp.server.repository.HistoryRepository;
import com.google.common.base.Strings;

/**
 * 竞拍历史记录服务类
 * @author hws
 *
 */
@Service
@Transactional
public class HistoryService {

	@Autowired
	HistoryRepository historyRepository;
	
	/**
	 * 查询历史记录
	 * @param pageable
	 * @return
	 */
	public List<History> findAllHistory(){
		return historyRepository.findAllHistory();
	}
	
	public boolean updateHistory(History record) {
		if(null == record) throw new IllegalArgumentException("Arguments record are required");
		return historyRepository.updateByPrimaryKeySelective(record)==0?false:true;
	}
	
	/**
	 * 通过竞拍用户id和商品id查询竞拍历史记录
	 * @param user_id 竞拍者id
	 * @param gId 商品id
	 * @param pageable
	 * @return
	 */
	public List<History> findHistoryByUserIdAndGoodsId(String userId,String gId,Pageable pageable){
		 if(!Strings.isNullOrEmpty(userId)&&!Strings.isNullOrEmpty(gId))
			 return historyRepository.findHistoryByUserIdAndGoodsId(userId, gId, pageable);
		 throw new IllegalArgumentException("Arguments user_id and gId are required");
	}
	/**
	 * 通过用户id查询历史记录
	 * @param user_id 竞拍者id
	 * @param pageable
	 * @return
	 */
	public List<History> findHistoryByUserId(String userId,Pageable pageable){
		if(!Strings.isNullOrEmpty(userId))
			return historyRepository.findHistoryByUserId(userId, pageable);
		throw new IllegalArgumentException("Arguments user_id are required");
	}
	/**
	 * 通过商品id和时间排序查询历史记录列表
	 * @param gId 商品id
	 * @param sort
	 * @param pageable
	 * @return
	 */
	public List<History> findHistoryByGoodsIdAndTimeSort(String gId,String sort,Integer page,Integer size){
		if(!Strings.isNullOrEmpty(gId)) {
			if(sort.toUpperCase().equals("ASC")) {
				return historyRepository.findHistoryByGoodsIdAndTimeSort(gId, PageRequest.of(page, size,Sort.by("bid_time").ascending()));
			}else if(sort.toUpperCase().equals("DESC")){
				return historyRepository.findHistoryByGoodsIdAndTimeSort(gId, PageRequest.of(page, size,Sort.by("bid_time").descending()));
			}else {
				return historyRepository.findHistoryByGoodsIdAndTimeSort(gId,PageRequest.of(page, size));
			}
		}
			
		throw new IllegalArgumentException("Arguments gId are required");
	}

	/**
	 * 通过商品id 用户id分组及价格排序方式查询历史记录
	 * @param gId
	 * @param sort
	 * @param pageable
	 * @return
	 */
	public List<History> findHistoryByGoodsIdAndPriceSortAndGroupByUserId(String gId,String sort,Integer page,Integer size){
		if(!Strings.isNullOrEmpty(gId)) {
			if(sort.toUpperCase().equals("ASC")) {
				return historyRepository.findHistoryByGoodsIdAndPriceSortAndGroupByUserId(gId, PageRequest.of(page, size,Sort.by("bid_price").ascending()));
			}else if(sort.toUpperCase().equals("DESC")){
				return historyRepository.findHistoryByGoodsIdAndPriceSortAndGroupByUserId(gId, PageRequest.of(page, size,Sort.by("bid_price").descending()));
			}else {
				return historyRepository.findHistoryByGoodsIdAndPriceSortAndGroupByUserId(gId,PageRequest.of(page, size));
			}
				
		}
		throw new IllegalArgumentException("Arguments gId is required");

	}
	
	public List<History> findHistoryByTradeNoAndGidAndPriceSort(String tradeNo) throws ApiException {
		if(!Strings.isNullOrEmpty(tradeNo)) {
			Pageable pageable = PageRequest.of(0, 2);
			List<History> list = historyRepository.findHistoryByTradeNoAndGidAndPriceSort(tradeNo, pageable);
			return list;
		}
			
		throw new IllegalArgumentException("Arguments tradeNo are required");
		
	}
	
	public History findHistoryByPrimaryKey(String tradeNo) {
		if(Strings.isNullOrEmpty(tradeNo))  throw new IllegalArgumentException("Arguments tradeNo are required");
		return historyRepository.selectByPrimaryKey(tradeNo);
	}
}
