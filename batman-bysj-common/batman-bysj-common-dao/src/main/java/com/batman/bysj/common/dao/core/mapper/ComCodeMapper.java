package com.batman.bysj.common.dao.core.mapper;

import com.batman.bysj.common.dao.mybatis.MyMapper;
import com.batman.bysj.common.model.ComCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author victor.qin
 * @date 2018/5/23 17:54
 */
@Mapper
public interface ComCodeMapper extends MyMapper<ComCode> {
    @Select("select count(1) from com_code")
    int getCount();
}
