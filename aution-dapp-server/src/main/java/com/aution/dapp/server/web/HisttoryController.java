package com.aution.dapp.server.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import com.aution.dapp.server.core.ApiConstants;
import com.aution.dapp.server.core.ApiResult;
import com.aution.dapp.server.model.History;
import com.aution.dapp.server.service.HistoryService;

@RestController
@RequestMapping("/api/history")
public class HisttoryController {

	@Autowired
	private HistoryService historyService;
	
	
	@RequestMapping(value="/findHistoryByUserIdAndGoodsId",method=RequestMethod.GET)
	@ResponseBody
	public ApiResult<List<History>> findHistoryByUserIdAndGoodsId(@RequestParam("userId") String userId, @RequestParam("gId")String gId, Integer page, Integer size){
		
		ApiResult<List<History>> result = new ApiResult<List<History>>();
		try {
			List<History> list = historyService.findHistorysByUserIdAndGoodsId(userId, gId, PageRequest.of(page, size));
			result.setCode(ApiConstants.CODE_SUCCESS);
			result.setMsg("");
			result.setData(list);
			
		}catch(IllegalArgumentException e) {
			result.setCode(ApiConstants.CODE_ARGS_ERROR);
			result.setMsg(e.getMessage());
			result.setData(null);
		}
		return result;  
		
	}
	@RequestMapping(value="/findHistoryByUserId",method=RequestMethod.GET)
	@ResponseBody
	public ApiResult<List<History>> findHistoryByUserId(@RequestParam("userId") String userId,Integer page,Integer size){
		
		ApiResult<List<History>> result = new ApiResult<List<History>>();
		try {
			List<History> list = historyService.findHistoryByUserId(userId, PageRequest.of(page, size));
			result.setCode(ApiConstants.CODE_SUCCESS);
			result.setMsg("");
			result.setData(list);
			
		}catch(IllegalArgumentException e) {
			result.setCode(ApiConstants.CODE_ARGS_ERROR);
			result.setMsg(e.getMessage());
			result.setData(null);
		}
		return result;  
		
	}
	@RequestMapping(value="/findHistoryByGoodsIdAndTimeSort",method=RequestMethod.GET)
	@ResponseBody
	public ApiResult<List<History>> findHistoryByGoodsIdAndTimeSort(@RequestParam("gId") String gId,String sort,Integer page,Integer size){
		
		ApiResult<List<History>> result = new ApiResult<List<History>>();
		try {
			List<History> list = historyService.findHistoryByGoodsIdAndTimeSort(gId,sort,page,size);
			result.setCode(ApiConstants.CODE_SUCCESS);
			result.setMsg("");
			result.setData(list);
			
		}catch(IllegalArgumentException e) {
			result.setCode(ApiConstants.CODE_ARGS_ERROR);
			result.setMsg(e.getMessage());
			result.setData(null);
		}
		return result;  
		
	}
	@RequestMapping(value="/findHistoryByGoodsIdAndPriceSortAndGroupByUserId",method=RequestMethod.GET)
	@ResponseBody
	public ApiResult<List<History>> findHistoryByGoodsIdAndPriceSortAndGroupByUserId(@RequestParam("gId") String gId,String sort,Integer page,Integer size){
		
		ApiResult<List<History>> result = new ApiResult<List<History>>();
		try {
			List<History> list = historyService.findHistoryByGoodsIdAndPriceSortAndGroupByUserId(gId,sort, page, size);
			result.setCode(ApiConstants.CODE_SUCCESS);
			result.setMsg("");
			result.setData(list);
			
		}catch(IllegalArgumentException e) {
			result.setCode(ApiConstants.CODE_ARGS_ERROR);
			result.setMsg(e.getMessage());
			result.setData(null);
		}
		return result;
		
	}
}
