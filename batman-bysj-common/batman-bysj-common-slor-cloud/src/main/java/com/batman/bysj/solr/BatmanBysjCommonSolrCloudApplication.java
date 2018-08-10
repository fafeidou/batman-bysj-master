package com.batman.bysj.solr;

import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
*
* @author victor.qin
* @date 2018/7/27 10:07
*/
@SpringBootApplication
public class BatmanBysjCommonSolrCloudApplication implements CommandLineRunner {

    @Autowired
    private CloudSolrClient cloudSolrClient;

    public static void main(String[] args) {
        SpringApplication.run(BatmanBysjCommonSolrCloudApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

    }
}
