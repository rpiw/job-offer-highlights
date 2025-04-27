package io.rp.job.offer.viewer.crawler;

import com.microsoft.playwright.Page;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class NoFluffJobsService implements CrawlerService {

    private final NoFluffJobsCrawler noFluffJobsCrawler;

    @Setter
    private int resultsNumber = 100;


    public NoFluffJobsService() {
        noFluffJobsCrawler = NoFluffJobsCrawler.initializeCrawler();
    }


    @Override
    public List<NoFluffJobsCrawler.LocatorResult> extractWebsite(String start) {
        String uri = UriComponentsBuilder.fromUriString("http://nofluffjobs.com/")
                .path(start)
                .toUriString();

        try (Page page = noFluffJobsCrawler.open(uri)) {
            return noFluffJobsCrawler.crawlCurrentPage(noFluffJobsCrawler.loadMany(page, resultsNumber));
        }
    }
}
