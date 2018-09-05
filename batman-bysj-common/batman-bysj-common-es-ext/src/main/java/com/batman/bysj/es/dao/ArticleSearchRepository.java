package com.batman.bysj.es.dao;

import com.batman.bysj.es.model.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author victor.qin
 * @date 2018/8/27 13:07
 */
public interface ArticleSearchRepository extends ElasticsearchRepository<Article, Long> {
}
