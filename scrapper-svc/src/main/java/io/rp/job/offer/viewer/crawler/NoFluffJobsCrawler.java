package io.rp.job.offer.viewer.crawler;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Slf4j
public class NoFluffJobsCrawler extends AbstractBrowserCrawler implements WebCrawler {

    private static final String JOB_LISTING_SELECTOR = "a.posting-list-item";
    private static final String LOAD_MORE_BUTTON = "button[nfjloadmore]";

    NoFluffJobsCrawler(Browser browser) {
        super(browser);
    }

    public static NoFluffJobsCrawler initializeCrawler() {
        return new NoFluffJobsCrawler(Playwright.create()
                .firefox().launch(new BrowserType.LaunchOptions().setHeadless(true))
        );
    }

    @Override
    public Page loadMore(Page page) {
        page.locator(LOAD_MORE_BUTTON).click();
        return page;
    }

    @Override
    public List<LocatorResult> crawlCurrentPage(Page page) {
        Locator allLinksLocator = page.locator(JOB_LISTING_SELECTOR);
        return allLinksLocator.all()
                .stream()
                .map(LocatorResult::fromLocator)
                .toList();
    }

    public record LocatorResult(String title, String link) {
        public static LocatorResult fromLocator(Locator locator) {
            return new LocatorResult(locator.innerText(), locator.getAttribute("href"));
        }
    }

    @Override
    public Page open(String url) {  // todo add retry
        Page page = browser.newPage();
        Response response = page.navigate(url);

        if (response.ok()) {
            return page;
        }
        throw new ResourceAccessException("Failed to open %s".formatted(url));
    }

    @Override
    public Page loadMany(Page page, int expectedNumber) {
        log.info("Calling loadMany with expectedNumber {}", expectedNumber);
        List<LocatorResult> locatorResults = crawlCurrentPage(page);

        // it does not work correctly, where are tests, huh
        int additionalCalls = Math.ceilDiv(expectedNumber, locatorResults.size()) - 1;  // one call has already been done!

        Random random = new Random();
        IntStream.range(0, additionalCalls).forEachOrdered(i -> {
            log.info("loadMore calls: {}", i + 1);
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextLong(1000, 3000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            loadMore(page);
        });
        return page;
    }
}
