package com.batman.bysj.solr.domain;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.io.Serializable;

/**
 * @author victor.qin
 * @date 2018/7/27 17:58
 */
@Data
@SolrDocument(solrCoreName = "collection2")
@EntityScan
public class Item implements Serializable {
    @Id
    @Field
    @Indexed
    private String id;

    @Field("item_title")
    @Indexed
    private String itemTitle;

    @Field("item_price")
    @Indexed
    private String itemPrice;

    @Field("item_category_name")
    @Indexed
    private String itemCategoryName;

    @Field("item_image")
    @Indexed
    private String itemImage;
}
