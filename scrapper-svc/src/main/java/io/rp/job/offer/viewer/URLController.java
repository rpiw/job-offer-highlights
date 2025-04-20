package io.rp.job.offer.viewer;

import io.rp.job.offer.viewer.scrapper.AccessNoFluffJobs;
import io.rp.job.offer.viewer.scrapper.ScrappingResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("fetch")
@Slf4j
public class URLController {

    @PostMapping(consumes = "application/json", produces = "application/text")
    public ResponseEntity<ScrappingResult> fetchWebsite(@RequestBody UrlDTO urlDTO) {
        log.info("New request with {}", urlDTO);
        ScrappingResult scrap = AccessNoFluffJobs.scrap(urlDTO.url());
        return ResponseEntity.ok(scrap);
    }

}
