package io.rp.job.offer.viewer.scrapper;

import io.rp.job.offer.viewer.scrapper.meta.JobsOfferMetadata;

import java.util.Arrays;
import java.util.List;

// todo: move the class to a portal specific package
public interface NoFluffJobsOfferMetadata {

    /**
     * @return an HTML tag from a page containing a list of offers to click on to obtain a job offer specific page
     */
    default String getListingTagToClick() {
        return "h3.posting-title__position";
    }

    default List<JobsOfferMetadata> getMetadata() {
        return Arrays.stream(JobsOfferMetadata.values()).toList();
    }


}
