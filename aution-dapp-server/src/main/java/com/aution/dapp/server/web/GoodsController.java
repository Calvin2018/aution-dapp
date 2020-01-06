package com.aution.dapp.server.web;

import java.io.IOException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aution.dapp.server.core.ApiConstants;
import com.aution.dapp.server.core.ApiException;
import com.aution.dapp.server.core.ApiResult;
import com.aution.dapp.server.model.Goods;
import com.aution.dapp.server.service.DappService;
import com.aution.dapp.server.service.GoodsService;

import net.sf.json.JSONObject;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {

	@Autowired
	private GoodsService goodsService;
	@Autowired
	private DappService dappService;
	 
	
	@RequestMapping(value="/findGoodsByParam",method=RequestMethod.GET)
	@ResponseBody
	public ApiResult<List<Goods>>  findGoodsByParam(String priceSort,String timeSort,Integer type,Integer page,Integer size) {
		ApiResult<List<Goods>> result = new ApiResult<List<Goods>>();
		try {
			List<Goods> list =  goodsService.findGoodsByTypeAndSpriceSortAndEtimeSort(priceSort, timeSort, type, PageRequest.of(page, size));
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
	@RequestMapping(name="/findGoodsBySellerId",method=RequestMethod.GET)
	@ResponseBody
	public ApiResult<List<Goods>>  findGoodsBySellerId(@RequestParam("sellerId") String sellerId,Integer page,Integer size){
		ApiResult<List<Goods>> result = new ApiResult<List<Goods>>();
		try {
			List<Goods> list =  goodsService.findGoodsBySellerId(sellerId, PageRequest.of(page, size));
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
	@RequestMapping(value="/findGoodsBySellerIdAndStatus",method=RequestMethod.GET) 
	@ResponseBody
	public ApiResult<List<Goods>> findGoodsBySellerIdAndStatus(@RequestParam("sellerId")String sellerId,@RequestParam("status")Integer status,Integer page,Integer size){
		ApiResult<List<Goods>> result = new ApiResult<List<Goods>>();
		try {
			List<Goods> list =  goodsService.findGoodsBySellerIdAndStatus(sellerId,status, PageRequest.of(page, size,Sort.by(Order.desc("end_time"))));
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

	
	@RequestMapping(value="/findGoodsByBuyerIdAndStatus",method=RequestMethod.GET)
	@ResponseBody
	public ApiResult<List<Goods>> findGoodsByBuyerIdAndStatus(@RequestParam("buyerId")String buyerId,@RequestParam("status")Integer status,Integer page,Integer size){
		ApiResult<List<Goods>> result = new ApiResult<List<Goods>>();
		try {
			List<Goods> list =  goodsService.findGoodsByBuyerIdAndStatus(buyerId,status, PageRequest.of(page, size,Sort.by(Order.desc("end_time"))));
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
	@RequestMapping(value="/findGoodsByEtimeAndSort",method=RequestMethod.GET)
	@ResponseBody
	public ApiResult<List<Goods>> findGoodsByEtimeAndSort(Long eTime,String sort,Integer page,Integer size){
		ApiResult<List<Goods>> result = new ApiResult<List<Goods>>();
		try {
			List<Goods> list =  goodsService.findGoodsByEtimeAndSort(eTime,sort, PageRequest.of(page, size));
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
	@RequestMapping(value="/findGoodsByType",method=RequestMethod.GET)
	@ResponseBody
	public ApiResult<List<Goods>> findGoodsByType(@RequestParam("type") Integer type,Integer page,Integer size){
		ApiResult<List<Goods>> result = new ApiResult<List<Goods>>();
		try {
			List<Goods> list =  goodsService.findGoodsByType(type, PageRequest.of(page, size));
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
	@RequestMapping(value="/findGoodsBySpriceAndSort",method=RequestMethod.GET)
	@ResponseBody
	public ApiResult<List<Goods>> findGoodsBySpriceAndSort(@RequestParam("sPrice") Double sPrice,String sort,Integer relation,Integer page,Integer size){
		ApiResult<List<Goods>> result = new ApiResult<List<Goods>>();
		try {
			List<Goods> list =  goodsService.findGoodsBySpriceAndSort(sPrice,sort,relation,PageRequest.of(page, size));
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
	@RequestMapping(value="/findGoodsByTitle",method=RequestMethod.GET) 
	@ResponseBody
	public ApiResult<List<Goods>> findGoodsByTitle(@RequestParam("title") String title,Pageable pageable){
		ApiResult<List<Goods>> result = new ApiResult<List<Goods>>();
		try {
			List<Goods> list =  goodsService.findGoodsByTitle(title,pageable);
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
	@RequestMapping(value="/findGoodsByGid",method=RequestMethod.GET) 
	@ResponseBody
	public ApiResult<Goods> findGoodsByGid(@RequestParam("gId") String gId) {
		ApiResult<Goods> result = new ApiResult<Goods>();
		try {
			Goods goods = goodsService.findGoodsByGid(gId);
			result.setCode(ApiConstants.CODE_SUCCESS);
			result.setMsg("");
			result.setData(goods);
			
		}catch(IllegalArgumentException e) {
			result.setCode(ApiConstants.CODE_ARGS_ERROR);
			result.setMsg(e.getMessage());
			result.setData(null);
		}
		return result;  
	}
	@RequestMapping(value="/create",method=RequestMethod.POST)
	public ApiResult<Boolean> insertGoods(Goods goods,@RequestParam("files")MultipartFile[] files) throws IOException {
		
		ApiResult<Boolean> result = new ApiResult<Boolean>();
		try {
			boolean flag = goodsService.createGoods(goods,files);
			result.setCode(ApiConstants.CODE_SUCCESS);
			result.setMsg("");
			result.setData(flag);
			
		}catch(IllegalArgumentException e) {
			result.setCode(ApiConstants.CODE_ARGS_ERROR);
			result.setMsg(e.getMessage());
			result.setData(null);
		}
		return result;
		
	}
	
	@RequestMapping(value="/updateGoods",method=RequestMethod.POST)
	public ApiResult<Boolean> updateGoods(Goods goods) {
		
		ApiResult<Boolean> result = new ApiResult<Boolean>();
		try {
			boolean flag = goodsService.updateGoods(goods);
			result.setCode(ApiConstants.CODE_SUCCESS);
			result.setMsg("");
			result.setData(flag);
			
		}catch(IllegalArgumentException e) {
			result.setCode(ApiConstants.CODE_ARGS_ERROR);
			result.setMsg(e.getMessage());
			result.setData(null);
		}
		return result;
	}
	@RequestMapping(value="/getNewMessage",method=RequestMethod.GET)
	@ResponseBody
	public ApiResult<JSONObject> getNewMessage(@RequestParam("userId") String userId) throws IOException{
		ApiResult<JSONObject> result = new ApiResult<JSONObject>();
		try {
			JSONObject obj = new JSONObject();
			obj.put("msg", goodsService.getNewMessage(userId));
			result.setCode(ApiConstants.CODE_SUCCESS);
			result.setMsg("");
			result.setData(obj);
			
		}catch(IllegalArgumentException e) {
			result.setCode(ApiConstants.CODE_ARGS_ERROR);
			result.setMsg(e.getMessage());
			result.setData(null);
		}
		return result;
		
	}
	
	
}
