package com.batman.bysj.common.dao.mapper;


import com.batman.bysj.common.dao.mybatis.MyMapper;
import com.batman.bysj.common.model.Test;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface TestMapper extends MyMapper<Test> {

}
