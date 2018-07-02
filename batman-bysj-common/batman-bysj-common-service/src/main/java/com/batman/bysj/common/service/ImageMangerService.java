package com.batman.bysj.common.service;


import com.batman.bysj.mongo.dao.BaseMongoDao;
import com.batman.bysj.mongo.dao.ImageMangerDao;
import com.batman.bysj.mongo.domain.ImageGroup;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

;

/**
 * 用于图片管理的service
 */
@Service
public class ImageMangerService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ImageMangerDao imageMangerDao;

    /**
     * 分页查询图片
     */
    public Page<ImageGroup> findByPage(final String title, PageRequest request) {
        Query query = BaseMongoDao.newActiveQuery(false);
        if (StringUtils.isNotEmpty(title)) {
            query.addCriteria(Criteria.where("title").regex(title.trim(), "ig"));
        }
        return imageMangerDao.findPage(query, request);
    }

    /**
     * 添加图片分组
     *
     * @param imageGroup
     */
    public void add(ImageGroup imageGroup) {
        imageMangerDao.save(imageGroup);
    }

    /**
     * 修改图片分组
     */
    public void update(ImageGroup imageGroup) {
        imageMangerDao.update(imageGroup);
    }

    /**
     * 删除图片分组
     */
    public void delImageGroup(String _id) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(_id)));
        Update update = new Update();
        update.set("active", 0);
        imageMangerDao.update(query, update);
    }

    /**
     * 根据id查询图片分组
     */
    public ImageGroup findById(String _id) {
        return imageMangerDao.findById(_id);
    }

}
