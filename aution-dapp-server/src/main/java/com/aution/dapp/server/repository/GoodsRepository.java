package com.aution.dapp.server.repository;

import com.aution.dapp.server.model.Goods;
import com.cesgroup.platform.mybatis.search.PlatformMybatisRepository;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 
 * @author hws
 *
 */
@Mapper
public interface GoodsRepository extends PlatformMybatisRepository<Goods> {

  @Select("select goods_id,title,imgs,current_price," + 
	  		"end_time,temp from t_goods")
  Page<Goods> findAllGoods(Pageable pageable);
	
  @Select("select goods_id,title,imgs,current_price," + 
  		"end_time,temp from t_goods where seller_id = #{sellerId}")	
  Page<Goods> findGoodsBySellerId(@Param("sellerId")String sellerId,Pageable pageable);
  
  @Select("select goods_id,title,start_price,imgs,current_price," + 
	  		"end_time,temp from t_goods where seller_id = #{sellerId} and status = #{status}")	
  Page<Goods> findGoodsBySellerIdAndStatus(@Param("sellerId")String sellerId,@Param("status")Integer status,Pageable pageable);
  
  @SelectProvider(type=DappProvider.class,method="findGoodsByBuyerIdAndStatus")
  Page<Goods> findGoodsByBuyerIdAndStatus(@Param("buyerId")String buyerId,@Param("status")Integer status,Pageable pageable);
  
  @SelectProvider(type=DappProvider.class,method="findGoodsByEtimeAndSort")	
  Page<Goods> findGoodsByEtimeAndSort(@Param("eTime")Long eTime,@Param("sort")String sort,Pageable pageable);
  
  @Select("select goods_id,title,imgs,start_price,end_time,temp " + 
  		"from t_goods where type = #{type}")
  Page<Goods> findGoodsByType(@Param("type")Integer type,Pageable pageable);
  
  @SelectProvider(type=DappProvider.class,method="findGoodsBySpriceAndSort")
  Page<Goods> findGoodsBySpriceAndSort(@Param("sPrice")Double sPrice,@Param("sort")String sort,@Param("relation")Integer relation,Pageable pageable);
  
  @Select("select goods_id,title,imgs,start_price,end_time,temp " + 
  		"from t_goods where  LOCATE( #{title},title)>0 ")
  Page<Goods> findGoodsByTitle(@Param("title")String title,Pageable pageable);
  
  @Select("select goods_id,seller_id,buyer_id,title,type,start_price,start_time,details,imgs, " + 
  		"current_price,content,status,end_time,temp  " + 
  		"from t_goods where goods_id = #{gId}")
  Goods findGoodsByGid(@Param("gId")String gId);
  
  @InsertProvider(type=DappProvider.class,method="insertGoods")
  Integer insertGoods(@RequestBody Goods goods);
  
  @UpdateProvider(type=DappProvider.class,method="updateGoods")
  Integer updateGoods(@RequestBody Goods goods);
  
  @Delete("delete from t_goods where goods_id = #{gId}")
  Integer deleteGoodsByGid(@Param("gId")String gId);  
  
}
