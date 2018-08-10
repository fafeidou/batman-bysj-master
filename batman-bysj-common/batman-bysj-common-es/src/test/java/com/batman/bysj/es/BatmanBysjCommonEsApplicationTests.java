package com.batman.bysj.es;

import com.alibaba.fastjson.JSONObject;
import com.batman.bysj.es.bean.EsPage;
import com.batman.bysj.es.utils.ElasticsearchUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.batman.bysj.es.utils.ElasticsearchUtils.createManyDates;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BatmanBysjCommonEsApplicationTests {
    @Test
    public void createIndexTest() {
        ElasticsearchUtils.createIndex("import_index");
    }

    @Test
    public void deleteIndexTest() {
        ElasticsearchUtils.deleteIndex("import_index");
    }

    @Test
    public void isIndexExistTest() {
        ElasticsearchUtils.isIndexExist("ymq_index");
    }

    @Test
    public void addDataTest() {
        for (int i = 0; i < 100; i++) {
            Map<String, Object> map = new HashMap<>();

            map.put("name", "鹏磊" + i);
            map.put("age", i);
            map.put("interests", new String[]{"阅读", "学习"});
            map.put("about", "世界上没有优秀的理念，只有脚踏实地的结果");
            map.put("processTime", new Date());

            ElasticsearchUtils.addData(JSONObject.parseObject(JSONObject.toJSONString(map)), "ymq_index", "about_test", "id=" + i);
        }
    }

    @Test
    public void deleteDataByIdTest() {

        for (int i = 0; i < 10; i++) {
            ElasticsearchUtils.deleteDataById("ymq_index", "about_test", "id=" + i);
        }
    }

    /**
     * 通过ID 更新数据
     * <p>
     * jsonObject 要增加的数据
     * index      索引，类似数据库
     * type       类型，类似表
     * id         数据ID
     */
    @Test
    public void updateDataByIdTest() {
        Map<String, Object> map = new HashMap<>();

        map.put("name", "鹏磊");
        map.put("age", 11);
        map.put("interests", new String[]{"阅读", "学习"});
        map.put("about", "这条数据被修改");
        map.put("processTime", new Date());

        ElasticsearchUtils.updateDataById(JSONObject.parseObject(JSONObject.toJSONString(map)), "ymq_index", "about_test", "id=11");
    }

    /**
     * 通过ID获取数据
     * <p>
     * index  索引，类似数据库
     * type   类型，类似表
     * id     数据ID
     * fields 需要显示的字段，逗号分隔（缺省为全部字段）
     */
    @Test
    public void searchDataByIdTest() {
        Map<String, Object> map = ElasticsearchUtils.searchDataById("ymq_index", "about_test", "id=11", null);
        System.out.println(JSONObject.toJSONString(map));
    }

    /**
     * 使用分词查询
     * <p>
     * index          索引名称
     * type           类型名称,可传入多个type逗号分隔
     * startTime      开始时间
     * endTime        结束时间
     * size           文档大小限制
     * fields         需要显示的字段，逗号分隔（缺省为全部字段）
     * sortField      排序字段
     * matchPhrase    true 使用，短语精准匹配
     * highlightField 高亮字段
     * matchStr       过滤条件（xxx=111,aaa=222）
     */
    @Test
    public void searchListData() {

        List<Map<String, Object>> list = ElasticsearchUtils.searchListData("ymq_index", "about_test", 1509959382607l, 1509959383865l, 0, "", "", false, "", "name=鹏磊");

        for (Map<String, Object> item : list) {

            System.out.println(JSONObject.toJSONString(item));
        }
    }

    /**
     * 使用分词查询,并分页
     * <p>
     * index          索引名称
     * type           类型名称,可传入多个type逗号分隔
     * currentPage    当前页
     * pageSize       每页显示条数
     * startTime      开始时间
     * endTime        结束时间
     * fields         需要显示的字段，逗号分隔（缺省为全部字段）
     * sortField      排序字段
     * matchPhrase    true 使用，短语精准匹配
     * highlightField 高亮字段
     * matchStr       过滤条件（xxx=111,aaa=222）
     */
    @Test
    public void searchDataPage() {

        EsPage esPage = ElasticsearchUtils.searchDataPage("ymq_index", "about_test", 10, 5, 1509943495299l, 1509943497954l, "", "processTime", false, "about", "about=鹏磊");

        for (Map<String, Object> item : esPage.getRecordList()) {

            System.out.println(JSONObject.toJSONString(item));
        }

    }
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Test
    public void testImport(){
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
        createManyDates(maps);
        System.out.println();
    }


}
