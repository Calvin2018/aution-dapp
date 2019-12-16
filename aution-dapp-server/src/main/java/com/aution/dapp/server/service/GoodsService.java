package com.aution.dapp.server.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.aution.dapp.server.core.ApiConstants;
import com.aution.dapp.server.core.AppClient;
import com.aution.dapp.server.model.Goods;
import com.aution.dapp.server.model.Message;
import com.aution.dapp.server.quartz.BidJob;
import com.aution.dapp.server.repository.GoodsRepository;
import com.aution.dapp.server.repository.MessageRepository;
import com.aution.dapp.server.repository.UserInfoRepository;
import com.aution.dapp.server.utils.GenerateNoUtil;
import com.google.common.base.Strings;


/**
 * 商品服务类
 * @author hws
 *
 */
@Service
@Transactional
public class  GoodsService{
  
  private static final Logger LOGGER = LoggerFactory.getLogger(GoodsService.class);
	
  @Autowired
  private GoodsRepository goodsRepository;
  @Autowired
  private MessageRepository msgRepository;
  @Autowired
  private Scheduler scheduler;
  @Autowired
  private UserInfoRepository userRepository;
	
  /**
   * 查询商品列表通过类型、时间排序、起拍价排序
   * @param pageable 分页作用
   * @return
   */
  public List<Goods> findGoodsByTypeAndSpriceSortAndEtimeSort(String priceSort,String timeSort,Integer type,Pageable pageable){
	  if(Strings.isNullOrEmpty(priceSort)&&Strings.isNullOrEmpty(timeSort)) pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),Sort.by("start_time").descending());
	  return goodsRepository.findGoodsByTypeAndSpriceSortAndEtimeSort(priceSort,timeSort,type,pageable);
  }
	
  /**
   * 根据卖家Id查询商品列表
   * @param sellerId 卖家id
   * @param pageable 分页作用
   * @return
   */
  public List<Goods> findGoodsBySellerId(String sellerId,Pageable pageable){
	 
	  if(!Strings.isNullOrEmpty(sellerId))
		  return goodsRepository.findGoodsBySellerId(sellerId, pageable);
	  throw new IllegalArgumentException("Arguments sellerId are required");
  }
  /**
   * 通过卖家id和商品状态查询商品列表
   * @param sellerId 卖家id
   * @param status 商品状态0:竞拍,1：成功,2：失败
   * @param pageable 分页作用
   * @return
   */
  public List<Goods> findGoodsBySellerIdAndStatus(String sellerId,Integer status,Pageable pageable){
	  if(!Strings.isNullOrEmpty(sellerId)&& null != status) {
		  if(status == 2) {
			  msgRepository.updateMessage(sellerId,'1');
		  } else if(status == 3) {
			  msgRepository.updateMessage(sellerId,'2');
		  } else if(status == 1) {
			  msgRepository.updateMessage(sellerId,'0');
		  }
		  return goodsRepository.findGoodsBySellerIdAndStatus(sellerId,status, pageable);
	  }
	  throw new IllegalArgumentException("Arguments sellerId and status are required");
  }
  /**
   * 通过买家id和商品状态查询商品列表
   * @param buyerId 卖家id
   * @param status 商品状态 0:竞拍,1：成功,2：失败
   * @param pageable 分页作用
   * @return
   */
  public List<Goods> findGoodsByBuyerIdAndStatus(String buyerId,Integer status,Pageable pageable){
	  if(!Strings.isNullOrEmpty(buyerId)&& null != status) {
		  if(status == 2) {
			  msgRepository.updateMessage(buyerId,'3');
		  } else if(status == 3) {
			  msgRepository.updateMessage(buyerId,'4');
		  } else if(status == 1) {
			  msgRepository.updateMessage(buyerId,'0');
		  }
		  List<Goods> list =  goodsRepository.findGoodsByBuyerIdAndStatus(buyerId,status, pageable);
		  if(status == 3) {
			  for(Goods temp:list) {
				  temp.setStatus(6);
			  }
		  }else if(status == 2) {
			  for(Goods temp:list) {
				  temp.setStatus(5);
			  }
		  }else if(status == 1) {
			  for(Goods temp:list) {
				  temp.setStatus(4);
			  }
		  }
		  return list;
	  }
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
	  if(null != eTime) {
		  return goodsRepository.findGoodsByEtimeAndSort(eTime,sort, pageable);
	  }
	  throw new IllegalArgumentException("Arguments eTime is required");
  }
  /**
   * 通过商品类别查询商品列表
   * @param type 商品类别 详情查看{@Goods}
   * @param pageable 分页作用
   * @return
   */
  public List<Goods> findGoodsByType(Integer type,Pageable pageable){
	  if(null != type) {
		  return goodsRepository.findGoodsByType(type, pageable);
	  }
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
	  if(null != sPrice) {
		  return goodsRepository.findGoodsBySpriceAndSort(sPrice,sort,relation,pageable);
	  }
	  throw new IllegalArgumentException("Arguments sPrice is required");
  }
  /**
   * 通过标题模糊查询商品列表
   * @param title 商品标题
   * @param pageable 分页作用
   * @return
   */
  public List<Goods> findGoodsByTitle(String title,Pageable pageable){
	  if(!Strings.isNullOrEmpty(title)) {
		  return goodsRepository.findGoodsByTitle(title,pageable);
	  }
	  throw new IllegalArgumentException("Arguments title are required");
  }

  /**
   * 通过商品id查询详细商品信息(其他查询都是粗略查询)
   * @param gId 商品id
   * @return
   */
  public Goods findGoodsByGid(String gId) {
	  
	  if(!Strings.isNullOrEmpty(gId)) {
		  Goods goods =  goodsRepository.findGoodsByGid(gId);
		  if(!Strings.isNullOrEmpty(goods.getBuyerId())) {
			  Goods temp = userRepository.findUserByUserId(goods.getBuyerId());
			  if(null != temp) {
				  goods.setBuyName(temp.getUserName());
				  goods.setBuyName(temp.getUserPhone());
			  }
		  }
		  return goods;
	  }
	  throw new IllegalArgumentException("Arguments gId are required");
  }

  /**
   * 插入商品信息到数据库中
   * @param goods
   * @return
 * @throws IOException 
   */
  public boolean createGoods(Goods goods,MultipartFile[] files) throws IOException {
	  if(null == goods||null == files) {
		  throw new IllegalArgumentException("Arguments goods and files are required");
	  }
	  goods.setGoodsId(GenerateNoUtil.generateGid(goods.getSellerId()));
	  goods.setStatus(1);
	  String imgUrl = imgStore(files,goods.getGoodsId());
	  goods.setImgs(imgUrl);
	  Integer flag = goodsRepository.insertGoods(goods);
	  if(flag != 0) {
		//任务名称
		Long time = System.currentTimeMillis();
	    String name = time + goods.getGoodsId();
	    //任务所属分组
	    String group = this.getClass().getName();

	    JobDataMap jobDataMap = new JobDataMap();
	    jobDataMap.put("goodsId", goods.getGoodsId());
	    jobDataMap.put("userId", goods.getSellerId());
	    
	    Date endTime = new Date(goods.getEndTime());
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(endTime);

	    String cronExpression = cal.get(Calendar.SECOND) + " " + cal.get(Calendar.MINUTE) + " " + cal.get(Calendar.HOUR_OF_DAY) 
	    + " " + cal.get(Calendar.DAY_OF_MONTH) + " " + (cal.get(Calendar.MONTH) + 1) + " ? " + cal.get(Calendar.YEAR);
	    CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
	    //创建任务
	    JobDetail jobDetail = JobBuilder.newJob(BidJob.class).withIdentity(name,group).usingJobData(jobDataMap).build();
	    //创建任务触发器
	    TriggerKey key = TriggerKey.triggerKey(name,group);
	    Trigger trigger = TriggerBuilder.newTrigger().withIdentity(key).withSchedule(scheduleBuilder).build();
	    //将触发器与任务绑定到调度器内
	    try {
			if (!scheduler.checkExists(key)) {
			  LOGGER.info("Schedule job - with key {} , and expression {}", key, cronExpression);
			  scheduler.scheduleJob(jobDetail, trigger);
			  scheduler.start();
			}
		} catch (SchedulerException e) {
			  LOGGER.info("SchedulerException:{}",e.getMessage());
		}
	  } 
	  return (0 == flag)?false:true;
  }
  /**
   * 更新对象
   * @param goods
   * @return
   */
  public boolean updateGoods(Goods goods) {
	  if(null == goods) {
		  throw new IllegalArgumentException("Arguments goods are required");
	  }
	  Integer flag = goodsRepository.updateGoods(goods);
	  return (0 == flag)?false:true;
  }
  /**
   * 通过用户id查询未读消息
   * @param userId 用户id
   * @return
   */
  public List<Character> getNewMessage(String userId) {
	  
	  if(Strings.isNullOrEmpty(userId)) {
		  throw new IllegalArgumentException("Arguments userId are required");
	  }
	 
	  List<Character> result = new ArrayList<Character>();
	  List<Message> list = msgRepository.findMessageByUidAndFlag(userId, '0');
	  Set<Character> isExist = new HashSet<Character>();
	  for(Message temp : list) {
		  if(!isExist.contains(temp.getType())) {
			  isExist.add(temp.getType());
			  result.add(temp.getType());
		  }
	  }
	  return result;
  }
  public  String  imgStore(MultipartFile[] files,String gId) throws IOException{
	  StringBuffer imgUrl = new StringBuffer();
	  OutputStream out = null;
	  InputStream inputStream = null;
	  BufferedInputStream bufferedInputStream = null;
	  BufferedOutputStream bufferedOutputStream = null;
	  try {
		
		for(int i=0;i<files.length;i++){
			MultipartFile file = files[i];
			//当打成jar包时此路径为jar包的父级文件夹路径
			//File  project= new File(System.getProperty("user.dir"));
			//File project = ResourceUtils.getFile("classpath:static");
			String imgLocation = AppClient.getInstance().getConfiguration().getProperty(ApiConstants.DA_IMG_FILENAME);
			LOGGER.info("图片路径：{}",imgLocation);
			LOGGER.info(imgLocation);
			String path = imgLocation+File.separator;
			if(!new File(imgLocation).exists()){
				new File(imgLocation).mkdirs();
			}
			String fileName = file.getOriginalFilename();
			
			inputStream = file.getInputStream();
			bufferedInputStream = new BufferedInputStream(inputStream);
			File tempFile = new File(path,fileName);
			String[] temp = fileName.split("\\.");
			
			fileName = (gId+i)+"." + temp[temp.length-1];
			
			tempFile = new File(path,fileName);
			out = new FileOutputStream(tempFile); 
			bufferedOutputStream = new BufferedOutputStream(out);
			byte[] buff = new byte[1024];
			int length = 0;
			while( (length = bufferedInputStream.read(buff)) != -1){
				bufferedOutputStream.write(buff,0,length);
			}
			bufferedOutputStream.flush();
			out.flush();
			
			
			imgUrl = imgUrl.append(fileName);
			imgUrl = imgUrl.append(";");
			
			if(bufferedOutputStream != null){
				bufferedOutputStream.close();
			}
			if(bufferedInputStream != null) {
				bufferedInputStream.close();
			}
			if(out != null){
				out.close();
			}
			if(inputStream != null){
				inputStream.close();
			}
			
		}
	  }catch(IOException e) {
		  throw new  IOException();
	  }finally {
		  
		if(bufferedOutputStream != null){
			bufferedOutputStream.close();
		}
		if(bufferedInputStream != null) {
			bufferedInputStream.close();
		}
		if(out != null){
			out.close();
		}
		if(inputStream != null){
			inputStream.close();
		}
		
		
	  }
		
		return imgUrl.toString();
	}
  
  public boolean  insertUser(String userId,String avatar,String userName,String userPhone) {
	  if(Strings.isNullOrEmpty(userId)||Strings.isNullOrEmpty(avatar)||Strings.isNullOrEmpty(userName)||Strings.isNullOrEmpty(userPhone)) {
		  throw new IllegalArgumentException("Arguments userId userPhone and user_name avatar are required");
	  }
	  Integer flag = userRepository.insertUser(userId, avatar, userName,userPhone);
	  return flag ==0?false:true;
  }
  public boolean updateUser(String userId,String avatar,String userName,String userPhone) {
	  if(Strings.isNullOrEmpty(userId)||Strings.isNullOrEmpty(avatar)||Strings.isNullOrEmpty(userName)||Strings.isNullOrEmpty(userPhone))throw new IllegalArgumentException("Arguments userId userPhone and user_name avatar are required");
	  Integer flag = userRepository.updateUser(userId, avatar, userName,userPhone);
	  return flag ==0?false:true;
  }
  
  
  
  public boolean testCreateGoods(Goods goods) throws IOException {
	  if(null == goods) {
		  throw new IllegalArgumentException("Arguments goods and files are required");
	  }
	  goods.setGoodsId(GenerateNoUtil.generateGid(goods.getSellerId()));
	  goods.setStatus(1);
	  goods.setImgs("test.jpg");
	  Integer flag = goodsRepository.insertGoods(goods);
	  if(flag != 0) {
		//任务名称
		Long time = System.currentTimeMillis();
	    String name = time + goods.getGoodsId();
	    //任务所属分组
	    String group = this.getClass().getName();

	    JobDataMap jobDataMap = new JobDataMap();
	    jobDataMap.put("goodsId", goods.getGoodsId());
	    jobDataMap.put("userId", goods.getSellerId());
	    
	    Date endTime = new Date(goods.getEndTime());
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(endTime);

	    String cronExpression = cal.get(Calendar.SECOND) + " " + cal.get(Calendar.MINUTE) + " " + cal.get(Calendar.HOUR_OF_DAY) 
	    + " " + cal.get(Calendar.DAY_OF_MONTH) + " " + (cal.get(Calendar.MONTH) + 1) + " ? " + cal.get(Calendar.YEAR);
	    CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
	    //创建任务
	    JobDetail jobDetail = JobBuilder.newJob(BidJob.class).withIdentity(name,group).usingJobData(jobDataMap).build();
	    //创建任务触发器
	    TriggerKey key = TriggerKey.triggerKey(name,group);
	    Trigger trigger = TriggerBuilder.newTrigger().withIdentity(key).withSchedule(scheduleBuilder).build();
	    //将触发器与任务绑定到调度器内
	    try {
			if (!scheduler.checkExists(key)) {
			  LOGGER.info("Schedule job - with key {} , and expression {}", key, cronExpression);
			  scheduler.scheduleJob(jobDetail, trigger);
			  scheduler.start();
			}
		} catch (SchedulerException e) {
			  LOGGER.info("SchedulerException:{}",e.getMessage());
		}
	  } 
	  return (0 == flag)?false:true;
  }
}
