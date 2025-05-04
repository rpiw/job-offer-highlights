package io.rp.job.offer.viewer.crawler;

import com.microsoft.playwright.Page;

import java.util.List;

public interface WebCrawler {

    Page loadMore(Page page);

    List<NoFluffJobsCrawler.LocatorResult> crawlCurrentPage(Page page);

    Page open(String url);

    /**
     * Load a page and detect a pagination limit. Then click loadMore until a desired number of elements is present.
     * The method should be called as a first or on a fresh web page as it does not validate if
     * loadMore has already been called. It counts a number of matching elements and then call loadMore until
     * a desired expectedNumber is exceeded.
     *
     * @param page to load
     * @param expectedNumber a number of results to exceed
     * @return page with more than expectedNumber elements
     */
    Page loadMany(Page page, int expectedNumber);
}
