package com.batman.bysj.solr.dao;

import com.batman.bysj.solr.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.RequestMethod;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;
import org.springframework.stereotype.Repository;

/**
 * @author victor.qin
 * @date 2018/7/27 18:01
 */
@Repository
public class ItemRepository extends SimpleSolrRepository<Item, String> {
//    @Query("*:*")
//    List<Item> findAll(String str);
    public ItemRepository(SolrOperations solrOperations) {
        super(solrOperations);
    }
    public Page<Item> search(SimpleQuery query) {
        return this.getSolrOperations().queryForPage("collection2", query, Item.class, RequestMethod.POST);
    }


}
