package io.rp.job.offer.viewer;

import io.rp.job.offer.viewer.crawler.NoFluffJobsCrawler;
import io.rp.job.offer.viewer.crawler.NoFluffJobsService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("crawl")
@Slf4j
public class CrawlerController {

    private final NoFluffJobsService noFluffJobsService;

    public CrawlerController(NoFluffJobsService noFluffJobsService) {
        this.noFluffJobsService = noFluffJobsService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<ExtractedLinksDTO> noFluffJobsCrawler(@RequestBody @NotNull SearchTagDTO searchTag) {
        try {
            List<NoFluffJobsCrawler.LocatorResult> start = noFluffJobsService.extractWebsite(searchTag.tag());
            return ResponseEntity.ok(new ExtractedLinksDTO(start));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(429), "Something went wrong, visiting exception not supported yet");
        }
    }

    public record SearchTagDTO(String tag) {

    }

    public record ExtractedLinksDTO(List<NoFluffJobsCrawler.LocatorResult> links) {
    }
}
