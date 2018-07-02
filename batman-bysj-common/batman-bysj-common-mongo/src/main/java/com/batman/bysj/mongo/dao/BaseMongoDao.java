package com.batman.bysj.mongo.dao;

import com.batman.bysj.mongo.domain.BaseMongoModel;
import com.google.common.collect.Iterators;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteResult;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.Repository;
import org.springframework.data.util.CloseableIterator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/** 
* 
* @author victor.qin 
* @date 2018/6/26 10:46
*/ 
public abstract class BaseMongoDao<T extends BaseMongoModel> implements Repository<T, String> {
    /**
     * 默认的stream 分页大小
     */
    public static final int DEFAULT_STREAM_PAGE_SIZE = 100;

    @Autowired(required = false)
    private AuditorAware auditorAware;

    /**
     * 要产生一个新的criteria, 静态的CRITERIA_ACTIVE 变量有状态,不能这样用
     */
    public static Criteria CRITERIA_ACTIVE() {
        return Criteria.where("active").is(1);
    }

    public static String[] BASE_EXCLUDE_FIELDS = new String[]{"creator", "_id", "created", "modified", "modifier", "active"};

    public static Query newActiveQuery(boolean excludeBaseField) {
        Query query = new Query(CRITERIA_ACTIVE());
        if (excludeBaseField) {
            for (final String baseExcludeField : BASE_EXCLUDE_FIELDS) {
                query.fields().exclude(baseExcludeField);
            }
        }
        return query;
    }

    public static Query newQuery(boolean excludeBaseField) {
        Query query = new Query();
        if (excludeBaseField) {
            for (final String baseExcludeField : BASE_EXCLUDE_FIELDS) {
                query.fields().exclude(baseExcludeField);
            }
        }
        return query;
    }


    private static final Logger logger = LoggerFactory.getLogger(BaseMongoDao.class);
    protected Class<T> entityClass;
    protected String collectionName;

    @Autowired
    protected MongoTemplate mongoTemplate;

