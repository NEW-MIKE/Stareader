package com.kaya.stareader.views.epubview;

/**
 * @author yuyh.
 * @date 2016/12/15.
 */
public interface ReaderCallback {

    String getPageHref(int position);

    void toggleToolBarVisible();

    void hideToolBarIfVisible();


}
