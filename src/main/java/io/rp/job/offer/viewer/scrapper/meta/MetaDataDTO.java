package io.rp.job.offer.viewer.scrapper.meta;


/**
 * A marker interface representing job offers related meta information like position, salary, company, job requirements, etc.
 */
public interface MetaDataDTO {


}

/**
 * Job position/role title
 * @param title
 */
record TitleDTO(String title) implements MetaDataDTO {
}

/**
 * todo: please, implement me!
 * @param data plain inner text of any html tag
 */
record NotCompleteImplementationDTO(String data) implements MetaDataDTO {
}