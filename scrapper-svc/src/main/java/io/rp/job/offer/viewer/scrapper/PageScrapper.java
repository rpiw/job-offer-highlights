package io.rp.job.offer.viewer.scrapper;


import com.microsoft.playwright.Page;

public interface PageScrapper {

    ScrappingResult scrap(Page page);

}
