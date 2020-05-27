package com.wym.mybatis.vo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author v_ymmwu
 */
public class Pages<T> {


    private List<T> list;

    private int pageNum;

    private int pageSize;

    private long total;

    public static void startPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
    }

    public static <T> Pages from(List<T> list) {
        Pages<T> pages = new Pages<>();

        PageInfo<T> pageInfo = new PageInfo<>(list);
        pages.list = pageInfo.getList();
        pages.pageNum = pageInfo.getPageNum();
        pages.pageSize = pageInfo.getPageSize();
        pages.total = pageInfo.getTotal();
        return pages;
    }

    public List<T> getList() {
        return list;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotal() {
        return total;
    }
}
