package org.aution.dapp.server;

import com.aution.dapp.server.model.Goods;
import com.aution.dapp.server.service.DappService;
import com.aution.dapp.server.service.GoodsService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Autowired
    DappService dappService;
    @Autowired
    GoodsService goodsService;

    @Test
    public void createGoodsTest(){
        Goods goods = new Goods();
        goods.setGoodsId(UUID.randomUUID().toString());
        goods.setEndTime(System.currentTimeMillis() + 50000L);
        goods.setDetails("");
        goods.setSellerId("");
        goods.setStartPrice(1d);
        goods.setTitle("");
        goods.setType(1);
        //goodsService.createGoods(goods,);
    }
    @Test
    public void bidTest(){

    }
    @Test
    public void getUserInfoTest(){

    }


}
