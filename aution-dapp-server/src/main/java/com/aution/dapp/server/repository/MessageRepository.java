package com.aution.dapp.server.repository;

import com.aution.dapp.server.model.Message;
import com.cesgroup.platform.mybatis.search.PlatformMybatisRepository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


/**
 * 
 * @author hws
 *
 */
@Mapper
public interface MessageRepository extends PlatformMybatisRepository<Message> {
	
	@Select("select msg_id,goods_id,user_id,type,flag,temp from t_message where user_id = #{userId} and flag = #{flag}")
	List<Message> findMessageByUidAndFlag(@Param("userId")String userId,@Param("flag")Character flag);

	@Update("update t_message set flag = '1' where user_id = #{userId} and flag = '0' and type = #{status} ")
	Integer updateMessage(@Param("userId")String userId,@Param("status")Character status);
	
	@Insert("insert into t_message(goods_id,user_id,type,flag)values(#{gId},#{userId},#{type},#{flag})")
	Integer insertMessage(@Param("userId")String userId,@Param("gId")String gId,@Param("type")Character type,@Param("flag")Character flag);
}