    public BaseMongoDao() {
        try {
            this.entityClass = resolveReturnedClassFromGernericType();
            if (this.entityClass != null) {
                this.collectionName = resolveCollectionName(this.entityClass);
            }
        } catch (Exception e) {
            logger.error(getClass().getName() + "定义的model不正确,未能获取到entityClass", e);
        }
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public List<T> find(final Query query) {
        return mongoTemplate.find(query, entityClass);
    }

    public List<T> findActive(Criteria criteria) {
        return findActive(criteria, entityClass);
    }

    public List<T> findAllActive() {
        return findActive(null, entityClass);
    }


    public <R> List<R> findActive(Criteria criteria, Class<R> clz) {
        checkClassAnnotationedByDocument(clz);
        Query q = newActiveQuery(false);
        if (criteria != null) {
            q.addCriteria(criteria);
        }
        return mongoTemplate.find(q, clz);
    }

    public <R> List<R> find(final Query query, Class<R> clz) {
        checkClassAnnotationedByDocument(clz);
        return mongoTemplate.find(query, clz);
    }

    public <R> List<R> findByNative(final Query query, Class<R> clz) {
        return mongoTemplate.find(query, clz, collectionName);
    }

    private void checkClassAnnotationedByDocument(final Class clz) {
        String collectionName = resolveCollectionName(clz);
        if (collectionName == null) {
            throw new IllegalArgumentException(clz.getName() + " must annotation by org.springframework.data.mongodb.core.mapping.Document");
        }
    }

    public <R> R findOne(final Query query, Class<R> clz) {
        checkClassAnnotationedByDocument(clz);
        return mongoTemplate.findOne(query, clz);
    }

    public <R> R findActiveOne(Criteria criteria, Class<R> clz) {
        checkClassAnnotationedByDocument(clz);
        Query q = newActiveQuery(false);
        if (criteria != null) {
            q.addCriteria(criteria);
        }
        return mongoTemplate.findOne(q, clz);
    }

    public T findActiveOne(Criteria criteria) {
        Query q = newActiveQuery(false);
        if (criteria != null) {
            q.addCriteria(criteria);
        }
        return mongoTemplate.findOne(q, entityClass);
    }

    public T findOne(final Query query) {
        return mongoTemplate.findOne(query, entityClass);
    }

    /**
     * 单关键字相等的方法
     *
     * @param key
     * @param val
     * @return
     */
    public T findActiveOneBy(@NonNull final String key, @NonNull Object val) {
        Query query = new Query(CRITERIA_ACTIVE().and(key).is(val));
        return findOne(query);
    }


    public T update(final Query query, final Update update) {
        return mongoTemplate.findAndModify(query, update, entityClass);
    }

    public T save(final T entity) {
        mongoTemplate.save(entity);
        return entity;
    }

    public T findById(final String id) {
        return mongoTemplate.findById(id, entityClass);
    }

    public T findById(final String id, final String collectionName) {
        return mongoTemplate.findById(id, entityClass, collectionName);
    }

    public Page<T> findPage(final PageRequest pageable) {
        return findPage(null, pageable);
    }

    public Page<T> findPage(Query query, final PageRequest pageable) {
        if (query == null) {
            query = new BasicQuery("{}");
        }
        long count = mongoTemplate.count(query, entityClass);
        List<T> list = mongoTemplate.find(query.with(pageable), entityClass);
        return new PageImpl<>(list, pageable, count);

    }

    //<editor-fold desc="stream操作">

    public CloseableIterator<T> stream() {
        return stream(new BasicQuery("{}"));
    }

    public CloseableIterator<T> stream(final Query query) {
        Query basicQuery = query == null ? new BasicQuery("{}") : query;
        return mongoTemplate.stream(basicQuery, entityClass);
    }

    public void stream(Consumer<List<T>> consumer) {
        stream(null, null, consumer);
    }

    public void stream(int partitionSize, Consumer<List<T>> consumer) {
        stream(null, partitionSize, consumer);
    }

    public void stream(final Query query, Integer partitionSize, @NonNull Consumer<List<T>> consumer) {
        Query q = query != null ? query : new BasicQuery("{}");
        Integer size = partitionSize != null ? partitionSize : DEFAULT_STREAM_PAGE_SIZE;
        try (CloseableIterator<T> stream = this.stream(q)) {
            Iterator<List<T>> iterator =
                    Iterators.partition(stream, size);
            iterator.forEachRemaining(consumer);
        }
    }
    //</editor-fold>

    public long count(final Query query) {
        return mongoTemplate.count(query, entityClass);
    }

    public WriteResult delete(final Query query) {
        return mongoTemplate.remove(query, entityClass);
    }

    public boolean deleteById(String id) {
        return mongoTemplate.remove(new Query(Criteria.where("_id").is(new ObjectId(id))), entityClass).getN() > 0;
    }

    public WriteResult update(final T model) {

        Query queryById = new Query(Criteria.where("_id").is(model.get_id()));
        DBObject dbDoc = new BasicDBObject();
        mongoTemplate.getConverter().write(model, dbDoc);
        Update update = fromDBObjectExcludeNullFields(dbDoc);
        return mongoTemplate.updateFirst(queryById, update, entityClass);
    }

    public <O> AggregationResults<O> aggregate(Aggregation aggregation, Class<O> outputType) {
        return mongoTemplate.aggregate(aggregation, this.collectionName, outputType);
    }

    public AggregationResults<T> aggregate(Aggregation aggregation) {
        logger.debug("class aggregation {}->{}", entityClass, aggregation);
        return mongoTemplate.aggregate(aggregation, this.collectionName, entityClass);
    }

    /**
     * 根据对象生成一个不包含null值的Update对象
     */
    public static Update fromDBObjectExcludeNullFields(DBObject object) {
        Update update = new Update();
        for (String key : object.keySet()) {
            Object value = object.get(key);
            if (value != null) {
                update.set(key, value);
            }
        }
        return update;
    }

    public BulkWriteResult bulkOperation(Consumer<BulkOperations> consumer) {
        BulkOperations ops = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, entityClass);
        consumer.accept(ops);
        return ops.execute();
    }

    @SuppressWarnings("unchecked")
    private Class<T> resolveReturnedClassFromGernericType() {
        ParameterizedType parameterizedType = resolveReturnedClassFromGernericType(getClass());
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    protected String resolveCollectionName(Class clz) {
        Document annotation = AnnotationUtils.findAnnotation(clz, Document.class);
        if (annotation != null) {
            return annotation.collection();
        }
        return null;
    }

    private ParameterizedType resolveReturnedClassFromGernericType(Class<?> clazz) {
        Object genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type rawtype = parameterizedType.getRawType();
            if (BaseMongoDao.class.equals(rawtype)) {
                return parameterizedType;
            }
        }
        return resolveReturnedClassFromGernericType(clazz.getSuperclass());
    }

    // 代理mongoTemplate的原子方法,这里添加AuditWare方式

    public T findAndModify(Query query, Update update) {
        addAuditUpdateFields(update);
        return this.getMongoTemplate().findAndModify(query, update, entityClass);
    }

    /**
     * 给update 对象添加通用审计字段
     */
    private void addAuditUpdateFields(final Update update) {
        if (auditorAware != null) {
            update.set("modified", new Date());
            update.set("modifier", auditorAware.getCurrentAuditor());
        }
    }

    public T findAndModify(Query query, Update update, FindAndModifyOptions options) {
        addAuditUpdateFields(update);
        return this.getMongoTemplate().findAndModify(query, update, options, entityClass);
    }

    public T findAndRemove(Query query) {
        return this.getMongoTemplate().findAndRemove(query, entityClass);
    }
}

