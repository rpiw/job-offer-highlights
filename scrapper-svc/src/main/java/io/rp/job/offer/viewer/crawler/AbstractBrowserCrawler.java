package io.rp.job.offer.viewer.crawler;

import com.microsoft.playwright.Browser;

abstract class AbstractBrowserCrawler {

    protected final Browser browser;

    AbstractBrowserCrawler(Browser browser) {
        this.browser = browser;
    }

    void closeBrowser() {
        this.browser.close();
    }
}
