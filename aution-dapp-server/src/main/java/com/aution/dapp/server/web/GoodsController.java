package com.aution.dapp.server.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	public List<Goods> findGoodsByParam(String priceSort,String timeSort,Integer type,Integer page,Integer size) {
		 
		return goodsService.findGoodsByTypeAndSpriceSortAndEtimeSort(priceSort, timeSort, type, PageRequest.of(page, size));
	}
	@RequestMapping(name="/findGoodsBySellerId",method=RequestMethod.GET)
	@ResponseBody
	public List<Goods> findGoodsBySellerId(String sellerId,Integer page,Integer size){
		 
		return goodsService.findGoodsBySellerId(sellerId, PageRequest.of(page, size));
	}
	@RequestMapping(value="/findGoodsBySellerIdAndStatus",method=RequestMethod.GET) 
	@ResponseBody
	public List<Goods> findGoodsBySellerIdAndStatus(String sellerId,Integer status,Integer page,Integer size){
		return goodsService.findGoodsBySellerIdAndStatus(sellerId,status, PageRequest.of(page, size));
	}

	
	@RequestMapping(value="/findGoodsByBuyerIdAndStatus",method=RequestMethod.GET)
	@ResponseBody
	public List<Goods> findGoodsByBuyerIdAndStatus(String buyerId,Integer status,Integer page,Integer size){
		return goodsService.findGoodsByBuyerIdAndStatus(buyerId,status, PageRequest.of(page, size));
	}
	@RequestMapping(value="/findGoodsByEtimeAndSort",method=RequestMethod.GET)
	@ResponseBody
	public List<Goods> findGoodsByEtimeAndSort(Long eTime,String sort,Integer page,Integer size){
		return goodsService.findGoodsByEtimeAndSort(eTime,sort, PageRequest.of(page, size));
	}
	@RequestMapping(value="/findGoodsByType",method=RequestMethod.GET)
	@ResponseBody
	public List<Goods> findGoodsByType(Integer type,Integer page,Integer size){
		return goodsService.findGoodsByType(type, PageRequest.of(page, size));
	}
	@RequestMapping(value="/findGoodsBySpriceAndSort",method=RequestMethod.GET)
	@ResponseBody
	public List<Goods> findGoodsBySpriceAndSort(Double sPrice,String sort,Integer relation,Integer page,Integer size){
		return goodsService.findGoodsBySpriceAndSort(sPrice,sort,relation,PageRequest.of(page, size));
	}
	@RequestMapping(value="/findGoodsByTitle",method=RequestMethod.GET) 
	@ResponseBody
	public List<Goods> findGoodsByTitle(String title,Pageable pageable){
		return goodsService.findGoodsByTitle(title,pageable);
	}
	@RequestMapping(value="/findGoodsByGid",method=RequestMethod.GET) 
	@ResponseBody
	public Goods findGoodsByGid(String gId) {
		  
		return goodsService.findGoodsByGid(gId);
	}
	@RequestMapping(value="/create",method=RequestMethod.POST)
	public boolean insertGoods(Goods goods,@RequestParam("files")MultipartFile[] files,HttpServletRequest request) throws IOException {
		
		return goodsService.createGoods(goods,dappService,files);
	}
	
	@RequestMapping(value="/updateGoods",method=RequestMethod.POST)
	public boolean updateGoods(Goods goods) {
		  return goodsService.updateGoods(goods);
	}
	@RequestMapping(value="/getNewMessage",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getNewMessage(String userId) throws IOException{
		
		JSONObject obj = dappService.getBalance(userId, null, null);
		obj.put("msg", goodsService.getNewMessage(userId));
		
		return obj;
	}
}
