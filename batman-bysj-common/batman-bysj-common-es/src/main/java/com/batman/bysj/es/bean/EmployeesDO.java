package com.batman.bysj.es.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @author victor.qin
 * @date 2018/7/28 18:02
 */
@Data
@Document(indexName = "company",type = "employe" , shards = 8, replicas = 1)
public class EmployeesDO implements Serializable {
    private static final long serialVersionUID = -5486342676464419079L;
    @Id
    private Integer empNo;

    private Date birthDate;

    private String firstName;

    private String lastName;

    private String gender;

    private Date hireDate;

}
