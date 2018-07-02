package com.batman.bysj.common.dao.mapper;


import com.batman.bysj.common.dao.mybatis.MyMapper;
import com.batman.bysj.common.model.User;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface UserMapper extends MyMapper<User> {

}
