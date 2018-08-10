package com.batman.bysj.solr;

import com.batman.bysj.solr.dao.ItemRepository;
import com.batman.bysj.solr.domain.Item;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BatmanBysjCommonSlorCloudApplicationTests {
    @Autowired
    private CloudSolrClient cloudSolrClient;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ItemRepository itemRepository;
    @Test
    public void testQuery() throws IOException, SolrServerException {
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");

//        String ymqTitle = "penglei";
//
//        query.setQuery(" ymqTitle:*" + ymqTitle + "* ");
        cloudSolrClient.setDefaultCollection("collection1");
        cloudSolrClient.connect();
        QueryResponse response = cloudSolrClient.query(query);
        SolrDocumentList results = response.getResults();
        results.forEach(entries -> System.out.println(entries.get("item_title")));
    }

    @Test
    public void testDelete() throws IOException, SolrServerException {
        cloudSolrClient.setDefaultCollection("collection1");
        cloudSolrClient.deleteByQuery("*:*");
//        UpdateResponse test001 = cloudSolrClient.deleteById("test001");
        cloudSolrClient.commit();
//        System.out.println(test001);
    }

    @Test
    public void testAdd() throws IOException, SolrServerException {
        cloudSolrClient.setDefaultCollection("collection1");
        cloudSolrClient.connect();
        SolrInputDocument solrInputFields = new SolrInputDocument();
        solrInputFields.addField("item_title", "测试商品");
        solrInputFields.addField("item_price", "100");
        solrInputFields.addField("id", "test001");
        cloudSolrClient.add(solrInputFields);
        cloudSolrClient.commit();
    }

    @Test
    public void test() throws IOException, SolrServerException {
        HttpSolrClient httpSolrClient = new HttpSolrClient("http://10.0.0.44:8983/solr/cms_liking_box");
        SolrQuery query = new SolrQuery();
        // 第三步：向SolrQuery中添加查询条件、过滤条件。。。
        query.setQuery("*:*");
        // 第四步：执行查询。得到一个Response对象。
        QueryResponse response = httpSolrClient.query(query);
        // 第五步：取查询结果。
        SolrDocumentList solrDocumentList = response.getResults();
        System.out.println();
    }

    @Test
    public void testImport() throws IOException, SolrServerException {
        String sql = "SELECT\n" +
                "\tsupplier_id,\n" +
                "\tproduct_id,\n" +
                "\tstore_code,\n" +
                "\tcategory_code_4,\n" +
                "\tbrand_id\n" +
                "FROM\n" +
                "\t`bt_sale_product_day_xxx_2018`\n" +
                "LIMIT 0,\n" +
                " 1000000";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        AtomicInteger count = new AtomicInteger();
        List<SolrInputDocument> list = new ArrayList<>();
        maps.forEach(i -> {
            count.getAndIncrement();
            cloudSolrClient.setDefaultCollection("collection2");
            cloudSolrClient.connect();
            SolrInputDocument solrInputFields = new SolrInputDocument();
            solrInputFields.addField("item_title", i.get("product_id"));
            solrInputFields.addField("item_price", i.get("store_code"));
            solrInputFields.addField("id", UUID.randomUUID().toString());
            solrInputFields.addField("item_category_name", i.get("brand_id"));
            solrInputFields.addField("item_image", i.get("category_code_4"));
            list.add(solrInputFields);
            try {
                if (count.get() % 1000 == 0) {
                    cloudSolrClient.add(list);
                    cloudSolrClient.commit();
                    list.clear();
                }

            } catch (SolrServerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println();
    }

    @Test
    public void testSolrTime() throws Exception {
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        query.addHighlightField("item_title");
        cloudSolrClient.setDefaultCollection("collection2");
        cloudSolrClient.connect();
        StopWatch stopWatch = new StopWatch("-----");
        stopWatch.start();
        QueryResponse response = cloudSolrClient.query(query);
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        SolrDocumentList results = response.getResults();
        results.forEach(entries -> System.out.println(entries.get("item_title")));
    }

    @Test
    public void testQueryRes(){
        StopWatch stopWatch = new StopWatch("-----");
        stopWatch.start();
        SimpleQuery query = new SimpleQuery("*:*");
        query.setRows(1000000);
//        query.setOffset((page - 1) * pageSize);
        Page<Item> search = itemRepository.search(query);
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        System.out.println();
    }
}
