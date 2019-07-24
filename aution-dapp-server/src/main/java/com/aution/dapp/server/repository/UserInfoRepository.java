package com.aution.dapp.server.repository;

import com.aution.dapp.server.model.Goods;
import com.cesgroup.platform.mybatis.search.PlatformMybatisRepository;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 
 * @author hws
 *
 */
@Mapper
public interface UserInfoRepository extends PlatformMybatisRepository<Goods> {
	
    @Insert("insert into t_user(user_id,avatar,user_name,user_phone) select #{userId},#{avatar},#{userName},#{userPhone} from DUAL WHERE NOT EXISTS (SELECT user_id,avatar,user_name,user_phone FROM t_user WHERE user_id = #{userId})")
	Integer insertUser(@Param("userId")String userId,@Param("avatar")String avatar,@Param("userName")String userName,@Param("userPhone")String userPhone);
}
