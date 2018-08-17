package com.batman.bysj.common.web.service;

import com.batman.bysj.common.dao.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author victor.qin
 * @date 2018/8/15 16:56
 */
@Service
public class TrTest {
    @Autowired
    TestMapper testMapper;

    @Transactional()
    public void a() {
        com.batman.bysj.common.model.Test test = new com.batman.bysj.common.model.Test();
        test.setId(1);
        testMapper.insertSelective(test);
        try {

        } catch (Exception e) {
            b();
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void b() {
        com.batman.bysj.common.model.Test test = new com.batman.bysj.common.model.Test();
        test.setId(2);
        testMapper.insertSelective(test);
        int i = 1 / 0;
    }
}
