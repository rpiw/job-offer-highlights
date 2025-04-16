package io.rp.job.offer.viewer.scrapper.meta;

import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Arrays;

@Getter
public enum AgreementType {

    /*
     keeping markingString in the enum is not a good idea as they change with language and country
     */
	B2B("Business-to-business, self-employed", "VAT (B2B)"),
	CoE("Contract of employment", "brutto");
	
	private final String description;
    private final String markingString;

    AgreementType(String description, String markingString) {
        this.description = description;
        this.markingString = markingString;
    }

    /**
     * valueOf based on markingString field: match AgreementType by a marking string specific for the agreement type
     * on a popular portal with job offers.
     * @param matchedString string containing the markingString, should not contain more than one, as the only first in Stream
     *                      is being found - order is not guaranteed. If the string does not contain any then unchecked IllegalArgumentException
     *                      is thrown
     * @return matched AgreementType
     */
    @SneakyThrows(IllegalArgumentException.class)
    public static AgreementType match(String matchedString) {
        return Arrays.stream(AgreementType.values())
                .filter(agreementType -> matchedString.contains(agreementType.markingString))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
