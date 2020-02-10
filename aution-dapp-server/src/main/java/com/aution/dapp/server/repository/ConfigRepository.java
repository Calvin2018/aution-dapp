package com.aution.dapp.server.repository;

import org.apache.ibatis.annotations.*;

import java.util.Map;


/**
 * 
 * @author hws
 *
 */
@Mapper
public interface ConfigRepository {

  @Select("select keywords,result from t_config where keywords = #{key}")
  Map<String,String> getServiceStatus(@Param("key")String key);
  @Update("update t_config set result = #{value} where keywords = #{key}")
  Integer updateResult(@Param("key")String key,@Param("value")String value);
}
