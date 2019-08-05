package com.aution.dapp.server.repository;

import com.aution.dapp.server.model.Goods;
import com.cesgroup.platform.mybatis.search.PlatformMybatisRepository;


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
public interface UserInfoRepository extends PlatformMybatisRepository<Goods> {
	
    @Insert("insert into t_user(user_id,avatar,user_name,user_phone) select #{userId},#{avatar},#{userName},#{userPhone} from DUAL WHERE NOT EXISTS (SELECT user_id,avatar,user_name,user_phone FROM t_user WHERE user_id = #{userId})")
	Integer insertUser(@Param("userId")String userId,@Param("avatar")String avatar,@Param("userName")String userName,@Param("userPhone")String userPhone);
    @Update("update t_user set avatar = #{avatar} ,user_name = #{userName},user_phone=#{userPhone} where user_id = #{userId}")
    Integer updateUser(@Param("userId")String userId,@Param("avatar")String avatar,@Param("userName")String userName,@Param("userPhone")String userPhone);
    @Select("select user_id,avatar,user_name,user_phone from t_user where user_id = #{userId}")
    Goods findUserByUserId(@Param("userId")String userId);
}
