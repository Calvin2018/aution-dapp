package com.aution.dapp.server.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aution.dapp.server.model.History;
import com.aution.dapp.server.service.HistoryService;

@RestController
@RequestMapping("/api/history")
public class HisttoryController {

	@Autowired
	private HistoryService historyService;
	
	
	@RequestMapping(value="/findHistoryByUserIdAndGoodsId",method=RequestMethod.GET)
	@ResponseBody
	public List<History> findHistoryByUserIdAndGoodsId(String userId,String gId,Integer page,Integer size){
		return historyService.findHistoryByUserIdAndGoodsId(userId, gId, PageRequest.of(page, size));
	}
	@RequestMapping(value="/findHistoryByUserId",method=RequestMethod.GET)
	@ResponseBody
	public List<History> findHistoryByUserId(String userId,Integer page,Integer size){
		return historyService.findHistoryByUserId(userId, PageRequest.of(page, size));
	}
	@RequestMapping(value="/findHistoryByGoodsIdAndTimeSort",method=RequestMethod.GET)
	@ResponseBody
	public List<History> findHistoryByGoodsIdAndTimeSort(String gId,String sort,Integer page,Integer size){
		
		return historyService.findHistoryByGoodsIdAndTimeSort(gId,sort,page,size);
	}
	/*@RequestMapping(name="/findHistoryByGoodsIdAndPriceSort",method=RequestMethod.POST)
	@ResponseBody
	public List<History> findHistoryByGoodsIdAndPriceSort(String gId,String sort,Pageable pageable){
		return historyService.findHistoryByGoodsIdAndPriceSort(gId,sort, pageable);
	}*/
	@RequestMapping(value="/findHistoryByGoodsIdAndPriceSortAndGroupByUserId",method=RequestMethod.GET)
	@ResponseBody
	public List<History> findHistoryByGoodsIdAndPriceSortAndGroupByUserId(String gId,String sort,Integer page,Integer size){
		return historyService.findHistoryByGoodsIdAndPriceSortAndGroupByUserId(gId,sort, page, size);

	}
}
