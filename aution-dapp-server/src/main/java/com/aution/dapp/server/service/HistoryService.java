package com.aution.dapp.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public List<History> findAllHistory(Pageable pageable ){
		return historyRepository.findAllHistory(pageable).getContent();
	}
	/**
	 * 通过竞拍用户id和商品id查询竞拍历史记录
	 * @param user_id 竞拍者id
	 * @param gId 商品id
	 * @param pageable
	 * @return
	 */
	public List<History> findHistoryByUserIdAndGoods(String user_id,String gId,Pageable pageable){
		 if(!Strings.isNullOrEmpty(user_id)&&!Strings.isNullOrEmpty(gId))
			 return historyRepository.findHistoryByUserIdAndGoodsId(user_id, gId, pageable).getContent();
		 throw new IllegalArgumentException("Arguments user_id and gId are required");
	}
	/**
	 * 通过用户id查询历史记录
	 * @param user_id 竞拍者id
	 * @param pageable
	 * @return
	 */
	public List<History> findHistoryByUserId(String user_id,Pageable pageable){
		if(!Strings.isNullOrEmpty(user_id))
			return historyRepository.findHistoryByUserId(user_id, pageable).getContent();
		throw new IllegalArgumentException("Arguments user_id are required");
	}
	/**
	 * 通过商品id和时间排序查询历史记录列表
	 * @param gId 商品id
	 * @param sort
	 * @param pageable
	 * @return
	 */
	public List<History> findHistoryByGoodsIdAndTimeSort(String gId,String sort,Pageable pageable){
		if(!Strings.isNullOrEmpty(gId)&&!Strings.isNullOrEmpty(sort))
			return historyRepository.findHistoryByGoodsIdAndTimeSort(gId,sort, pageable).getContent();
		throw new IllegalArgumentException("Arguments gId and sort are required");
	}
	/**
	 * 通过商品id和价格排序查询历史列表
	 * @param gId 商品id
	 * @param sort 价格排序方式 ASC DESC
	 * @param pageable
	 * @return
	 */
	public List<History> findHistoryByGoodsIdAndPriceSort(String gId,String sort,Pageable pageable){
		if(!Strings.isNullOrEmpty(gId)&&!Strings.isNullOrEmpty(sort))
			return historyRepository.findHistoryByGoodsIdAndPriceSort(gId,sort, pageable).getContent();
		throw new IllegalArgumentException("Arguments gId and sort are required");
	}
}
