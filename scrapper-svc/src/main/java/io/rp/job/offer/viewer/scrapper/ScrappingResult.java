package io.rp.job.offer.viewer.scrapper;

import io.rp.job.offer.viewer.scrapper.meta.MetaDataDTO;

import java.util.List;

/**
 * @param results list of any metadata obtained from Page scrapping
 */
public record ScrappingResult(List<MetaDataDTO> results) {

}

