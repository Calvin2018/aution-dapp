package com.aution.dapp.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aution.dapp.server.model.Goods;
import com.aution.dapp.server.model.History;
import com.aution.dapp.server.model.Transaction;
import com.aution.dapp.server.repository.GoodsRepository;
import com.aution.dapp.server.repository.HistoryRepository;
import com.aution.dapp.server.repository.TransactionRepository;


/**
 * 竞拍服务类
 * @author Administrator
 *
 */
@Service
@Transactional
public class DappService {

	@Autowired
	HistoryRepository hRepository;
	
	@Autowired
	TransactionRepository tRepository;
	
	@Autowired
	GoodsRepository goodsRepository;
	
	/**
	 * 竞拍流程
	 * @param gId 商品id
	 * @param userId 竞拍者用户id
	 * @param price 竞拍价格
	 * @return
	 * @throws Exception 
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean bidProcess(String gId,String userId,Double price) throws Exception {
		
		//检查竞拍价格比当前价格高
		Double maxPrice = hRepository.findMaxPriceByGid(gId);
		if(null != maxPrice) 
			if(price <= maxPrice) throw new Exception("Current price is higher than  bid price");
		
		Pageable pageable = PageRequest.of(0, 1, Sort.by(
			    Order.desc("bid_price")));
		Page<History> temp = hRepository.findHistoryByUserIdAndGoodsId(userId, gId, pageable);
		if(temp.getSize() >0)
			price = price - temp.getContent().get(0).getbPrice();
		
		//中转id  从系统配置文件读取
		String transferId = "";
		//TODO step:1  调用第三方接口 灵光币转账   返回交易id
		String txId = "";
		Long time = System.currentTimeMillis();
		
		Goods goods = new Goods();
		goods.setgId(gId);
		goods.setcPrice(price);
		Integer goodsFlag = goodsRepository.updateByPrimaryKeySelective(goods);
		//用于数据库回滚
		if(0 == goodsFlag) throw new Exception("Goods Update Failed");
		
		History history = new History();
		history.setgId(gId);
		history.setUserId(userId);
		history.setbPrice(price);
		history.setbTime(time);
		Integer hFlag = hRepository.insertHistory(history);
		//用于数据库回滚
		if(0 == hFlag) throw new Exception("History Insert Failed");
		
		Transaction transaction = new Transaction();
		transaction.setgId(gId);
		transaction.setFromUserId(userId);
		transaction.setPrice(price);
		transaction.setToUserId(transferId);
		transaction.setTxId(txId);
		transaction.setTxTime(time);
		Integer tFlag = tRepository.insertTransaction(transaction);
		//用于数据库回滚
		if(0 == tFlag) throw new Exception("Transaction Insert Failed");
		
		return true;
	}
	public void bidCompleted(String gId,String sellerId) {
		//查询所有参与竞拍者 第一个为买家
		String sort = "DESC";
		List<History> historyList = hRepository.findHistoryByGoodsIdAndPriceSort(gId, sort, null).getContent();
		
		for(int i=0;i<historyList.size();i++) {
			if(i == 0) {
				
			}
		}
	}
}
