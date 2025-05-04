package io.rp.job.offer.viewer.crawler;

import com.microsoft.playwright.Page;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@Slf4j
public class NoFluffJobsService implements CrawlerService {

    private final NoFluffJobsCrawler noFluffJobsCrawler;

    private final CrawlerRepository repository;

    @Setter
    private int resultsNumber = 100;

    NoFluffJobsService(CrawlerRepository repository) {
        this.repository = repository;
        noFluffJobsCrawler = NoFluffJobsCrawler.initializeCrawler();
    }

    @Override
    public List<NoFluffJobsCrawler.LocatorResult> extractMainPage(String start) {
        String uri = UriComponentsBuilder.fromUriString("http://nofluffjobs.com/")
                .path(start)
                .toUriString();

        try (Page page = noFluffJobsCrawler.open(uri)) {
            List<NoFluffJobsCrawler.LocatorResult> locatorResults = noFluffJobsCrawler.crawlCurrentPage(noFluffJobsCrawler.loadMany(page, resultsNumber));
            log.info("Extracted {} urls.", locatorResults.size());

            List<URLEntity> urlEntities = locatorResults
                    .stream()
                    .map(result -> new URLEntity("https://nofluffjobs.com/" + result.link()))
                    .toList();

            log.debug("Saving results");
            List<URLEntity> saved = repository.saveAll(urlEntities);
            log.info("Saved {} results", saved.size());
            return locatorResults;
        }
    }
}
