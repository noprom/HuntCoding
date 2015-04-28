package com.huntdreams.coding.common.network;

/**
 * 页面信息
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/4/28.
 */
public class PageInfo {
    public int pageAll = 1;
    public int pageIndex = 0;

    public boolean isNewRequest = true;

    public boolean isLoadingLastPage(){return pageIndex >= pageAll;}

    @Override
    public String toString() {
        return "PageInfo{" +
                "pageAll=" + pageAll +
                ", pageIndex=" + pageIndex +
                ", isNewRequest=" + isNewRequest +
                '}';
    }
}
