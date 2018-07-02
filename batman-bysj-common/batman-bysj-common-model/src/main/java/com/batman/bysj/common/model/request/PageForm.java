package com.batman.bysj.common.model.request;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class PageForm {

    private Integer pageNum = 0;

    private Integer pageSize = 10;

    private List<OrderForm> orders;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<OrderForm> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderForm> orders) {
        this.orders = orders;
    }

    public String getOrderBy() {
        if (CollectionUtils.isEmpty(orders)) {
            return "";
        }
        return orders.stream()
                .map(order -> toUnderscore(order.getColumn()) + " " + order.getDirection())
                .collect(Collectors.joining(","));
    }

    protected static String toUnderscore(String input) {
        if (StringUtils.hasText(input)) {
            StringBuilder sb = new StringBuilder();
            for (char ch : input.toCharArray()) {
                if (ch >= 'A' && ch <= 'Z') {
                    sb.append('_');
                    sb.append((char) (ch + 32));
                } else {
                    sb.append(ch);
                }
            }
            return sb.toString();
        }
        return "";
    }

    public static class OrderForm {

        private String column;
        private String direction;

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }
    }

}
