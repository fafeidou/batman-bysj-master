package com.batman.bysj.common.dao.mapper;


import com.batman.bysj.common.dao.mybatis.MyMapper;
import com.batman.bysj.common.model.UserInfoX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends MyMapper<UserInfoX> {

    @Select("select user_name as userName,password from user where user_name = #{userName} limit 1")
    UserInfoX selectUserByName(@Param("userName") String userName);
}
