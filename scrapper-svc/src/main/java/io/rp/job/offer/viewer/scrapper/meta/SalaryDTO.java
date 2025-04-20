package io.rp.job.offer.viewer.scrapper.meta;

import java.util.Optional;

public record SalaryDTO(int lowerBoundary, int higherBoundary, AgreementType agreementType) {

    public static Optional<SalaryDTO> salary(int lowerBoundary, int higherBoundary, AgreementType agreementType) {
        if (lowerBoundary <= 0 || higherBoundary <= 0 || agreementType == null) {
            return Optional.empty();
        }
        return Optional.of(new SalaryDTO(lowerBoundary, higherBoundary, agreementType));
    }
}


