package com.batman.bysj.es.dao;

import com.batman.bysj.es.bean.EmployeesDO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author victor.qin
 * @date 2018/7/28 18:03
 */
public interface  EmployeesRepository extends ElasticsearchRepository<EmployeesDO,Integer> {
}
