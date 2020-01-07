package org.aution.dapp.server.task;

import com.aution.dapp.server.model.Goods;
import com.aution.dapp.server.service.DappService;
import com.aution.dapp.server.service.GoodsService;
import java.util.concurrent.Callable;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderTask implements Callable<Boolean> {

    private String gid;
    private String userid;
    @Autowired
    GoodsService goodsService;
    @Autowired
    DappService dappService;
    @Override
    public Boolean call() throws Exception {
        Goods goods = goodsService.findGoodsByGid(gid);
        Double currentPrice = goods.getCurrentPrice();
        JSONObject order = dappService.createOrder(gid, userid, currentPrice+1.00);
        System.out.println(order);
        return true;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
