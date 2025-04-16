package io.rp.job.offer.viewer.scrapper.meta;

import lombok.Getter;

import java.util.function.Function;

public enum JobsOfferMetadata {

    TITLE("common-posting-header", TitleDTO::new),
    JOB_REQUIREMENTS("section[data-cy-section='JobOffer_Requirements']", NotCompleteImplementationDTO::new),
    POSTING_SENIORITY("li[id='posting-seniority']", NotCompleteImplementationDTO::new),
    REQUIREMENTS_SHORT("div[id='posting-requirements']", NotCompleteImplementationDTO::new),
    REQUIREMENTS_DESCRIPTION("section[data-cy-section='JobOffer_Requirements']", NotCompleteImplementationDTO::new),
    JOB_DESCRIPTION("section[id='posting-description']", NotCompleteImplementationDTO::new),
    JOB_TASKS("section[id='posting-tasks'", NotCompleteImplementationDTO::new),
    JOB_SPECS("section[id='posting-specs']", NotCompleteImplementationDTO::new),
    SALARY_LIST("common-posting-salaries-list", NotCompleteImplementationDTO::new),
    COMPANY("common-posting-company-about", NotCompleteImplementationDTO::new);



    @Getter
    private final String selector;

    public final Function<String, MetaDataDTO> extractor;

    JobsOfferMetadata(String selector, Function<String, MetaDataDTO> extractor) {
        this.selector = selector;
        this.extractor = extractor;
    }


}
