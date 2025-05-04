package io.rp.job.offer.viewer.crawler;

import java.util.List;

interface CrawlerService {

    List<NoFluffJobsCrawler.LocatorResult> extractMainPage(String startingUrl);

}
