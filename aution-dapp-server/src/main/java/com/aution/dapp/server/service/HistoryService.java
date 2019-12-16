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
	 * @return
	 */
	public List<History> findAllHistory(){
		return historyRepository.findAllHistory();
	}
	
	public boolean updateHistory(String temp,String tradeNo) {
		if(Strings.isNullOrEmpty(temp)||Strings.isNullOrEmpty(tradeNo)) {
			throw new IllegalArgumentException("Arguments tradeNo and temp  are required");
		}
		return historyRepository.updateHistory(temp, tradeNo)==0?false:true;
	}
	
	/**
	 * 通过竞拍用户id和商品id查询竞拍历史记录
	 * @param userId 竞拍者id
	 * @param gId 商品id
	 * @param pageable
	 * @return
	 */
	public List<History> findHistorysByUserIdAndGoodsId(String userId,String gId,Pageable pageable){
		 if(!Strings.isNullOrEmpty(userId)&&!Strings.isNullOrEmpty(gId)) {
			 return historyRepository.findHistorysByUserIdAndGoodsId(userId, gId, pageable);
		 }
		 throw new IllegalArgumentException("Arguments user_id and gId are required");
	}
	/**
	 * 通过用户id查询历史记录
	 * @param userId 竞拍者id
	 * @param pageable
	 * @return
	 */
	public List<History> findHistoryByUserId(String userId,Pageable pageable){
		if(!Strings.isNullOrEmpty(userId)) {
			return historyRepository.findHistoryByUserId(userId, pageable);
		}
		throw new IllegalArgumentException("Arguments user_id are required");
	}
	/**
	 * 通过商品id和时间排序查询历史记录列表
	 * @param gId 商品id
	 * @param sort
	 * @return
	 */
	public List<History> findHistoryByGoodsIdAndTimeSort(String gId,String sort,Integer page,Integer size){
		if(!Strings.isNullOrEmpty(gId)) {
			String asc = "ASC";
			if(!Strings.isNullOrEmpty(sort)) {
				if (sort.toUpperCase().equals(asc)) {
					return historyRepository.findHistoryByGoodsIdAndTimeSort(gId, PageRequest.of(page, size, Sort.by("bid_price").ascending()));
				} else{
					return historyRepository.findHistoryByGoodsIdAndTimeSort(gId, PageRequest.of(page, size, Sort.by("bid_price").descending()));
				}
			}else{
				return historyRepository.findHistoryByGoodsIdAndTimeSort(gId, PageRequest.of(page, size));
			}
		}
			
		throw new IllegalArgumentException("Arguments gId are required");
	}

	/**
	 * 通过商品id 用户id分组及价格排序方式查询历史记录
	 * @param gId
	 * @param sort
	 * @return
	 */
	public List<History> findHistoryByGoodsIdAndPriceSortAndGroupByUserId(String gId,String sort,Integer page,Integer size){
		if(!Strings.isNullOrEmpty(gId)) {
			String asc = "ASC";
			if(!Strings.isNullOrEmpty(sort)) {
				if (sort.toUpperCase().equals(asc)) {
					return historyRepository.findHistoryByGoodsIdAndPriceSortAndGroupByUserId(gId, PageRequest.of(page, size, Sort.by("bid_price").ascending()));
				} else {
					return historyRepository.findHistoryByGoodsIdAndPriceSortAndGroupByUserId(gId, PageRequest.of(page, size, Sort.by("bid_price").descending()));
				}
			}else{
				return historyRepository.findHistoryByGoodsIdAndPriceSortAndGroupByUserId(gId, PageRequest.of(page, size));
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
	
	public History findHistoryByTradeNo(String tradeNo) {
		if(Strings.isNullOrEmpty(tradeNo)) {
			throw new IllegalArgumentException("Arguments tradeNo are required");
		}
		return historyRepository.findHistoryByTradeNo(tradeNo);
	}
	public Double findMaxPriceByGid(String gId) {
		if(Strings.isNullOrEmpty(gId)) {
			throw new IllegalArgumentException("Arguments gId is required");
		}
		return historyRepository.findMaxPriceByGid(gId);
	}
}
