package com.aution.dapp.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aution.dapp.server.model.Goods;
import com.aution.dapp.server.repository.GoodsRepository;
import com.google.common.base.Strings;
/**
 * 商品服务类
 * @author hws
 *
 */
@Service
@Transactional
public class  GoodsService{
  
  @Autowired
  GoodsRepository goodsRepository;
	
  /**
   * 查询商品列表
   * @param pageable 分页作用
   * @return
   */
  public List<Goods> findAllGoods(Pageable pageable){
	  
	  return goodsRepository.findAllGoods(pageable).getContent();
  }
	
  /**
   * 根据卖家Id查询商品列表
   * @param sellerId 卖家id
   * @param pageable 分页作用
   * @return
   */
  public List<Goods> findGoodsBySellerId(String sellerId,Pageable pageable){
	 
	  if(!Strings.isNullOrEmpty(sellerId))
		  return goodsRepository.findGoodsBySellerId(sellerId, pageable).getContent();
	  throw new IllegalArgumentException("Arguments sellerId are required");
  }
  /**
   * 通过卖家id和商品状态查询商品列表
   * @param sellerId 卖家id
   * @param status 商品状态
   * @param pageable 分页作用
   * @return
   */
  public List<Goods> findGoodsBySellerIdAndStatus(String sellerId,Integer status,Pageable pageable){
	  if(!Strings.isNullOrEmpty(sellerId)&& null != status)
		  return goodsRepository.findGoodsBySellerIdAndStatus(sellerId,status, pageable).getContent();
	  throw new IllegalArgumentException("Arguments sellerId and status are required");
  }
  /**
   * 通过买家id和商品状态查询商品列表
   * @param buyerId 卖家id
   * @param status 商品状态
   * @param pageable 分页作用
   * @return
   */
  public List<Goods> findGoodsByBuyerIdAndStatus(String buyerId,Integer status,Pageable pageable){
	  if(!Strings.isNullOrEmpty(buyerId)&& null != status)
		  return goodsRepository.findGoodsByBuyerIdAndStatus(buyerId,status, pageable).getContent();
	  throw new IllegalArgumentException("Arguments buyerId and status are required");
  }
  /**
   * 通过商品截止时间正序或倒叙查询商品列表
   * @param eTime 商品拍卖截止时间
   * @param sort 截止时间排序方式 ASC DESC
   * @param pageable
   * @return
   */
  public List<Goods> findGoodsByEtimeAndSort(Long eTime,String sort,Pageable pageable){
	  if(!Strings.isNullOrEmpty(sort)&& null != eTime)
		  return goodsRepository.findGoodsByEtimeAndSort(eTime,sort, pageable).getContent();
	  throw new IllegalArgumentException("Arguments eTime and sort are required");
  }
  /**
   * 通过商品类别查询商品列表
   * @param type 商品类别 详情查看{@Goods}
   * @param pageable 分页作用
   * @return
   */
  public List<Goods> findGoodsByType(Integer type,Pageable pageable){
	  if(null != type)
		  return goodsRepository.findGoodsByType(type, pageable).getContent();
	  throw new IllegalArgumentException("Arguments type are required");
  }
  
  /**
   * 通过起拍价查询商品列表
   * @param sPrice 起拍价
   * @param sort 起拍价排序方式 ASC DESC
   * @param relation  0 ：小于等于  1：大于等于 起拍价
   * @param pageable
   * @return
   */
  public List<Goods> findGoodsBySpriceAndSort(Double sPrice,String sort,Integer relation,Pageable pageable){
	  if(!Strings.isNullOrEmpty(sort)&&null != sPrice)
		  return goodsRepository.findGoodsBySpriceAndSort(sPrice,sort,relation,pageable).getContent();
	  throw new IllegalArgumentException("Arguments sPrice and sort are required");
  }
  /**
   * 通过标题模糊查询商品列表
   * @param title 商品标题
   * @param pageable 分页作用
   * @return
   */
  public List<Goods> findGoodsByTitle(String title,Pageable pageable){
	  if(!Strings.isNullOrEmpty(title))
		  return goodsRepository.findGoodsByTitle(title,pageable).getContent();
	  throw new IllegalArgumentException("Arguments title are required");
  }

  /**
   * 通过商品id查询详细商品信息(其他查询都是粗略查询)
   * @param gId 商品id
   * @return
   */
  public Goods findGoodsByGid(String gId) {
	  
	  if(!Strings.isNullOrEmpty(gId))
		  return goodsRepository.findGoodsByGid(gId);
	  throw new IllegalArgumentException("Arguments gId are required");
  }

  /**
   * 插入商品信息到数据库中
   * @param goods
   * @return
   */
  public boolean insertGoods(Goods goods) {
	  if(null == goods)  throw new IllegalArgumentException("Arguments goods are required");
	  Integer flag = goodsRepository.insertGoods(goods);
	  return (0 == flag)?false:true;
  }
}
