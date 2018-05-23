package com.batman.bysj.common.redis.cache;

/**
 * 下标转换帮助类
 * Created by jonas on 2017/1/13.
 */
final class IndexConverter {
    /**
     * 将索引下标从倒序（负数）转换为正序（正数）
     */
    static long[] convertRange(int size, long start, long end) {
        if (size == 0)
            return null;
        // 将负数下标转换成正数下标
        if (start < 0) start = size + start;
        if (end < 0) end = size + end;
        // 检查起始范围，如果超出了索引范围，则直接返回空
        if (start < 0 || start >= size || start > end)
            return null;
        // 如果结束索引超出了最大位置，那么回归到最后一位
        if (size <= end) {
            end = size - 1L;
        }
        end += 1;
        return new long[]{start, end};
    }
}
