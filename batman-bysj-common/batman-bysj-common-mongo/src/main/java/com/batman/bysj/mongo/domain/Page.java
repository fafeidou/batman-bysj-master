package com.batman.bysj.mongo.domain;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Voyageone Inc.</p>
 *
 * @author holysky.zhao 2018/4/11 15:15
 * @version 1.0
 */
@Data
public class Page {
    private int page;
    private int size=10;
    private Sort sort;

    public PageRequest toPageRequest() {
        return new PageRequest(page, size, sort);
    }

    public PageRequest toPageRequest(Sort defaultSort) {
        if (sort == null) {
            sort = defaultSort;
        }
        return new PageRequest(page, size, sort);
    }
}
