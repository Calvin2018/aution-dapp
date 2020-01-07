package org.aution.dapp.server;

import com.aution.dapp.server.model.Goods;
import com.aution.dapp.server.service.DappService;
import com.aution.dapp.server.service.GoodsService;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sf.json.JSONObject;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.aution.dapp.server.task.OrderTask;
import org.springframework.beans.factory.annotation.Autowired;
import sun.net.www.http.HttpClient;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()throws Exception {
//        assertTrue( true );
        findGoodsTest();
    }
           public void findGoodsTest() throws Exception {

        String gid = "g002";
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 6
                , 6, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(10));
        OrderTask orderTask = new OrderTask();
        orderTask.setGid(gid);
        orderTask.setUserid("10012019102102386143256887992328");
        executor.submit(orderTask);
    }
}
